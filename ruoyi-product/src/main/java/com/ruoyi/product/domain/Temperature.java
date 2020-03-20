package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @author chenm
 * @create 2019-08-02 10:17
 */
@Getter
@Setter
public class Temperature extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long teId;

    private Long lineId;//产线ID

    private Long productId;//产品ID

    private String status;//开关机状态

    private String place;//区域

    private Integer placeCount;//区域总数

    private Integer orderNum;//排序

    private String temperatureUpAvg="——" ;//上机标准

    private String temperatureDownAvg="——" ;//下机标准

    private String temperatureUpAct="——" ;//上机实际温度

    private String temperatureDownAct="——" ;//下机实际温度


    private Integer machineType;//上下机型（1，上机2，下机）

    private Integer machineCount;//机型数量

    private String machineTypes;//接受前台传递的机型数组

    private Long createUser;

    private Integer lineOrder;

    private ProductLine line;//s产线对象

}
