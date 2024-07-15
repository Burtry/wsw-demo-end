package com.example.wswdemo.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserReservationVO {
    /**
     * 预约id
     */
    private Long id;

    /**
     * 预约人
     */
    private String username;

    /**
     * 场地名称
     */
    private String spaceName;

    /**
     * 场地类型
     */
    private String spaceType;

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
     * 预约状态
     */
    private Integer reservationStatus;

    /**
     * 位置
     */
    private String location;

    /**
     * 备注
     */
    private String remark;
}
