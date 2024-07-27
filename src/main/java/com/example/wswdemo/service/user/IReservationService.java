package com.example.wswdemo.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.vo.UserReservationVO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.utils.result.ReservationResult;

import java.util.List;

public interface IReservationService extends IService<Reservations> {

    List<UserReservationVO> getUserReservations(Integer radioStatus);

    ReservationResult addReservation(ReservationsDTO reservationsDTO);
}
