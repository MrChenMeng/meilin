package com.ruoyi.product.service.impl;

import com.ruoyi.product.domain.ProductHistory;
import com.ruoyi.product.mapper.ProductHistoryMapper;
import com.ruoyi.product.service.IProductHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductHistoryServiceImpl implements IProductHistoryService {

	@Resource
	private ProductHistoryMapper productHistoryMapper;

	@Override
	public List<ProductHistory> selectByProductId(Long prouctId) {
		return productHistoryMapper.selectByProductId(prouctId);
	}

	@Override
	public ProductHistory selectById(Long id) {
		return productHistoryMapper.selectByPrimaryKey(id);
	}
}
