package com.example.wswdemo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wswdemo.pojo.entity.Reservations;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper extends BaseMapper<Reservations> {
}
