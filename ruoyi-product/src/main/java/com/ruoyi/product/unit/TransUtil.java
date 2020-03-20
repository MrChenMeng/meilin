package com.ruoyi.product.unit;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.product.enums.MeterEnum;

/**
 * @author chenm
 * @create 2019-09-29 15:13
 */
public class TransUtil {

    private static final MeterEnum[] meterEnums = MeterEnum.values();

    public static Message trans (String unit)throws BusinessException {
       boolean flag = false;
       String key = "";
        Message msg = new Message();
        for(MeterEnum meter : meterEnums){
            if(meter.getKey().toLowerCase().equals(unit.toLowerCase())){
                flag = true;
                msg.setCode(Message.getSuccess());
                msg.setMsg(meter.getKey());
                msg.setCount(meter.getValue());
            }
        }
        if(!flag){
            msg.setCode(Message.getError());
            msg.setMsg("计量单位不合法（g,kg,t）");

        }
        return  msg;
    }
}
