package com.ruoyi.product.service.impl;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.UuidUtil;
import com.ruoyi.product.domain.QRCode;
import com.ruoyi.product.mapper.QRCodeMapper;
import com.ruoyi.product.service.IQRCodeService;
import com.ruoyi.product.unit.UtilMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author chenm
 * @create 2019-09-26 15:07
 */
@Service("qrCodeService")
public class IQRCodeServiceImpl implements IQRCodeService {

    @Resource
    private QRCodeMapper qrCodeMapper;

    @Override
    @Transactional
    public Message insertQRCode(QRCode qrCode) {
        Date date = new Date();
        Message msg = new Message();
        for(int i = 0;i<qrCode.getCount();i++){
            String comment_code = "";
            String max_code = qrCodeMapper.getMaxCodeNo(qrCode);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); // 时间字符串产生方式 2090807
            String uid_pfix = format.format(date);
            if(!StringUtils.isBlank(max_code) && max_code.contains(uid_pfix)) {
                String uid_end = max_code.substring(8, 13);//截取字符串最后四位 0001
                int endNum = Integer.parseInt(uid_end);//转为int类型 1
                int tmpNum = 100000 + endNum + 1;//订单号+1变成10002
                comment_code = uid_pfix + UtilMethod.subStr("" + tmpNum, 1);//把首位的1 去掉 再拼成2019080700002
            }else {
                comment_code = uid_pfix + "00001";
            }
            qrCode.setUuid(comment_code);
            qrCodeMapper.insertQRCode(qrCode);
        }
        msg.setCode(Message.getSuccess());
        msg.setMsg("操作成功");
        return msg;
    }

    @Override
    public int deleteQRCodeByIds(String idStr) {
        Long[] ids = Convert.toLongArray(idStr);
        return qrCodeMapper.deleteQRCodeByIds(ids);
    }

    @Override
    public int updateQRCode(QRCode qrCode) {
        return qrCodeMapper.updateQRCode(qrCode);
    }

    @Override
    public QRCode selectQRCodeById(Long id) {
        return qrCodeMapper.selectQRCodeById(id);
    }

    @Override
    public QRCode selectQRCodeByUuid(String uuid) {
        return qrCodeMapper.selectQRCodeByUuid(uuid);
    }

    @Override
    public List<QRCode> selectQRCodeList(QRCode qrCode) {
        return qrCodeMapper.selectQRCodeList(qrCode);
    }

    @Override
    public List<QRCode> selectCountGroup(QRCode qrCode) {
        return qrCodeMapper.selectCountGroup(qrCode);
    }

    @Override
    @Transactional
    public int checkOver(String idStr) {
        Long[] ids = Convert.toLongArray(idStr);
        return qrCodeMapper.checkOver(ids);
    }
}
