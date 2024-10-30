package com.example.wswdemo.listener;

import com.alibaba.fastjson.JSON;
import com.example.wswdemo.config.RabbitMQConfig;
import com.example.wswdemo.pojo.dto.ReservationStatusMessage;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.service.IReservationsService;
import com.example.wswdemo.service.ISpaceService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelayedMessageReceiver implements ChannelAwareMessageListener {

    @Autowired
    private IReservationsService reservationsService;

    @Autowired
    private ISpaceService spaceService;

    @Override
    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE_NAME, ackMode = "MANUAL") // 设置手动ACK模式
    public void onMessage(Message message, Channel channel) throws Exception {
        String messageBody = new String(message.getBody());
        ReservationStatusMessage reservationStatusMessage = JSON.parseObject(messageBody, ReservationStatusMessage.class);

        try {
            Reservations reservation = reservationsService.getById(reservationStatusMessage.getReservationId());

            if (reservation == null || reservation.getSpaceId() == null) {
                // 手动确认消息，表示该消息已处理（）
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }

            Long spaceId = reservation.getSpaceId();

            if ("进行中".equals(reservationStatusMessage.getStatus())) {
                reservation.setReservationStatus(2); // 进行中
                reservationsService.updateById(reservation); // 更新预定状态
            } else if ("已完成".equals(reservationStatusMessage.getStatus())) {
                reservation.setReservationStatus(3); // 已完成
                reservationsService.updateById(reservation); // 更新预定状态

                // 修改场地状态
                Space space = spaceService.getById(spaceId);
                space.setStatus("0"); // 修改成未预约状态
                spaceService.updateById(space); // 更新场地状态
            }

            // 手动确认消息，表示消息已成功处理
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            // 出现异常，拒绝消息，并且重新入队
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            throw e; // 抛出异常
        }
    }
}
