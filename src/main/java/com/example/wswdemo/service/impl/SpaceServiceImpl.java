package com.example.wswdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.service.ISpaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Autowired
    private SpaceMapper spaceMapper;

    @Override
    public void add(Space space) {
        space.setCreateTime(LocalDateTime.now());
        space.setUpdateTime(LocalDateTime.now());
        save(space);
    }

    @Override
    public PageDTO<Space> getSpaceOfPage(PageQuery pageQuery) {

        //创建分页对象
        Page<Space> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        //构建查询条件
        QueryWrapper<Space> spaceQueryWrapper = new QueryWrapper<>();

        if (pageQuery.getSortBy() == null || pageQuery.getSortBy().isEmpty()) {
            //默认已更新时间降序排序
            spaceQueryWrapper.orderByDesc("update_time");
        } else {
            spaceQueryWrapper.orderByDesc(pageQuery.getSortBy());
        }

        //开始分页查询
        Page<Space> spacePage = spaceMapper.selectPage(page, spaceQueryWrapper);

        //数据校验
        List<Space> records = spacePage.getRecords();

        if (records.isEmpty()) {
            return new PageDTO<>(spacePage.getTotal(), spacePage.getPages(), Collections.emptyList());
        } else {
            return new PageDTO<>(page.getTotal(), page.getPages(),records);
        }
    }
}
