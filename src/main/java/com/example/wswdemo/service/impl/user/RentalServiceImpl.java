package com.example.wswdemo.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wswdemo.mapper.EquipmentMapper;
import com.example.wswdemo.mapper.UserMapper;
import com.example.wswdemo.mapper.user.RentalMapper;
import com.example.wswdemo.pojo.dto.RentalsDTO;
import com.example.wswdemo.pojo.entity.Equipment;
import com.example.wswdemo.pojo.entity.Rentals;
import com.example.wswdemo.pojo.entity.User;
import com.example.wswdemo.pojo.vo.UserRentalVO;
import com.example.wswdemo.service.user.IRentalService;
import com.example.wswdemo.utils.context.BaseContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl extends ServiceImpl<RentalMapper, Rentals> implements IRentalService {

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;


    @Override
    public List<UserRentalVO> getUserRentals(Integer radioStatus) {
        Long userId = BaseContext.getCurrentId();

        //在redis中查询是否存在该用户，存在则直接从redis中获取

        Object o = redisTemplate.opsForValue().get("userId_" + userId);
        User user = objectMapper.convertValue(o, User.class);
        if (user == null) {
            //从数据库获取数据
            user = userMapper.selectById(userId);
            //存入redis中
            redisTemplate.opsForValue().set("userId_" + userId,user);
        }

        //c查询租借表中该用户创建的租借信息
        QueryWrapper<Rentals> rentalsQueryWrapper = new QueryWrapper<>();

        if (radioStatus != -1) {
            //根据radioStatus条件查询
            rentalsQueryWrapper.eq("rental_status",radioStatus);
        }

        //添加用户条件
        rentalsQueryWrapper.eq("user_id",userId);


        //查询
        List<Rentals> rentalsList = rentalMapper.selectList(rentalsQueryWrapper);

        //校验
        if (rentalsList.isEmpty()) {
            return Collections.emptyList();
        }

        //收集器材信息
        Set<Long> equipmentIds = rentalsList.stream()
                .map(Rentals::getEquipmentId).collect(Collectors.toSet());

        Map<Long, Equipment> equipmentMap = equipmentMapper.selectBatchIds(equipmentIds).stream().collect(Collectors.toMap(Equipment::getId, equipment -> equipment));

        //封装VO
        User finalUser = user;
        List<UserRentalVO> userRentalVOList = rentalsList.stream().map(rental -> {
            Equipment equipment = equipmentMap.get(rental.getEquipmentId());
            UserRentalVO userRentalVO = new UserRentalVO();
            BeanUtil.copyProperties(rental,userRentalVO);
            userRentalVO.setUsername(finalUser.getUsername());
            userRentalVO.setPhone(finalUser.getPhone());
            userRentalVO.setEquipmentName(equipment.getEquipmentName());
            userRentalVO.setEquipmentType(equipment.getEquipmentType());
            userRentalVO.setImg(equipment.getImg());

            return userRentalVO;
        }).collect(Collectors.toList());

        return  userRentalVOList;
    }

    @Override
    public void addRental(RentalsDTO rentalsDTO) {
        Rentals rental = new Rentals();
        BeanUtil.copyProperties(rentalsDTO,rental);
        rental.setCreateTime(LocalDateTime.now());
        rental.setUpdateTime(LocalDateTime.now());
        rental.setRentalStatus(1);  //已租借
        save(rental);
    }


}
