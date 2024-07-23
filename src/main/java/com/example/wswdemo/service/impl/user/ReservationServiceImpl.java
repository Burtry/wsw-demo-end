package com.example.wswdemo.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.mapper.UserMapper;
import com.example.wswdemo.mapper.user.ReservationMapper;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.pojo.vo.UserReservationVO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.service.user.IReservationService;
import com.example.wswdemo.utils.context.BaseContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservations> implements IReservationService{

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    @Override
    public List<UserReservationVO> getUserReservations(Integer radioStatus) {
        Long userId = BaseContext.getCurrentId();
        //查询用户详细信息
        Object object = redisTemplate.opsForValue().get("userId_" + userId);
        User user = objectMapper.convertValue(object, User.class);
        if (user == null) {
            //从数据库获取用户信息
            user = userMapper.selectById(userId);
        }

        // 查询预约表中该用户创建的预约
        //根据radioStatus进行查询，-1全部, 0已取消,1已预约,2进行中,3已完成
        QueryWrapper<Reservations> listQueryWrapper = new QueryWrapper<>();
        if (radioStatus != -1) {
            listQueryWrapper.eq("reservation_status",radioStatus);
        }
        listQueryWrapper.eq("user_id",userId);

        List<Reservations> reservationsList = reservationMapper.selectList(listQueryWrapper);

        if (reservationsList.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集所有场地ID
        Set<Long> spaceIds = reservationsList.stream()
                .map(Reservations::getSpaceId)
                .collect(Collectors.toSet());

        // 一次性查询所有相关的场地信息
        Map<Long, Space> spaceMap = spaceMapper.selectBatchIds(spaceIds).stream()
                .collect(Collectors.toMap(Space::getId, space -> space));

        // 封装VO
        User finalUser = user;
        List<UserReservationVO> userReservationVOList = reservationsList.stream().map(reservation -> {
            Space space = spaceMap.get(reservation.getSpaceId());

            UserReservationVO userReservationVO = new UserReservationVO();
            BeanUtil.copyProperties(reservation, userReservationVO);
            userReservationVO.setUsername(finalUser.getUsername());
            userReservationVO.setPhone(finalUser.getPhone());
            userReservationVO.setEmail(finalUser.getEMail());
            userReservationVO.setSpaceName(space.getSpaceName());
            userReservationVO.setSpaceType(space.getSpaceType());
            userReservationVO.setLocation(space.getLocation());

            userReservationVO.setImg(space.getImg());

            return userReservationVO;
        }).collect(Collectors.toList());

        return userReservationVOList;
    }

    @Override
    public void addReservation(ReservationsDTO reservationsDTO) {

        Reservations reservation = new Reservations();
        BeanUtil.copyProperties(reservationsDTO,reservation);
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setUpdateTime(LocalDateTime.now());
        reservation.setReservationStatus(1); //已预约状态
        save(reservation);
    }
}
