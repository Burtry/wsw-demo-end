package com.example.wswdemo.controller;


import com.example.wswdemo.pojo.dto.EquipmentDTO;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.service.IEquipmentService;
import com.example.wswdemo.utils.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 器材表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private IEquipmentService equipmentService;


    @GetMapping()
    public Result<PageDTO<Equipment>> getEquipment(PageQuery pageQuery) {
        PageDTO<Equipment> equipmentPageDTO = equipmentService.getEquipmentOfPage(pageQuery);
        return Result.success(equipmentPageDTO,"分页获取成功！");
    }


    @PostMapping()
    public Result addEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        equipmentService.add(equipmentDTO);
        return Result.success();
    }

    @DeleteMapping()
    public Result deleteEquipment(Long id) {
        equipmentService.removeById(id);
        return Result.success("删除成功！");
    }

    @PutMapping()
    public Result updateEquipment(@RequestBody Equipment equipment) {
        equipmentService.lambdaUpdate()
                .set(Equipment::getEquipmentName,equipment.getEquipmentName())
                .set(Equipment::getEquipmentType,equipment.getEquipmentType())
                .set(Equipment::getImg,equipment.getImg())
                .set(Equipment::getRentalPrice,equipment.getRentalPrice())
                .set(Equipment::getStatus,equipment.getStatus())
                .set(Equipment::getDescription,equipment.getDescription())
                .set(Equipment::getUpdateTime, LocalDateTime.now())
                .eq(Equipment::getId,equipment.getId())
                .update();
        return Result.success("更新成功！");
    }

    @GetMapping("{id}")
    public Result<Equipment> getById(@PathVariable Long id) {
        Equipment equipment = equipmentService.getById(id);
        return Result.success(equipment,"获取成功！");
    }

    @GetMapping("/all")
    public Result<List<Equipment>> getAll() {
        List<Equipment> list = equipmentService.list();
        return Result.success(list,"获取成功！");
    }
}
