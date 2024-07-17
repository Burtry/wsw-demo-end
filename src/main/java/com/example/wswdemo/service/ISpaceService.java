package com.example.wswdemo.service;

import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.SpaceDTO;
import com.example.wswdemo.pojo.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 场地表 服务类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
public interface ISpaceService extends IService<Space> {


    void add(SpaceDTO spaceDTO);

    PageDTO<Space> getSpaceOfPage(PageQuery pageQuery);
}
