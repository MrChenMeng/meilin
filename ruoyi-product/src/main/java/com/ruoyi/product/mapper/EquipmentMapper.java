package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Equipment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EquipmentMapper {
    int deleteByPrimaryKey(@Param("ids") String ids);

    int insert(Equipment record);

    int insertSelective(Equipment record);

    Equipment selectByPrimaryKey(Long id);

    List<Equipment> selectAll(Equipment equipment);

    int updateByPrimaryKeySelective(Equipment record);

    int updateByPrimaryKey(Equipment record);

    Equipment check(Equipment equipment);

    int count(Equipment equipment);

    List<Equipment> equipmentList(Equipment equipment);
}