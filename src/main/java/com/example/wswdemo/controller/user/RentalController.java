package com.example.wswdemo.controller.user;

import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.vo.UserRentalVO;
import com.example.wswdemo.service.user.IRentalService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user/rentals")
@Slf4j
public class RentalController {

    @Autowired
    private IRentalService rentalService;


    @GetMapping()
    public Result getUserRentals(Integer radioStatus) {

        List<UserRentalVO> list = rentalService.getUserRentals(radioStatus);
        return Result.success(list,"获取成功!");
    }

    @PostMapping()
    public Result addRental(@RequestBody RentalsDTO rentalsDTO) {
        log.info("添加器材预约");
        rentalService.addRental(rentalsDTO);
        return Result.success("添加成功！");
    }

}
