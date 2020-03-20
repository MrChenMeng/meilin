package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.ProductPlan;

import java.util.List;

public interface ProductPlanMapper {

     List<ProductPlan> selectProductPlanList(ProductPlan plan);

     Integer insertProductPlan(ProductPlan plan);


    /**
     * 校验编码
     *
     * @param planName 编码
     * @return 结果
     */
     ProductPlan checkPlanCode(String planName);

    ProductPlan selectByPlanId(Long planId);

    int updateProductPlan(ProductPlan productPlan);
    int deleteProductPlanByIds(Long[] proIdS);


}
