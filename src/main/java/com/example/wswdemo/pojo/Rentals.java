package com.example.wswdemo.pojo;

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
 * 租借表
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("rentals")
public class Rentals implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租借id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 器材id
     */
    private Long equipmentId;

    /**
     * 租借开始时间
     */
    private LocalDateTime startTime;

    /**
     * 租借结束时间
     */
    private LocalDateTime endTime;

    /**
     * 租借状态(已完成/进行中)
     */
    private String rentalStatus;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
