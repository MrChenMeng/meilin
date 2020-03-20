package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;


@Data
public class Material extends BaseEntity implements Serializable {
    private Long mid; //id

    private Long procedureId;//物料类型id

    private Long productId;//计划单id

    private String productNo;//计划单号procedure

    private String name;//物料名称

    private String unit;//计量单位

    private Integer datastate;//数据状态

    private String support;//供应商

    private Double dosage; //标准用量

    private String remark;//备注


    private String uuid;//唯一标识码

    private String type;//数据字典对应的物料类型

    private Double dosageActual;//实际用量

    private Double drift;//误差值

    private Integer count ;//上传称重计数（手机app使用）

    private int materialState=0;//生产中助剂配方修改次数（手机app使用）

    private String weightUnit;//重量单位（手机app使用）

    private Boolean flag = false;//判断是否机器取值

    private Procedure procedure;

    private int update;//不插入数据库，用来判断修改更改配方后，是否刷新数据库

    private String productName;


    private  Integer  sort;//排序字段

    private  Integer  major;//标记字段

}