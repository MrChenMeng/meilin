package com.ruoyi.product.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.product.domain.Product;

import java.text.ParseException;
import java.util.List;

/**
 * 产品 数据层
 *
 */
public interface IProductService {


    /**
     * 查询产品管理数据
     *
     */
    public List<Product> selectProductList(Product product);

    /**
     * 新增
     */
    public Long insertProduct(Product product) throws Exception;

    int updateProduct(Product product) throws Exception;

    Product selectProductById(Long productId);

    Product selectProductByProductNo(String productNo);

    int deleteProductByIds(String ids);

    int editCheckState(Long id,String state);

    //查询拌和温度
    public String selectTemperature(Long lineId);

    int count (Product product);

    List<Product> productList(Product product);

    Message saveProduct(Long id,Double weight);

    List<Product> capacity(Product product);

    List<Product> speed(Product product);

    //新增一键复制功能
    Long copyProduct(Long id) throws Exception;

    int examine(Long productId);
}
