package com.ruoyi.product.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.print.domain.PrintTemplate;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author chenm
 * @create 2019-09-26 14:16
 */
@Getter
@Setter
public class QRCode  extends BaseEntity implements Serializable {

    private Long id;            //id

    private Long lineId;        //产线id

    private String productNo;   //单号

    private Integer weight;      //重量

    private Double chengZhong;  //称重重量

    private int dataState;      //删除状态

    private Date sweepDate;     //扫码时间

    private String uuid;        //唯一标识码

    private Integer valid;      //是否扫码（1未扫码2已扫码）

    private Integer isPrint;    //二维码是否打印（1未打印，2已打印）

    private String unit;        //数据字典对应的计量单位

    private Long printId;       //打印模板id

    private int count;          //生成二维码数量，

    private String idStr;       //查询条件，不进数据库

    private PrintTemplate print;

    private Double completeSchedule; //完成进度百分比

    private Double Surplus ;//剩余未报

    private String productUint;//产品的计量单位

    private Double productAddNum;//产品累计数量

    private String codeUnit;   //二维码的计量单位

}
