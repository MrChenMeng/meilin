package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.ProductHistory;

import java.util.List;

public interface ProductHistoryMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProductHistory record);

    int insertSelective(ProductHistory record);

    ProductHistory selectByPrimaryKey(Long id);

    List<ProductHistory> selectByProductId(Long productNo);

    int updateByPrimaryKeySelective(ProductHistory record);

    int updateByPrimaryKey(ProductHistory record);
}