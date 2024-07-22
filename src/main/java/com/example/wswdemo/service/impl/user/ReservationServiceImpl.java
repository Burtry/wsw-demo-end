package com.example.wswdemo.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.mapper.UserMapper;
import com.example.wswdemo.mapper.user.ReservationMapper;
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
    public List<UserReservationVO> getUserReservations() {
        Long userId = BaseContext.getCurrentId();
        //查询用户详细信息
        Object object = redisTemplate.opsForValue().get("userId_" + userId);
        User user = objectMapper.convertValue(object, User.class);
        if (user == null) {
            //从数据库获取用户信息
            user = userMapper.selectById(userId);
        }

        //获取用户名
        String username = user.getUsername();

        // 查询预约表中该用户创建的预约
        List<Reservations> reservationsList = lambdaQuery().eq(Reservations::getUserId, userId).list();
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
        List<UserReservationVO> userReservationVOList = reservationsList.stream().map(reservation -> {
            Space space = spaceMap.get(reservation.getSpaceId());

            UserReservationVO userReservationVO = new UserReservationVO();
            BeanUtil.copyProperties(reservation, userReservationVO);
            userReservationVO.setUsername(username);
            userReservationVO.setSpaceName(space.getSpaceName());
            userReservationVO.setSpaceType(space.getSpaceType());
            userReservationVO.setLocation(space.getLocation());
            userReservationVO.setImg(space.getImg());

            return userReservationVO;
        }).collect(Collectors.toList());

        return userReservationVOList;
    }
}
