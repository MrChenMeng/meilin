package com.ruoyi.product.service;

import com.ruoyi.product.domain.ProductPlan;

import java.util.List;

/**
 * 产品 数据层
 *
 */
public interface IProductPlanService {



    /**
     * 新增信息
     *
     */
    public int insertProductPlan(ProductPlan plan);


    /**
     * 校验编码
     *
     * @return 结果
     */
    public String checkProductCodeUnique(ProductPlan plan);


    /**
     * 查询产品管理数据
     */
    public List<ProductPlan> selectProductPlanList(ProductPlan plan);

    ProductPlan selectByName(String name);

    Object saveProductPlans(List<ProductPlan> productLines);

    ProductPlan selectByPlanId(Long planId);
    int updateProductPlan(ProductPlan plan);

    int deleteProductPlanByIds(String ids);

}
