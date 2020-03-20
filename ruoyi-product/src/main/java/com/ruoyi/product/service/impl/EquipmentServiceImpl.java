package com.ruoyi.product.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.product.domain.Equipment;
import com.ruoyi.product.mapper.EquipmentMapper;
import com.ruoyi.product.service.IEquipmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EquipmentServiceImpl implements IEquipmentService {

	/*
	 * 注入依赖
	 * */
	@Resource
	private EquipmentMapper equipmentMapper;

	/**
	 * 新增设备
	 *
	 * @param equipment
	 * @return int
	 * @author TYw
	 * @date 2019/8/30 15:48
	 */
	@Override
	public int insertEquipment(Equipment equipment) {
		return equipmentMapper.insertSelective(equipment);
	}

	/**
	 * 查询所有
	 *
	 * @param equipment
	 * @return java.util.List<com.ruoyi.product.domain.Equipment>
	 * @author TYw
	 * @date 2019/8/30 15:49
	 */
	@Override
	public List<Equipment> selectAll(Equipment equipment) {
		return equipmentMapper.selectAll(equipment);
	}

	/**
	 * 根据id查询
	 *
	 * @param id
	 * @return com.ruoyi.product.domain.Equipment
	 * @author TYw
	 * @date 2019/8/30 15:54
	 */
	@Override
	public Equipment selectById(Long id) {
		return equipmentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 修改设备信息
	 *
	 * @param equipment
	 * @return int
	 * @author TYw
	 * @date 2019/8/30 15:55
	 */
	@Override
	public int updateEquipment(Equipment equipment) {
		return equipmentMapper.updateByPrimaryKeySelective(equipment);
	}

	/**
	 * 删除设备信息
	 *
	 * @param id
	 * @return int
	 * @author TYw
	 * @date 2019/8/30 15:56
	 */
	@Override
	public int deleteEquipment(String ids) {
		return equipmentMapper.deleteByPrimaryKey(ids);
	}

	/**
	 * 检查ip地址是否重复
	 *
	 * @param * @param name
	 * @return com.ruoyi.product.domain.Equipment
	 * @author TYw
	 * @date 2019/8/30 17:09
	 */
	@Override
	public String check(Equipment equipment) {
		Equipment equipment2 = equipmentMapper.check(equipment);
		if (equipment2 != null) {
			return UserConstants.DICT_TYPE_NOT_UNIQUE;
		}
		return UserConstants.DICT_TYPE_UNIQUE;
	}

	@Override
	public int count(Equipment equipment) {
		return equipmentMapper.count(equipment);
	}

	@Override
	public List<Equipment> equipmentList(Equipment equipment) {
		return equipmentMapper.equipmentList(equipment);
	}
}
