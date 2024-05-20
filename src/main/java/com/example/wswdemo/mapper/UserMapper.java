package com.example.wswdemo.mapper;

import com.example.wswdemo.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Burtry
 * @since 2024-05-20
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
