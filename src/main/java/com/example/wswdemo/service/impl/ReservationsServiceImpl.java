package com.example.wswdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.mapper.ReservationsMapper;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.service.IReservationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 预约表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
public class ReservationsServiceImpl extends ServiceImpl<ReservationsMapper, Reservations> implements IReservationsService {


    @Autowired
    private ReservationsMapper reservationsMapper;

    @Autowired
    private SpaceMapper spaceMapper;


    @Override
    public PageDTO<Reservations> pageResult(PageQuery pageQuery) {

        //构建分页参数
        Page<Reservations> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        //构建查询条件

        QueryWrapper<Reservations> reservationsQueryWrapper = new QueryWrapper<>();
        if(pageQuery.getSortBy() == null || pageQuery.getSortBy().isEmpty()) {
            //默认以更新时间排序
            reservationsQueryWrapper.orderByDesc("update_time");
        } else {
            reservationsQueryWrapper.orderByDesc(pageQuery.getSortBy());
        }

        //查询
        Page<Reservations> reservationsPage = reservationsMapper.selectPage(page, reservationsQueryWrapper);

        //数据校验
        List<Reservations> records = reservationsPage.getRecords();
        if (records.isEmpty()) {
            return new PageDTO<>(reservationsPage.getTotal(), reservationsPage.getPages(), Collections.emptyList());
        } else {
            return new PageDTO<>(reservationsPage.getTotal(), reservationsPage.getPages(), records);
        }

    }

    @Override
    public void updateReservations(ReservationsDTO reservationsDTO) {

        //修改预约记录表
        lambdaUpdate().set(Reservations::getUserId,reservationsDTO.getUserId())
                .set(Reservations::getSpaceId,reservationsDTO.getSpaceId())
                .set(Reservations::getStartTime,reservationsDTO.getStartTime())
                .set(Reservations::getEndTime,reservationsDTO.getEndTime())
                .set(Reservations::getReservationStatus,reservationsDTO.getReservationStatus())
                .set(Reservations::getRemark,reservationsDTO.getRemark())
                .set(Reservations::getUpdateTime, LocalDateTime.now())
                .eq(Reservations::getId,reservationsDTO.getId())
                .update();

    }

}
