package com.ruoyi.product.service;

import com.ruoyi.product.domain.Equipment;

import java.util.List;

public interface IEquipmentService {

	/**
	 * 新增设备
	 *
	 * @author TYw
	 * @date 2019/8/30 15:48
	 * @param  * @param equipment
	 * @return int
	 */
	int insertEquipment(Equipment equipment);

	/**
	 * 查询所有
	 *
	 * @author TYw
	 * @date 2019/8/30 15:49
	 * @param  * @param equipment
	 * @return java.util.List<com.ruoyi.product.domain.Equipment>
	 */
	List<Equipment> selectAll(Equipment equipment);

	/**
	 * 根据id查询
	 *
	 * @author TYw
	 * @date 2019/8/30 15:54
	 * @param  * @param id
	 * @return com.ruoyi.product.domain.Equipment
	 */
	Equipment selectById(Long id);

	/**
	 * 修改设备信息
	 *
	 * @author TYw
	 * @date 2019/8/30 15:55
	 * @param  * @param equipment
	 * @return int
	 */
	int updateEquipment(Equipment equipment);

	/**
	 * 删除设备信息
	 *
	 * @author TYw
	 * @date 2019/8/30 15:56
	 * @param  * @param id
	 * @return int
	 */
	int deleteEquipment(String ids);
	
	/*检查ip地址是否重复
	 *  
	 * @author TYw
	 * @date 2019/8/30 17:10
	 * @param  * @param name
	 * @return com.ruoyi.product.domain.Equipment
	 */
	String check(Equipment equipment);

    int count(Equipment equipment);

	List<Equipment> equipmentList(Equipment equipment);
}
