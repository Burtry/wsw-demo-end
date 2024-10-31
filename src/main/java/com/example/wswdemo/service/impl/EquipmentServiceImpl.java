package com.example.wswdemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.wswdemo.constant.IndexConstant;
import com.example.wswdemo.pojo.dto.EquipmentDTO;
import com.example.wswdemo.pojo.dto.PageDTO;
import com.example.wswdemo.pojo.dto.PageQuery;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.mapper.EquipmentMapper;
import com.example.wswdemo.pojo.vo.SearchVO;
import com.example.wswdemo.service.IEquipmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
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
 * 器材表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Service
@Slf4j
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements IEquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public void add(EquipmentDTO equipmentDTO) {

        Arrays.stream(equipmentDTO.getImg())
                .map(imgUrl -> "\"" + imgUrl + "\"") // 将每个URL用双引号括起来
                .collect(Collectors.joining(", "));
        Equipment equipment = new Equipment();
        BeanUtil.copyProperties(equipmentDTO,equipment);
        long equipmentId = IdUtil.getSnowflake(1, 1).nextId();
        equipment.setId(equipmentId);
        equipment.setCreateTime(LocalDateTime.now());
        equipment.setUpdateTime(LocalDateTime.now());
        save(equipment);

        //删除redis中的数据
        log.info("删除redis数据");
        redisTemplate.delete("equipment_all");

        log.info("向es中添加数据");
        SearchVO searchVO = new SearchVO();
        searchVO.setName(equipment.getEquipmentName());
        searchVO.setType("equipment");
        searchVO.setPrice(equipment.getRentalPrice());
        searchVO.setUrl(equipment.getImg());
        searchVO.setId(equipmentId);

        IndexRequest indexRequest = new IndexRequest(IndexConstant.WSW_DEMO_TEST);
        indexRequest.id(String.valueOf(equipmentId)).source(JSON.toJSONString(searchVO), XContentType.JSON);

        try {
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            log.info("添加成功");
        } catch (IOException e) {
            log.error("添加失败");
            throw new RuntimeException(e);
        }
    }

    /**
     * 分页获取器材数据
     * @param pageQuery
     * @return
     */
    @Override
    public PageDTO<Equipment> getEquipmentOfPage(PageQuery pageQuery) {


        //构建查询条件
        Page<Equipment> page = Page.of(pageQuery.getPageNum(), pageQuery.getPageSize());

        QueryWrapper<Equipment> equipmentQueryWrapper = new QueryWrapper<>();
        if (pageQuery.getSortBy() == null || pageQuery.getSortBy().isEmpty()) {
            equipmentQueryWrapper.orderByDesc("update_time");//默认已创建时间降序排序
        } else {
            //按照sortBy排序
            equipmentQueryWrapper.orderByDesc(pageQuery.getSortBy());
        }

        IPage<Equipment> equipmentPage = equipmentMapper.selectPage(page, equipmentQueryWrapper);

        //数据校验
        List<Equipment> records = equipmentPage.getRecords();
        if(records.isEmpty()) {
            //空，返回空数据
            return new PageDTO<>(page.getTotal(),page.getPages(), Collections.emptyList());
        }

        return new PageDTO<>(page.getTotal(),page.getPages(),records);

    }

    @Override
    public void updateEquipment(Equipment equipment) {
            lambdaUpdate()
                .set(Equipment::getEquipmentName,equipment.getEquipmentName())
                .set(Equipment::getEquipmentType,equipment.getEquipmentType())
                .set(Equipment::getImg,equipment.getImg())
                .set(Equipment::getRentalPrice,equipment.getRentalPrice())
                .set(Equipment::getStatus,equipment.getStatus())
                .set(Equipment::getDescription,equipment.getDescription())
                .set(Equipment::getUpdateTime, LocalDateTime.now())
                .eq(Equipment::getId,equipment.getId())
                .update();

        UpdateRequest request = new UpdateRequest(IndexConstant.WSW_DEMO_TEST, String.valueOf(equipment.getId()));
        request.doc("name",equipment.getEquipmentName(),"price",equipment.getRentalPrice(),"url", equipment.getImg());

        try {
            restHighLevelClient.update(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
