package com.example.wswdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wswdemo.pojo.dto.EquipmentDTO;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.mapper.EquipmentMapper;
import com.example.wswdemo.service.IEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private EquipmentMapper equipmentMapper;


    @Override
    public void add(EquipmentDTO equipmentDTO) {

        Arrays.stream(equipmentDTO.getImg())
                .map(imgUrl -> "\"" + imgUrl + "\"") // 将每个URL用双引号括起来
                .collect(Collectors.joining(", "));
        Equipment equipment = new Equipment();
        BeanUtil.copyProperties(equipmentDTO, equipment);
        equipment.setCreateTime(LocalDateTime.now());
        equipment.setUpdateTime(LocalDateTime.now());
        save(equipment);
    }

    /**
     * 分页获取器材数据
     * @param pageQuery
     * @return
     */
    @Override
    public PageDTO<Equipment> getEquipmentOfPage(PageQuery pageQuery) {


        //构建查询条件
        Page<Equipment> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        QueryWrapper<Equipment> equipmentQueryWrapper = new QueryWrapper<>();
        if (pageQuery.getSortBy() == null || pageQuery.getSortBy().isEmpty()) {
            equipmentQueryWrapper.orderByDesc("update_time");//默认已创建时间降序排序
        } else {
            //按照sortBy排序
            equipmentQueryWrapper.orderByDesc(pageQuery.getSortBy());
        }

        IPage<Equipment> equipmentPage = equipmentMapper.selectPage(page, equipmentQueryWrapper);

        //数据校验
        List<Equipment> records = equipmentPage.getRecords();
        if(records.isEmpty()) {
            //空，返回空数据
            return new PageDTO<>(page.getTotal(),page.getPages(), Collections.emptyList());
        }

        return new PageDTO<>(page.getTotal(),page.getPages(),records);

    }
}
