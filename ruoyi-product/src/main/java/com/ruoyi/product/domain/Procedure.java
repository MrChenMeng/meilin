package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenm
 * @create 2019-09-10 15:28
 */
@Getter
@Setter
public class Procedure extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;//产品ID

    private String name;//产品名称

    private int dataState;//数据状态

    private String type;//数据字典对应的物料类型

    private String uuid;

    private String remark;


}
