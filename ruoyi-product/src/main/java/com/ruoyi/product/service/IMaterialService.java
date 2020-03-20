package com.ruoyi.product.service;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.product.domain.Material;
import com.ruoyi.product.domain.MaterialList;

import java.text.ParseException;
import java.util.List;

public interface IMaterialService {

	int removeMaterialByIds(String ids);

	/*
	int removeMixById(Long id,Long productId,String type);
	*/

	int insertMaterial(Material material);

	List<Material> selectMaterial(Material material);

	Material selectMaterialById(Long mid);

	int updateMaterial(Material material);

	/*
	int updateMaterial(String type,List<Material> material, HttpServletRequest request);
	*/
	/*
    int count(Material material);
	*/

    /*
	List<Material> materialList(Material material);
	*/

    List<Material> mobileMaterial(Material material);

	Message saveMaterial(MaterialList materialList);

	/**
	 * @author: louk
	 * @Description: :检查物料名称是否唯一
	 * @date: 2019/9/20 14:38
	 *
	 */
	String checkName(Material material);

	Object insertMaterialList(List<Material> materialData, Message message) throws ParseException;

	//颜色标识
	Object updatesign(Material material);



}
