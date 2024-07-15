package com.example.wswdemo.controller.user;

import com.example.wswdemo.pojo.vo.UserReservationVO;
import com.example.wswdemo.service.user.IReservationService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user/reservation")
@Slf4j
public class ReservationController {

    @Autowired
    private IReservationService reservationService;

    @GetMapping()
    public Result getUserReservations() {
        List<UserReservationVO> reservationList = reservationService.getUserReservations();
        return Result.success(reservationList,"获取成功!");
    }
}
