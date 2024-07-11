package com.example.wswdemo.service;

import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Reservations;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 预约表 服务类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
public interface IReservationsService extends IService<Reservations> {

    /**
     * 分页获取预约信息
     * @param pageQuery
     * @return
     */
    PageDTO<Reservations> pageResult(PageQuery pageQuery);

    void updateReservations(ReservationsDTO reservationsDTO);
}
