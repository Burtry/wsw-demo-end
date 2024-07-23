package com.example.wswdemo.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRentalVO {

    /**
     * 预约id
     */
    private Long id;

    /**
     * 预约人
     */
    private String username;

    /**
     * 预约人手机号
     */
    private String phone;

    /**
     * 预约人邮箱
     */
    private String email;

    /**
     * 器材名称
     */
    private String equipmentName;

    /**
     * 器材类型
     */
    private String equipmentType;

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
     * 租借状态
     */
    private Integer rentalStatus;


    private String img;

    /**
     * 备注
     */
    private String remark;
}
