package com.example.wswdemo.service.user;

import com.example.wswdemo.pojo.entity.Favorite;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wswdemo.pojo.vo.FavoriteVO;

import java.util.List;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author Burtry
 * @since 2024-07-24
 */
public interface IFavoriteService extends IService<Favorite> {

    List<FavoriteVO> getUserFavoriteList();

}
