package com.example.wswdemo.service.impl;

import com.example.wswdemo.pojo.entity.Reservations;
import com.example.wswdemo.mapper.ReservationsMapper;
import com.example.wswdemo.service.IReservationsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
