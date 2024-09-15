package com.example.wswdemo.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wswdemo.config.RabbitMQConfig;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.mapper.UserMapper;
import com.example.wswdemo.mapper.user.ReservationMapper;
import com.example.wswdemo.pojo.dto.ReservationStatusMessage;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.pojo.vo.UserReservationVO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.service.user.IReservationService;
import com.example.wswdemo.utils.context.BaseContext;
import com.example.wswdemo.utils.result.ReservationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.desktop.AboutEvent;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

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
    public ReservationResult addReservation(ReservationsDTO reservationsDTO) {

        //判断该时间段是否有冲突的预约，如果有则返回 -1 ,成功则返回 1
        LocalDateTime startTime = reservationsDTO.getStartTime();
        LocalDateTime endTime = reservationsDTO.getEndTime();
        Long spaceId = reservationsDTO.getSpaceId();

        // 检查该时间段内是否有冲突的预约
        QueryWrapper<Reservations> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("space_id", spaceId)
                .and(wrapper -> wrapper
                        .between("start_time", startTime, endTime)
                        .or()
                        .between("end_time", startTime, endTime)
                        .or()
                        .le("start_time", startTime)
                        .ge("end_time", endTime));

        List<Reservations> conflictingReservationList = reservationMapper.selectList(queryWrapper);
        if (!conflictingReservationList.isEmpty()) {
            // 存在冲突预约，返回 -1
            //获取第一个冲突返回
            return ReservationResult.error(conflictingReservationList.get(0).getStartTime(),conflictingReservationList.get(0).getEndTime(),-1);
        }

        //添加预约
        Reservations reservation = new Reservations();
        BeanUtil.copyProperties(reservationsDTO,reservation);
        reservation.setCreateTime(LocalDateTime.now());
        reservation.setUpdateTime(LocalDateTime.now());
        reservation.setReservationStatus(1); //已预约状态
        save(reservation);

        //修改场地状态
        Space space = spaceMapper.selectById(spaceId);
        space.setStatus("1");//已预约
        spaceMapper.updateById(space);


        LocalDateTime reservationStartTime = reservation.getStartTime();
        LocalDateTime reservationEndTime = reservation.getEndTime();


        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 计算当前时间与过去时间的 Duration
        Duration durationFromPast1 = Duration.between(now, reservationStartTime);
        Duration durationFromPast2 = Duration.between(now, reservationEndTime);

        // 获取相差的毫秒数
        long millisecondsFromPast1 = durationFromPast1.toMillis();
        long millisecondsFromPast2 = durationFromPast2.toMillis();

        //封装对象
        ReservationStatusMessage reservationStatusMessageStart = new ReservationStatusMessage();
        reservationStatusMessageStart.setReservationId(reservation.getId());
        reservationStatusMessageStart.setStatus("进行中");

        ReservationStatusMessage reservationStatusMessageEnd = new ReservationStatusMessage();
        reservationStatusMessageEnd.setReservationId(reservation.getId());
        reservationStatusMessageEnd.setStatus("已完成");

        //发送消息到延迟队列中
        sendDelayedMessage(RabbitMQConfig.DELAYED_EXCHANGE_NAME,RabbitMQConfig.ROUTING_KEY,reservationStatusMessageStart,millisecondsFromPast1);

        sendDelayedMessage(RabbitMQConfig.DELAYED_EXCHANGE_NAME,RabbitMQConfig.ROUTING_KEY,reservationStatusMessageEnd,millisecondsFromPast2);


        return ReservationResult.success(1);
    }

    /**
     * 发送消息到延迟队列中
     * @param exchange
     * @param routingKey
     * @param messageContent
     * @param delay
     */
    private void sendDelayedMessage(String exchange, String routingKey, ReservationStatusMessage messageContent, long delay) {
        // 设置消息属性（包括延迟时间）
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-delay", delay);

        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDelayLong(delay);
        messageProperties.getHeaders().putAll(headers);

        Message message = new Message(JSON.toJSONBytes(messageContent), messageProperties);

        // 发送消息
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        System.out.println("Sent message: " + messageContent + " with delay: " + delay + " ms");
    }

    @Override
    public ReservationResult findReservationsByDateTime(LocalDateTime localDateTime) {

        QueryWrapper<Reservations> reservationsQueryWrapper = new QueryWrapper<>();
        //添加

        reservationsQueryWrapper.le("start_time", localDateTime)
                .ge("end_time", localDateTime);

        List<Reservations> reservations = reservationMapper.selectList(reservationsQueryWrapper);

        if (reservations.isEmpty()) {
            return ReservationResult.success(1);
        }
        //返回第一个
        return ReservationResult.error(reservations.get(0).getStartTime(),reservations.get(0).getEndTime(),-1);
    }
}
