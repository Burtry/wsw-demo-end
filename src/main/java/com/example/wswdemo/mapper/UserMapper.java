package com.example.wswdemo.mapper;

import com.example.wswdemo.pojo.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
