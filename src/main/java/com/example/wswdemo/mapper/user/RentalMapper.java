package com.example.wswdemo.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wswdemo.pojo.entity.Rentals;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RentalMapper extends BaseMapper<Rentals> {
}
