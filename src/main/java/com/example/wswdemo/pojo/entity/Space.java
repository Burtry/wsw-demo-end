package com.example.wswdemo.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Space implements Serializable {

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

    /**
     * 位置
     */
    private String location;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 描述
     */
    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
