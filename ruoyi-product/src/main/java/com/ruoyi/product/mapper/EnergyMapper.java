package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Energy;

import java.util.List;

/**
 * @author chenm
 * @create 2019-10-28 15:37
 */
public interface EnergyMapper {

    List<Energy> energy(Energy energy);

    List<Energy> byDay(Energy energy);

    List<Energy> byMonth(Energy energy);

    List<Energy> byYear(Energy energy);
}
