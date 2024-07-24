package com.example.wswdemo.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.wswdemo.pojo.entity.Favorite;
import com.example.wswdemo.pojo.entity.Space;
import com.example.wswdemo.pojo.vo.FavoriteVO;
import com.example.wswdemo.service.IEquipmentService;
import com.example.wswdemo.service.ISpaceService;
import com.example.wswdemo.service.user.IFavoriteService;
import com.example.wswdemo.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 收藏表 前端控制器
 * </p>
 *
 * @author Burtry
 * @since 2024-07-24
 */
@RestController
@RequestMapping("/user/favorite")
@Slf4j
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;


    @GetMapping()
    public Result<List<FavoriteVO>> getFavoriteList() {
        List<FavoriteVO> list = favoriteService.getUserFavoriteList();
        return Result.success(list,"获取成功!");
    }


    @PostMapping()
    public Result addFavorite(@RequestBody Favorite favorite) {

        QueryWrapper<Favorite> favoriteQueryWrapper = new QueryWrapper<>();
        favoriteQueryWrapper.eq("user_id",favorite.getUserId());
        if (favorite.getFavoriteType() == 1) {  //favoriteType -> 1场地 ,2器材
            //从场地查询场地
            favoriteQueryWrapper.eq("favorite_id",favorite.getFavoriteId());
            favoriteQueryWrapper.eq("favorite_type",favorite.getFavoriteType());
            Favorite one = favoriteService.getOne(favoriteQueryWrapper);
            if (one != null) {
                //收藏表存在，直接返回
                return Result.error("该场地已收藏!");
            }
            //添加收藏
            favorite.setFavoriteTime(LocalDateTime.now());
            favoriteService.save(favorite);
            return Result.success("收藏成功！");

        } else {
            //从器材查询
            favoriteQueryWrapper.eq("favorite_id",favorite.getFavoriteId());
            favoriteQueryWrapper.eq("favorite_type",favorite.getFavoriteType());
            Favorite one = favoriteService.getOne(favoriteQueryWrapper);
            if (one != null) {
                return Result.error("该器材已收藏!");
            }

            favorite.setFavoriteTime(LocalDateTime.now());
            favoriteService.save(favorite);
            return Result.success("收藏成功!");

        }
    }


}
