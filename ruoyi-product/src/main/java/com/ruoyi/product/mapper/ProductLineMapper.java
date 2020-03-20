package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.ProductLine;

import java.util.List;

public interface ProductLineMapper {

    Integer  insertProductLine(ProductLine productLine);

    /**
     * 校验編碼是否唯一
     *
     * @param lineCode
     * @return 结果
     */
    public ProductLine checkLineCode(String lineCode);

    List<ProductLine> selectProductLineList(ProductLine productLine);

    ProductLine selectProductLineById(Long lineId);

    Integer  updateProductLine(ProductLine productLine);

    Integer deleteProductLine(ProductLine productLine);

    Integer deleteProductLineByIds(Long[] ids);
}
