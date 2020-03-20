package com.ruoyi.product.service.impl;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.product.constant.ProductPlanConstants;
import com.ruoyi.product.domain.ProductPlan;
import com.ruoyi.product.mapper.ProductPlanMapper;
import com.ruoyi.product.service.IProductPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductPlanServiceImpl implements IProductPlanService {

    private static final Logger log = LoggerFactory.getLogger(ProductPlanServiceImpl.class);

    @Resource
    private ProductPlanMapper planMapper;


    /**
     * 新增信息
     *
     * @param plan
     */
    @Override
    public int insertProductPlan(ProductPlan plan) {
        return planMapper.insertProductPlan(plan);
    }

    /**
     * 校验编码
     *
     * @return 结果
     */
    @Override
    public String checkProductCodeUnique(ProductPlan plan) {
        Long planId = StringUtils.isNull(plan.getPlanId()) ? -1L :plan.getPlanId();
        ProductPlan prodect_Plan = planMapper.checkPlanCode(plan.getPlanName());
        if (StringUtils.isNotNull(prodect_Plan) && prodect_Plan.getPlanId().longValue() != planId.longValue())
        {
            return ProductPlanConstants.DICT_TYPE_NOT_UNIQUE;
        }
        return ProductPlanConstants.DICT_TYPE_UNIQUE;
    }

    /**
     * 查询产品管理数据
     *
     * @param plan
     */
    @Override
    public List<ProductPlan> selectProductPlanList(ProductPlan plan) {
        return planMapper.selectProductPlanList(plan);
    }

    @Override
    public ProductPlan selectByName(String name) {
        return planMapper.checkPlanCode(name);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object saveProductPlans(List<ProductPlan> productPlans) {
        Message msg = new Message();
            for(ProductPlan plan:productPlans){
                planMapper.insertProductPlan(plan);
            }
            msg.setCode(1);
            msg.setMsg("导入成功");
            return msg;
    }

    @Override
    public ProductPlan selectByPlanId(Long planId) {
        ProductPlan productPlan = planMapper.selectByPlanId(planId);
        System.out.println(productPlan);
        return productPlan;

    }

    @Override
    public int updateProductPlan(ProductPlan plan) {

        int flag = planMapper.updateProductPlan(plan);
        return flag;
    }

    @Override
    public int deleteProductPlanByIds(String ids) {
        Long[] proIds = Convert.toLongArray(ids);

        return planMapper.deleteProductPlanByIds(proIds);
    }


}
