package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenm
 * @create 2019-08-14 13:55
 */
@Getter
@Setter
public class CheckStandard extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;//id

    private Long parentId;//父级id

    /** 祖级列表 */
    private String ancestors;

    private String checkName;//检测名称

    //检测编码
    private String checkCode;
    /** 显示顺序 */
    private Integer orderNum;//

    /** 父部门名称 */
    private String parentName="无上级";

    private Integer dataState = 1;//1有效，2刪除
}
