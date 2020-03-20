package com.ruoyi.product.service.impl;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.product.domain.TemperaturePanel;
import com.ruoyi.product.mapper.TemperaturePanelMapper;
import com.ruoyi.product.service.ITemperaturePanelSevice;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenm
 * @create 2019-11-08 19:41
 */
@Service
public class TemperaturePanelSeviceImpl implements ITemperaturePanelSevice {

    @Resource
    private TemperaturePanelMapper temperaturePanelMapper;

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public List<TemperaturePanel> temperature(TemperaturePanel temperaturePanel) {
        return temperaturePanelMapper.temperature(temperaturePanel);
    }

    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public TemperaturePanel selectById(Long id) {
        return temperaturePanelMapper.selectById(id);
    }


    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public List<Long> top12() {
        return temperaturePanelMapper.top12();
    }
}
