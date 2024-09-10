package com.example.wswdemo.job;

import com.example.wswdemo.constant.RentalStatusConstant;
import com.example.wswdemo.constant.ReservationStatusConstant;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.pojo.entity.Rentals;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.service.IEquipmentService;
import com.example.wswdemo.service.IRentalsService;
import com.example.wswdemo.service.IReservationsService;
import com.example.wswdemo.service.ISpaceService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@Transactional
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

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private IEquipmentService equipmentService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {

        LocalDateTime now = LocalDateTime.now();
        //log.info("更新预约记录");
        //// 查询所有需要更新的预约记录
        //log.info("查询所有要更新的预约记录");
        //List<Reservations> reservationsList = reservationsService.lambdaQuery()
        //        .ne(Reservations::getReservationStatus, ReservationStatusConstant.COMPLETED)
        //        .ne(Reservations::getReservationStatus,ReservationStatusConstant.CANCEL)
        //        .list();//排除已完成、已取消
        //
        ////更新预约状态
        //List<Reservations> updatedReservations = reservationsList.stream().map(reservation -> {
        //     if (reservation.getEndTime().isBefore(now)) {
        //        reservation.setReservationStatus(ReservationStatusConstant.COMPLETED);  //已完成
        //    } else if (reservation.getStartTime().isBefore(now) && reservation.getEndTime().isAfter(now)) {
        //        reservation.setReservationStatus(ReservationStatusConstant.CONTINUING);  //进行中
        //    } else if (reservation.getStartTime().isAfter(now)) {
        //        reservation.setReservationStatus(ReservationStatusConstant.RESERVED);  //已预约
        //    }
        //    return reservation;
        //}).toList();
        //
        //if (updatedReservations.isEmpty()) {
        //    log.info("没有要更新的预约记录");
        //} else {
        //    // 批量更新预约记录
        //    reservationsService.updateBatchById(updatedReservations);
        //}
        //
        //
        //
        //log.info("更新场地状态");
        ////根据预约记录中的spaceId查询所有需要更新的场地
        //List<Long> spaceIds = reservationsList.stream()
        //        .map(Reservations::getSpaceId)
        //        .distinct()
        //        .collect(Collectors.toList());
        //if (spaceIds.isEmpty()) {
        //    log.info("没有要更新的场地！");
        //} else {
        //    //获取需要更新的场地信息
        //    List<Space> spaceList = spaceService.lambdaQuery()
        //            .in(Space::getId, spaceIds)
        //            .list();
        //    // 更新场地状态
        //    List<Space> updatedSpaces = spaceList.stream().map(space -> {
        //        // 获取当前场地的预约记录
        //        List<Reservations> spaceReservations = reservationsList.stream()
        //                .filter(reservation -> reservation.getSpaceId().equals(space.getId()))
        //                .collect(Collectors.toList());
        //
        //        // 判断场地状态
        //        boolean isContinuing = spaceReservations.stream().anyMatch(reservation ->
        //                reservation.getReservationStatus().equals(ReservationStatusConstant.CONTINUING)
        //        );
        //        boolean isReserved = spaceReservations.stream().anyMatch(reservation ->
        //                reservation.getReservationStatus().equals(ReservationStatusConstant.RESERVED)
        //        );
        //
        //        if (isContinuing) {
        //            space.setStatus("1"); // 当前时间使用中
        //        } else if (!isReserved) {
        //            space.setStatus("0"); // 当前时间未使用
        //        }
        //
        //        return space;
        //    }).toList();
        //
        //    // 批量更新场地记录
        //    if (updatedSpaces.isEmpty()) {
        //        log.info("没有要更新的预约记录");
        //    } else {
        //        spaceService.updateBatchById(updatedSpaces);
        //    }
        //
        //}

        log.info("更新租借状态");
        //查询所有需要更新的租借记录
        log.info("查询所有要更新的租借状态");
        List<Rentals> rentalsList = rentalsService.lambdaQuery()
                .ne(Rentals::getRentalStatus, RentalStatusConstant.CANCEL)
                .ne(Rentals::getRentalStatus, RentalStatusConstant.RETURNED)
                .list();//排除已取消 、已归还

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

        if (updatedRentals.isEmpty()) {
            log.info("没有要更新的租借记录");
        } else {
            //批量更新租借记录
            rentalsService.updateBatchById(updatedRentals);
        }

        log.info("更新器材状态");
        //根据租借记录中的EquipmentId查询所有需要更新的器材
        List<Long> equipmentIds = rentalsList.stream()
                .map(Rentals::getEquipmentId)
                .distinct()
                .collect(Collectors.toList());
        if (equipmentIds.isEmpty()) {
            log.info("没有要更新的器材!");
        } else {
            //查询需要更新的器材信息
            List<Equipment> equipmentList = equipmentService.lambdaQuery().in(Equipment::getId, equipmentIds).list();

            //更新器材状态
            List<Equipment> updateEquipments = equipmentList.stream().map(equipment -> {
                //获取当前器材的租借记录
                List<Rentals> equipmentRentals = rentalsList.stream().filter(rental -> rental.getEquipmentId().equals(equipment.getId()))
                        .collect(Collectors.toList());


                //判断器材状态
                 boolean isContinuing = equipmentRentals.stream().anyMatch(rental -> rental.getRentalStatus().equals(RentalStatusConstant.CONTINUING));//租借状态进行中

                boolean notReturn = equipmentRentals.stream().anyMatch(rental -> rental.getRentalStatus().equals(RentalStatusConstant.NOT_RETURNED));//租借状态未归还

                boolean rented = equipmentRentals.stream().anyMatch(rental -> rental.getRentalStatus().equals(RentalStatusConstant.RENTED));//租借状态已租借


                if (isContinuing) {
                    equipment.setStatus("1"); //租借进行中，器材已租借
                } else if (notReturn) {
                    equipment.setStatus("1");//租借超时未归还，器材已租借
                } else if (rented) {
                    equipment.setStatus("1");//器材已租借
                } else {
                    equipment.setStatus("0");//器材未租借
                }
                return equipment;
            }).toList();

            //批量更新器材记录
            if (updateEquipments.isEmpty()) {
                log.info("没有要更新的租借记录！");
            } else {
                equipmentService.updateBatchById(updateEquipments);
            }

        }

    }
}
