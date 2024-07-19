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
 * 器材表
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("equipment")
public class EquipmentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 器材id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 器材名称
     */
    private String equipmentName;

    /**
     * 器材类型
     */
    private String equipmentType;

    /**
     * 图片url
     */
    private String[] img;


    /**
     * 租借价格
     */
    private BigDecimal rentalPrice;

    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
