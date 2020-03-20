package com.ruoyi.product.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Standard extends BaseEntity implements Serializable {
    private Long id;

    @Excel(name = "检测项目")
    private String standardName;//检测名称

    @Excel(name = "标准值")
    private String standardNumber;//标准值

    @Excel(name = "检测值")
    private String detectedNumber;//检测值

    @Excel(name = "上限")
    private String upperNumber;//上限

    @Excel(name = "下限")
    private String lowerNumber;//下限

    private String remark;//备注

    private Integer datastate;//状态

    private String uuid;//唯一标识符

    private Long productId;//产品id

    private Long checkId;//检测行业id

    private CheckStandard check;//检测行标对象

}