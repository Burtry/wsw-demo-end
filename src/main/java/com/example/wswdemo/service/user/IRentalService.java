package com.example.wswdemo.service.user;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.entity.Rentals;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.pojo.vo.UserRentalVO;
import com.example.wswdemo.utils.result.ReservationResult;

import java.util.List;

public interface IRentalService extends IService<Rentals> {

    List<UserRentalVO> getUserRentals(Integer radioStatus);

    ReservationResult addRental(RentalsDTO rentalsDTO);
}
