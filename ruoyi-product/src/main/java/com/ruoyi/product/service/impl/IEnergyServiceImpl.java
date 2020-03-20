package com.ruoyi.product.service.impl;

import com.ruoyi.common.annotation.DataSource;
import com.ruoyi.common.enums.DataSourceType;
import com.ruoyi.product.domain.Energy;
import com.ruoyi.product.mapper.EnergyMapper;
import com.ruoyi.product.service.IEnergyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenm
 * @create 2019-10-28 15:34
 */
@Service
public class IEnergyServiceImpl implements IEnergyService {

    @Resource
    private EnergyMapper energyMapper;


    @Override
    @DataSource(value = DataSourceType.SLAVE)
    public List<Energy> energy(Energy energy) {
        if(energy.getType().equals("hourse")){
            return energyMapper.energy(energy);
        }else if(energy.getType().equals("day")){
            return energyMapper.byDay(energy);
        }else if(energy.getType().equals("month")){
            return energyMapper.byMonth(energy);
        }else if(energy.getType().equals("year")){
            return energyMapper.byYear(energy);
        }else{
            return null;
        }
    }

}
