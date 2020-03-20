package com.ruoyi.product.domain;

import lombok.Data;

@Data
public class ProductHistory {
    private Long id;

    private String updateTime;

    private String updateBy;

    private String createBy;

    private String createTime;

    private String remark;

    private String productNo;

}