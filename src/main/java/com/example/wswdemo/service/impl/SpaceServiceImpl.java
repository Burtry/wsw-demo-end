package com.example.wswdemo.service.impl;

import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.service.ISpaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 场地表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space> implements ISpaceService {


    @Override
    public void add(Space space) {
        space.setCreateTime(LocalDateTime.now());
        space.setUpdateTime(LocalDateTime.now());
        save(space);
    }
}
