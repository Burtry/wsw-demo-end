package com.example.wswdemo.controller.user;

import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.pojo.vo.UserRentalVO;
import com.example.wswdemo.service.IEquipmentService;
import com.example.wswdemo.service.user.IRentalService;
import com.example.wswdemo.utils.result.ReservationResult;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("user/rentals")
@Slf4j
public class RentalController {

    @Autowired
    private IRentalService rentalService;

    @Autowired
    private IEquipmentService equipmentService;

    private final ReentrantLock lock = new ReentrantLock();


    @GetMapping()
    public Result getUserRentals(Integer radioStatus) {

        List<UserRentalVO> list = rentalService.getUserRentals(radioStatus);
        return Result.success(list,"获取成功!");
    }

    @PostMapping()
    public Result addRental(@RequestBody RentalsDTO rentalsDTO) {
        // 尝试获取锁，立即返回加锁结果
        if (lock.tryLock()) {
            try {
                log.info("添加器材租借");
                ReservationResult result = rentalService.addRental(rentalsDTO);
                if (result.getCode() == -1) {
                    return Result.error("添加失败", result);
                }

                Equipment equipment = equipmentService.getById(rentalsDTO.getEquipmentId());
                equipment.setStatus("1"); // 设置器材为租借状态
                equipmentService.updateById(equipment);

                return Result.success("添加成功！");
            } finally {
                lock.unlock(); // 释放锁
            }
        } else {
            // 未获取到锁，立即返回错误信息
            return Result.error("器材正在被操作，请稍后再试！");
        }
    }

}
