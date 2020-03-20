package com.ruoyi.product.enums;

import lombok.Getter;

/**
 * @author chenm
 * @create 2019-09-29 15:00
 */
@Getter
public enum MeterEnum {

    克("g",1),千克("kg",1000),顿("t",1000000);

    private final String key;

    private final int value;

    private MeterEnum(String key ,int value){
        this.key = key;
        this.value = value;
    }

}
