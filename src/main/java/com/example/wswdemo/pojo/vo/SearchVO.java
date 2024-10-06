package com.example.wswdemo.pojo.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SearchVO {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String name;

    private BigDecimal price;

    private String url;

    private String type;
}
