package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.TemperaturePanel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenm
 * @create 2019-11-08 19:43
 */
public interface TemperaturePanelMapper {

    List<TemperaturePanel> temperature(TemperaturePanel temperaturePanel);

    TemperaturePanel selectById(@Param("id") Long id);

    List<Long> top12();
}
