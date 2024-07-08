package com.example.wswdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.mapper.EquipmentMapper;
import com.example.wswdemo.service.IEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 器材表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements IEquipmentService {

    @Override
    public void add(Equipment equipment) {
        Equipment newEquipment = BeanUtil.copyProperties(equipment, Equipment.class);
        newEquipment.setCreateTime(LocalDateTime.now());
        newEquipment.setUpdateTime(LocalDateTime.now());
        save(newEquipment);
    }
}
