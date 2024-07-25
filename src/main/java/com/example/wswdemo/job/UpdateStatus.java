package com.example.wswdemo.job;

import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.service.IRentalsService;
import com.example.wswdemo.service.IReservationsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UpdateStatus implements Job {


    /**
     * 用于定时更新预约和租借状态
     * @param jobExecutionContext
     * @throws JobExecutionException
     */

    @Autowired
    private IReservationsService reservationsService;

    @Autowired
    private IRentalsService rentalsService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LocalDateTime now = LocalDateTime.now();

        // 查询所有需要更新的预约记录
        List<Reservations> reservationsList = reservationsService.lambdaQuery()
                .ne(Reservations::getReservationStatus, 3).list();//排除已完成

        //更新预约状态
        List<Reservations> updatedReservations = reservationsList.stream().map(reservation -> {
            if (reservation.getReservationStatus() == 0) {  //已取消
                reservation.setReservationStatus(0);
            } else if (reservation.getEndTime().isBefore(now)) {
                reservation.setReservationStatus(3);  //已完成
            } else if (reservation.getStartTime().isBefore(now) && reservation.getEndTime().isAfter(now)) {
                reservation.setReservationStatus(2);  //进行中
            } else if (reservation.getStartTime().isAfter(now)) {
                reservation.setReservationStatus(1);  //已预约
            }
            return reservation;
        }).toList();

        // 批量更新预约记录
        reservationsService.updateBatchById(updatedReservations);
    }
}
