package com.example.wswdemo.job;

import com.example.wswdemo.constant.RentalStatusConstant;
import com.example.wswdemo.constant.ReservationStatusConstant;
import com.example.wswdemo.pojo.entity.Rentals;
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
                .ne(Reservations::getReservationStatus, ReservationStatusConstant.COMPLETED)
                .ne(Reservations::getReservationStatus,ReservationStatusConstant.CANCEL)
                .list();//排除已完成、已取消

        //更新预约状态
        List<Reservations> updatedReservations = reservationsList.stream().map(reservation -> {
             if (reservation.getEndTime().isBefore(now)) {
                reservation.setReservationStatus(ReservationStatusConstant.COMPLETED);  //已完成
            } else if (reservation.getStartTime().isBefore(now) && reservation.getEndTime().isAfter(now)) {
                reservation.setReservationStatus(ReservationStatusConstant.CONTINUING);  //进行中
            } else if (reservation.getStartTime().isAfter(now)) {
                reservation.setReservationStatus(ReservationStatusConstant.RESERVED);  //已预约
            }
            return reservation;
        }).toList();

        // 批量更新预约记录
        reservationsService.updateBatchById(updatedReservations);

        //查询所有需要更新的租借记录
        List<Rentals> rentalsList = rentalsService.lambdaQuery()
                .ne(Rentals::getRentalStatus, RentalStatusConstant.CANCEL)
                .ne(Rentals::getRentalStatus, RentalStatusConstant.RETURNED)
                .list();//排除已取消 、已归还  //TODO 未归还待完成

        //更新预约状态
        List<Rentals> updatedRentals = rentalsList.stream().map(rental -> {
            if (rental.getStartTime().isAfter(now)) {   //开始时间在当前时间之后 now < startTime
                rental.setRentalStatus(RentalStatusConstant.RENTED);    //已租借
            } else if (rental.getStartTime().isBefore(now) && rental.getEndTime().isAfter(now)) {//开始时间在当前时间之前，结束时间在当前时间之后{startTime < now < endTime}
                rental.setRentalStatus(RentalStatusConstant.CONTINUING); //进行中
            } else if (rental.getEndTime().isBefore(now)) { //结束时间在当前时间之前  endTime < now
                rental.setRentalStatus(RentalStatusConstant.NOT_RETURNED);  //未归还
            }
            return rental;
        }).toList();

        //批量更新租借记录
        rentalsService.updateBatchById(updatedRentals);

    }
}
