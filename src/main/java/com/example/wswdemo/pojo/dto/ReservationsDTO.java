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
 * 预约表
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Data

public class ReservationsDTO implements Serializable {
    /**
     * 预约id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 场地id
     */
    private Long spaceId;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 预约状态(3已完成/2进行中/1已预约/0已取消)
     */
    private Integer reservationStatus;

    /**
     * 备注
     */
    private String remark;



}
