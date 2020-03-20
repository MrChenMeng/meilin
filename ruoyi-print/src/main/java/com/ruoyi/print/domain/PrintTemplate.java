package com.ruoyi.print.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenm
 * @create 2019-09-17 14:54
 */
@Getter
@Setter
public class PrintTemplate extends BaseEntity {

    private Long id;

    private String name;//模板名称

    private String content;//模板内容

    private int dataStatus;//删除字段

    private String status;//数据状态 "0=正常,1=停用"

}
