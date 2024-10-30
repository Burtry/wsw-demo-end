package com.example.wswdemo.controller.user;

import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.vo.UserReservationVO;
import com.example.wswdemo.service.user.IReservationService;
import com.example.wswdemo.utils.result.ReservationResult;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("user/reservation")
@Slf4j
public class ReservationController {

    @Autowired
    private IReservationService reservationService;


    @GetMapping()
    public Result getUserReservations(Integer radioStatus) {
        List<UserReservationVO> reservationList = reservationService.getUserReservations(radioStatus);
        return Result.success(reservationList, "获取成功!");
    }


    @PostMapping()
    public Result addReservation(@RequestBody ReservationsDTO reservationsDTO) {
        log.info("添加预约:" + reservationsDTO);
        ReservationResult result = reservationService.addReservation(reservationsDTO);
        //结果为-1，预约冲突，重新选择预约实现,
        if (result.getCode() == -1) {
            return Result.error("添加失败", result);
        }

        return Result.success("添加成功！");
    }


    @GetMapping("/date")
    public Result getLocalDateTimeReservationInfo(@RequestParam("localDateTime") String localDateTimeStr,@RequestParam("spaceId") Long spaceId) {

        log.info("获取在当前时间段已有的预约记录");
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            ZonedDateTime utcZonedDateTime = ZonedDateTime.parse(localDateTimeStr, formatter);
            LocalDateTime localDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();

            ReservationResult result = reservationService.findReservationsByDateTime(localDateTime,spaceId);
            return Result.success(result, "成功获取已有预约");
        } catch (DateTimeParseException e) {
            log.error("日期时间解析失败: {}", e.getMessage());
            return Result.error("日期时间格式错误");
        }

    }


    @GetMapping("/dateAll")
    public Result getReservationAll(@RequestParam("spaceId") Long spaceId) {
        List<Reservations> list = reservationService.getAllReservationBySpaceId(spaceId);
        return Result.success(list,"获取成功!");
    }
}
