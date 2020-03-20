package com.ruoyi.product.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.mapper.ProductLineMapper;
import com.ruoyi.product.service.IProductLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductLineServiceImpl implements IProductLineService {

    @Resource
    private ProductLineMapper productLineMapper;

    @Override
    public int insertProductLine(ProductLine productLine) {
        Integer num = productLineMapper.insertProductLine(productLine);
        return num;
    }

    @Override
    public List<ProductLine> selectProductLineList(ProductLine productLine) {
        List<ProductLine> list = productLineMapper.selectProductLineList(productLine);
        return list;
    }


    @Override
    public ProductLine selectProductLineById(Long lineId) {
        return productLineMapper.selectProductLineById(lineId);
    }

    /**
     * 校验編碼是否唯一
     *
     * @param productLine
     * @return 结果
     */
    @Override
    public String checkLineCode(ProductLine productLine) {
        Long lineId = StringUtils.isNull(productLine.getLineId()) ? -1L : productLine.getLineId();
        ProductLine prodect_Line = productLineMapper.checkLineCode(productLine.getLineCode());
        if (StringUtils.isNotNull(prodect_Line) && prodect_Line.getLineId().longValue() != lineId.longValue())
        {
            return UserConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    @Override
    public int updateProductLine(ProductLine productLine) {
        Integer num = productLineMapper.updateProductLine(productLine);
        return num;
    }

    @Override
    public int deleteProductLine(ProductLine productLine) {
        return productLineMapper.deleteProductLine(productLine);
    }

    @Override
    public int deleteProductLineByIds(String ids) throws Exception {
        Long[] lineIds = Convert.toLongArray(ids);
        for (Long dictId : lineIds)
        {
            /*ProductLine productLine = selectProductLineById(dictId);
            if (productLineMapper.countDictDataByType(dictType.getDictType()) > 0)
            {
                throw new BusinessException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }*/
        }

        return productLineMapper.deleteProductLineByIds(lineIds);
    }


}
