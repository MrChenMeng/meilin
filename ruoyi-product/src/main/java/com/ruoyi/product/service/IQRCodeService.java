package com.ruoyi.product.service;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.product.domain.QRCode;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-26 15:06
 */
public interface IQRCodeService {

    Message insertQRCode(QRCode qrCode);

    int deleteQRCodeByIds(String idStr);

    int updateQRCode(QRCode qrCode);

    QRCode selectQRCodeById(Long id);

    QRCode selectQRCodeByUuid(String uuid);

    List<QRCode> selectQRCodeList(QRCode qrCode);

    public List<QRCode> selectCountGroup(QRCode qrCode);

    public int checkOver(String idStr);

}
