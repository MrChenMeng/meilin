package com.ruoyi.product.service;

import com.ruoyi.product.domain.Energy;
import com.ruoyi.product.domain.TemperaturePanel;

import java.util.List;

/**
 * @author chenm
 * @create 2019-11-08 19:39
 */
public interface ITemperaturePanelSevice {

    List<TemperaturePanel> temperature(TemperaturePanel temperaturePanel);

    TemperaturePanel selectById(Long id);

    List<Long> top12();
}
