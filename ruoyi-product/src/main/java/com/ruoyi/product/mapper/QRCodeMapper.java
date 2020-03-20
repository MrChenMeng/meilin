package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.QRCode;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-26 14:19
 */
public interface QRCodeMapper {

    int insertQRCode(QRCode qrCode);

    String getMaxCodeNo(QRCode qrCode);

    int deleteQRCodeByIds(Long[] ids);

    int updateQRCode(QRCode qrCode);

    QRCode selectQRCodeById(Long id);

    QRCode selectQRCodeByUuid(String uuid);

    List<QRCode> selectQRCodeList(QRCode qrCode);

    List<QRCode> selectCountGroup(QRCode qrCode);

    int checkOver(Long[] ids);

}
