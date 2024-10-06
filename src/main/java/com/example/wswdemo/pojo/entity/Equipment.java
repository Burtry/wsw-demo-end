package com.example.wswdemo.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonSerialize(using = ToStringSerializer.class)
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
     * 图片url(字符串)
     */

    private String img;

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
