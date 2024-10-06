package com.example.wswdemo.service;

import com.example.wswdemo.pojo.dto.EquipmentDTO;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.entity.Equipment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 器材表 服务类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
public interface IEquipmentService extends IService<Equipment> {

    void add(EquipmentDTO equipmentDTO);

    PageDTO<Equipment> getEquipmentOfPage(PageQuery pageQuery);

    void updateEquipment(Equipment equipment);
}
