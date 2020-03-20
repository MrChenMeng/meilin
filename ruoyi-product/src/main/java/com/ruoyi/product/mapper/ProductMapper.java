package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    List<Product> selectProductList(Product product);

    Integer insertProduct(Product product);

    String getMaxProductNo(Product product);

    int updateProduct(Product product);

    Product selectProductById(Long productId);

    Product selectProductByUuid(String uuid);

    int deleteProductByIds(Long[] ids);

    int editState(@Param("id") Long id,@Param("state") String stste);

    String selectTemperature(Long productId);

    int removeCheckId(Long productId);

    int count(Product product);

    List<Product> productList(Product product);

    List<Product> capacity(Product product);

    Product selectProductByProductNo(String productNo);

    List<Product> speed(Product product);

    int examine(Long productId);
}
