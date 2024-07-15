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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservations> implements IReservationService{

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SpaceMapper spaceMapper;
    @Override
    public List<UserReservationVO> getUserReservations() {
        Long userId = BaseContext.getCurrentId();
        //查询用户详细信息
        User user = userMapper.selectById(userId);
        //获取用户名
        String username = user.getUsername();

        // 查询预约表中该用户创建的预约
        List<Reservations> reservationsList = lambdaQuery().eq(Reservations::getUserId, userId).list();
        if (reservationsList.isEmpty()) {
            return Collections.emptyList();
        }

        // 收集所有场地ID
        List<Long> spaceIds = reservationsList.stream()
                .map(Reservations::getSpaceId)
                .collect(Collectors.toList());

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

            return userReservationVO;
        }).collect(Collectors.toList());

        return userReservationVOList;
    }
}
