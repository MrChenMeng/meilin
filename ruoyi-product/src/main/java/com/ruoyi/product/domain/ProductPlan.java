package com.ruoyi.product.domain;

import com.ruoyi.common.annotation.Excel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author chenm
 * @create 2019-07-30 18:52
 */

@Getter
@Setter
public class ProductPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long planId;//产品ID

    @Excel(name = "产品名称")
    private String planName;//产品名称

    private String remark;

    private Integer dataState;//数据状态
}
