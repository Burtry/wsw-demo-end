package com.example.wswdemo.pojo.entity;

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
 * 评价表
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reviews")
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 评价id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 评价类型(space/equipment)
     */
    private String reviewType;

    /**
     * 评价对象id
     */
    private Long itemId;

    /**
     * 评分(1-5)
     */
    private Integer rating;

    /**
     * 评价内容
     */
    private String comment;

    private LocalDateTime reviewTime;


}
