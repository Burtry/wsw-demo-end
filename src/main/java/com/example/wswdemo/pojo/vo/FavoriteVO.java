package com.example.wswdemo.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 收藏表
 * </p>
 *
 * @author Burtry
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("favorite")
public class FavoriteVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    /**
     * 收藏类型(1-场地,2-器材)
     */
    private Integer favoriteType;

    /**
     * 收藏物品名称
     */
    private String name;

    /**
     * 收藏物id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long favoriteId;

    /**
     * 收藏物图片(url)
     */
    private String img;

    /**
     * 收藏物品类型
     */
    private String type;


    /**
     * 收藏物描述
     */
    private String description;

    /**
     * 收藏时间
     */
    private LocalDateTime favoriteTime;


}
