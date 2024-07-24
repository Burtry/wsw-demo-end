package com.example.wswdemo.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wswdemo.mapper.EquipmentMapper;
import com.example.wswdemo.mapper.SpaceMapper;
import com.example.wswdemo.pojo.entity.Favorite;
import com.example.wswdemo.mapper.user.FavoriteMapper;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.pojo.vo.FavoriteVO;
import com.example.wswdemo.service.user.IFavoriteService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.wswdemo.utils.context.BaseContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author Burtry
 * @since 2024-07-24
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;


    @Override
    public List<FavoriteVO> getUserFavoriteList() {
        Long userId = BaseContext.getCurrentId();
        //查询条件
        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.eq("user_id",userId);
        //查询
        List<Favorite> favorites = favoriteMapper.selectList(favoriteQueryWrapper);

        List<FavoriteVO> favoriteVOList = new ArrayList<>();
        for (Favorite favorite : favorites) {
            FavoriteVO favoriteVO = new FavoriteVO();
            BeanUtil.copyProperties(favorite,favoriteVO);
            favoriteVOList.add(favoriteVO);
        }
        return favoriteVOList;
    }

}
