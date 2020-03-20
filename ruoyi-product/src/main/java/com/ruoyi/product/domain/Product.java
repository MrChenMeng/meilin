package com.ruoyi.product.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.Type;
import com.ruoyi.common.annotation.Excels;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenm
 * @create 2019-07-30 18:52
 */

@Getter
@Setter
public class Product extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productId;//产品ID

    private Long checkId;//檢測id；

    private Long lineId;//产线id

    @Excel(name = "型号")
    private String productName;//产品名称

    @Excel(name = "（班次）批号")
    private String productNo;//产品单号

    private String orderNo;//订单号

    private Long productCode;//产品编码

    private Double productPrice;//产品单价

    private String lineIds;//接收前端产线字符串

    private Double productNum;//产品数量

    private Double productAddNum = 0D;//产品累计数量

    private String productUnit;//产品单位（数据字典）

    private String productSpecs;//包装规格（数据字典）

    private String status ;//生产状态（数据字典）

    private String checkStatus;//检测状态 （数据字典）

    private Long createUserId;//创建人id

    private String uuid;//uuid唯一标识

    private String examine = "0";//审核状态

    /** 开始时间 */
    private String startTime;

    /** 结束时间 */
    private String endTime;

    private String checkTime; //检测时间

    private List<ProductLine> lineList;

    private List<Standard> standardList;

    private Integer mixDosage;//拌和缸数

    private Integer materialDosage;//物料缸数

    private Double completeSchedule;//完成进度

    private String completeScheduleView;//完成进度展示

    private String mixTemperature;//拌合温度

    private int mixCount;   //拌和累计缸数

    private int materialCount;      // 助剂累计缸数

    private int materialState=0;//生产中助剂配方修改次数

    private int mixState =0;//生产中拌和配方修改次数

    private Integer datastate;

    private int pageindex;

    private int pagesize;

    private int start;

    private int end;

    private String weightUnit;

    private String classNumber;

    private Date sweepDate;

    /** 部门对象 */
    @Excels({
            @Excel(name = "行业标准", targetAttr = "checkName", type = Type.EXPORT),
    })
    private CheckStandard check;//检测行标对象

}
