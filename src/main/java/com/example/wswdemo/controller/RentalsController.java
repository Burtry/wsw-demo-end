package com.example.wswdemo.controller;


import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.pojo.entity.Rentals;
import com.example.wswdemo.service.IEquipmentService;
import com.example.wswdemo.service.IRentalsService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 租借表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@RestController
@RequestMapping("/rentals")
@Slf4j
public class RentalsController {

    @Autowired
    private IRentalsService rentalsService;

    @Autowired
    private IEquipmentService equipmentService;


    @GetMapping()
    private Result<PageDTO<Rentals>> pageDTOResult(PageQuery pageQuery) {
      log.info("分页获取租借信息");
      PageDTO<Rentals> pageDTO = rentalsService.getResultOfPage(pageQuery);

      return Result.success(pageDTO,"获取成功！");
    }


    @DeleteMapping("{id}")
    public Result deleteRental(@PathVariable Long id) {
        log.info("删除租借id" + id);

        rentalsService.removeById(id);
        return Result.success("删除成功！");
    }


    @PutMapping()
    public Result updateRental(@RequestBody RentalsDTO rentalsDTO) {
        log.info("租借信息修改");
        rentalsService.updateRental(rentalsDTO);
        return Result.success("更新成功!");
    }

    @PutMapping("/status/{id}")
    public Result updateRentalStatus(@RequestParam Integer status, @PathVariable Long id) {
        log.info("更新id: "+ id + "状态为" + status);
        if (status == 4) {
            //已归还，则设置器材状态为0，未租借
            Rentals rental = rentalsService.getById(id);
            Long equipmentId = rental.getEquipmentId();
            Equipment equipment = equipmentService.getById(equipmentId);

            equipment.setStatus("0");
            equipmentService.updateById(equipment);
        }
        rentalsService.lambdaUpdate().set(Rentals::getRentalStatus,status)
                .eq(Rentals::getId,id).update();
        return Result.success("更新成功!");
    }
}
