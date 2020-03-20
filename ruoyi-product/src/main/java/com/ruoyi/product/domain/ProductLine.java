package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ProductLine extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long lineId;//产线ID

    private String lineCode;//产线编码

    private String lineName;//产线名称

    private Long createUserId;//创建人id

    private int orderNum;

    private int status;

    private Object pid = 0;



    @NotBlank(message = "名称不能为空")
    @Size(min = 0, max = 100, message = "名称长度不能超过100个字符")
    public String getLineName() {
        return lineName;
    }

}
