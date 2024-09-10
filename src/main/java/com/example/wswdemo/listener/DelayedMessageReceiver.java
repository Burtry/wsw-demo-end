package com.example.wswdemo.listener;


import com.alibaba.fastjson.JSON;
import com.example.wswdemo.config.RabbitMQConfig;
import com.example.wswdemo.pojo.dto.ReservationStatusMessage;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.service.IReservationsService;
import com.example.wswdemo.service.ISpaceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class DelayedMessageReceiver {


    @Autowired
    private IReservationsService reservationsService;

    @Autowired
    private ISpaceService spaceService;

    @RabbitListener(queues = RabbitMQConfig.DELAYED_QUEUE_NAME)
    public void receiveMessage(String message) {
        ReservationStatusMessage reservationStatusMessage = JSON.parseObject(message, ReservationStatusMessage.class);

        Reservations reservation = reservationsService.getById(reservationStatusMessage.getReservationId());
        Long spaceId = reservation.getSpaceId();

        if (reservation == null) {
            return;
        }
        if (reservationStatusMessage.getStatus().equals("进行中")) {
            reservation.setReservationStatus(2);//进行中
            //更新状态
            reservationsService.updateById(reservation);
        } else if (reservationStatusMessage.getStatus().equals("已完成")) {
            reservation.setReservationStatus(3);//已完成
            //更新状态
            reservationsService.updateById(reservation);
            //修改场地状态
            Space space = spaceService.getById(spaceId);
            space.setStatus("0");//修改成未预约状态
            spaceService.updateById(space);
        }
    }
}
