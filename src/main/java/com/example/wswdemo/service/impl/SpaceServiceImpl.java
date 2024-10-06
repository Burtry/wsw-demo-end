package com.example.wswdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wswdemo.constant.IndexConstant;
import com.example.wswdemo.mapper.user.ReservationMapper;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.dto.SpaceDTO;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.ISpaceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 场地表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
@Slf4j
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space> implements ISpaceService {

    @Autowired
    private SpaceMapper spaceMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void add(SpaceDTO spaceDTO) {
        Arrays.stream(spaceDTO.getImg())
                .map(imgUrl -> "\"" + imgUrl + "\"") // 将每个URL用双引号括起来
                .collect(Collectors.joining(", "));
        Space space = new Space();
        BeanUtil.copyProperties(spaceDTO,space);

        long spaceId = IdUtil.getSnowflake(1, 1).nextId();
        space.setId(spaceId);
        space.setCreateTime(LocalDateTime.now());
        space.setUpdateTime(LocalDateTime.now());
        save(space);

        //删除redis中的数据
        log.info("删除redis数据");
        redisTemplate.delete("space_all");

        log.info("向es中添加数据");
        SearchVO searchVO = new SearchVO();
        searchVO.setName(space.getSpaceName());
        searchVO.setType("space");
        searchVO.setPrice(space.getPrice());
        searchVO.setUrl(space.getImg());
        searchVO.setId(spaceId);

        IndexRequest indexRequest = new IndexRequest(IndexConstant.WSW_DEMO_TEST);
        indexRequest.id(String.valueOf(spaceId)).source(JSON.toJSONString(searchVO), XContentType.JSON);

        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("添加成功");
        } catch (IOException e) {
            log.error("添加失败");
            throw new RuntimeException(e);
        }
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


    @Override
    public void updateSpace(Space spaceDTO) {
        lambdaUpdate().set(Space::getSpaceName,spaceDTO.getSpaceName())
                .set(Space::getSpaceType,spaceDTO.getSpaceType())
                .set(Space::getLocation,spaceDTO.getLocation())
                .set(Space::getImg,spaceDTO.getImg())
                .set(Space::getPrice,spaceDTO.getPrice())
                .set(Space::getDescription,spaceDTO.getDescription())
                .eq(Space::getId,spaceDTO.getId())
                .update();

        //TODO 修改es中添加数据
    }

}
