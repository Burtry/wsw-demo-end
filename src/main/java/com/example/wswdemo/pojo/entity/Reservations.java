package com.example.wswdemo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 预约表
 * </p>
 *
 * @author Burtry
 * @since 2024-06-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reservations")
public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;

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
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 预约状态(已确认/已取消)
     */
    private Integer reservationStatus;

    /**
     * 备注
     */
    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
