package com.example.wswdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Rentals;
import com.example.wswdemo.mapper.RentalsMapper;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.service.IRentalsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 租借表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
public class RentalsServiceImpl extends ServiceImpl<RentalsMapper, Rentals> implements IRentalsService {

    @Autowired
    private RentalsMapper rentalsMapper;

    @Override
    public PageDTO<Rentals> getResultOfPage(PageQuery pageQuery) {

        //1.准备分页参数
        Page<Rentals> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());
        //2.准备条件参数
        QueryWrapper<Rentals> rentalsQueryWrapper = new QueryWrapper<>();

        if(pageQuery.getSortBy() == null ||pageQuery.getSortBy().isEmpty()) {
            //默认以更新时间降序
            rentalsQueryWrapper.orderByDesc("update_time");
        } else {
            rentalsQueryWrapper.orderByDesc(pageQuery.getSortBy());
        }
        //3.分页查询
        Page<Rentals> rentalsPage = rentalsMapper.selectPage(page, rentalsQueryWrapper);
        //4.数据校验
        List<Rentals> records = rentalsPage.getRecords();

        if (records.isEmpty()) {
            return new PageDTO<>(rentalsPage.getTotal(), rentalsPage.getPages(), Collections.emptyList());
        }

        return new PageDTO<>(rentalsPage.getTotal(), rentalsPage.getPages(), records);
    }

    @Override
    public void updateRental(RentalsDTO rentalsDTO) {

        lambdaUpdate().set(Rentals::getUserId,rentalsDTO.getUserId())
                .set(Rentals::getEquipmentId,rentalsDTO.getEquipmentId())
                .set(Rentals::getRentalStatus,rentalsDTO.getRentalStatus())
                .set(Rentals::getStartTime,rentalsDTO.getStartTime())
                .set(Rentals::getEndTime,rentalsDTO.getEndTime())
                .set(Rentals::getRemark,rentalsDTO.getRemark())
                .set(Rentals::getUpdateTime, LocalDateTime.now())
                .eq(Rentals::getId,rentalsDTO.getId())
                .update();
    }
}


