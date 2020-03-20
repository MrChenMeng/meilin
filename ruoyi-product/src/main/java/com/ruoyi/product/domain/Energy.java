package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author chenm
 * @create 2019-10-28 15:25
 */
@Getter
@Setter
public class Energy extends BaseEntity {

    private Date dd;

    private Date eTime;

    private String time;

    private String eName;

    private Float feng;

    private Float zong;

    private int lineId;

    private String lineIds;//接收前端产线字符串

    private String type;
}
