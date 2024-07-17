package com.example.wswdemo.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 场地表
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("space")
public class SpaceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 场地Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 场地名称
     */
    private String spaceName;

    /**
     * 场地类型
     */
    private String spaceType;

    private String status = "0";    //未预约状态

    /**
     * 位置
     */
    private String location;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片
     */
    private String[] img;

    /**
     * 描述
     */
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
