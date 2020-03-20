package com.ruoyi.product.service;

import com.ruoyi.product.domain.ProductHistory;

import java.util.List;

public interface IProductHistoryService {

	List<ProductHistory> selectByProductId(Long prouctId);

	ProductHistory selectById(Long id);
}
