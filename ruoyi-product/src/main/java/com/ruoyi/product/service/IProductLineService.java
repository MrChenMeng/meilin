package com.ruoyi.product.service;

import com.ruoyi.product.domain.ProductLine;

import java.util.List;

/**
 * 产线
 */
public interface IProductLineService {

    /**
     * 新增
     */
    public int insertProductLine(ProductLine productLine);


    /**
     * 校验編碼是否唯一
     *
     * @param productLine
     * @return 结果
     */
    public String checkLineCode(ProductLine productLine);


    /**
     * 查询产品管理数据
     */
    public List<ProductLine> selectProductLineList(ProductLine productLine);



    public ProductLine selectProductLineById(Long lineId);


    /**
     * 修改信息
     */
    public int updateProductLine(ProductLine productLine);


    /**
     * 删除信息
     */
    public int deleteProductLine(ProductLine productLine);


    public int deleteProductLineByIds(String ids) throws Exception;

}
