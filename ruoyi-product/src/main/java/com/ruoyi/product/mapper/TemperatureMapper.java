package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Temperature;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenm
 * @create 2019-08-02 10:21
 */
public interface TemperatureMapper {

    Integer  insertTemperature(Temperature temperature);

    List<Temperature> selectTemperatureList(Temperature temperature);

    List<Temperature> selectTemperatureListProductIdIsNull(Temperature temperature);

    Temperature selectTemperatureById(Long teId);

    Integer  updateTemperature(Temperature temperature);

    Integer deleteTemperatureByIds(Long[] ids);

    Integer deleteByproductId(Long productId);

    public List<Temperature> selectByIds(@Param("ids") String ids);

    int  insertListTemp(@Param("temperatureList")List<Temperature> temperatureList,@Param("productId")Long productId);

    Temperature selectTemperatureByLineIdLastTime(@Param("lineId") Long lineId);

    List<Temperature> selectTemperatureTemplate(@Param("lineId") Long lineId);
}
