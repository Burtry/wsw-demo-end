package com.example.wswdemo.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class RentalsDTO implements Serializable {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;


    /**
     * 租借结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime endTime;

    /**
     * 租借状态(3已完成/2进行中/1已确认/0已取消)
     */
    private Integer rentalStatus;

    /**
     * 备注
     */
    private String remark;


}
