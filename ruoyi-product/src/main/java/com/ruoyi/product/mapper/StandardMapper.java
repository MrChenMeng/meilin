package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Standard;

import java.util.List;

public  interface StandardMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Standard record);

    int insertSelective(Standard record);

    Standard selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Standard record);

    int updateByPrimaryKey(Standard record);

    int removeMaterialByIds(Long[] ids);

    List<Standard> selectByProductIds(Long[] ids);

    List<Standard> selectAll(Standard standard);

    List<Standard> selectByProductId(Long productId);

    int deleteByTemplate(Long checkId);
}