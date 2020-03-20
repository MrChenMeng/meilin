package com.ruoyi.product.domain;

import lombok.Data;

@Data
public class Equipment {
    private Long id;

    private String ipAddress;

    private String port;

    private String name;

    private Integer datastate;

    private String remark;

    private String type;

}