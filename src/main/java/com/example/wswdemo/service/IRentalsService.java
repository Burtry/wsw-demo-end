package com.example.wswdemo.service;

import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.dto.ReservationsDTO;
import com.example.wswdemo.pojo.entity.Rentals;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 租借表 服务类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
public interface IRentalsService extends IService<Rentals> {

    PageDTO<Rentals> getResultOfPage(PageQuery pageQuery);

    void updateRental(RentalsDTO rentalsDTO);
}
