package com.example.wswdemo.controller;


import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.SpaceDTO;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.service.ISpaceService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 场地表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@RestController
@RequestMapping("/space")
@Slf4j
public class SpaceController {

    @Autowired
    private ISpaceService spaceService;

    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping()
    public Result<PageDTO<Space>> getSpaceList(PageQuery pageQuery) {

        PageDTO<Space> spacePageDTO = spaceService.getSpaceOfPage(pageQuery);
        return Result.success(spacePageDTO,"获取成功！");
    }

    @PostMapping()
    public Result addSpace(@RequestBody SpaceDTO spaceDTO) {
        log.info("新增Space信息");
        spaceService.add(spaceDTO);
        return Result.success("操作成功!");
    }

    @DeleteMapping()
    public Result deleteSpace(Long id) {
        log.info("根据id删除场地");
        spaceService.removeById(id);
        return Result.success("操作成功!");
    }

    @PutMapping()
    public Result update(@RequestBody Space space) {
        log.info("更新场地信息");
        spaceService.updateSpace(space);
        return Result.success("更新成功!");
    }

    @GetMapping("/id")
    public Result<Space> getById(Long id) {
        log.info("场地id:" + id);
        Space space = spaceService.getById(id);
        return Result.success(space,"获取成功!");
    }

    @GetMapping("/all")
    public Result<List<Space>> getAll() {
        //先查看Reids是否存在，存在直接返回
        String redisKey = "all";

        List<Space> list;
        // 尝试从 Redis 中获取数据
        list = (List<Space>) redisTemplate.opsForValue().get(redisKey);

        if (list != null) {
            // 如果 Redis 中存在数据，直接返回
            return Result.success(list, "获取成功!");
        }
        //查询数据库
        list = spaceService.list();
        //TODO 添加到Redis中
        redisTemplate.opsForValue().set("all",list);
        return Result.success(list,"获取成功!");
    }


}
