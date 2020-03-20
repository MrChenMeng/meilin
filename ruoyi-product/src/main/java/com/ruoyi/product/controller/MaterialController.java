package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.constant.SessionTokenConstants;
import com.ruoyi.product.domain.Material;
import com.ruoyi.product.domain.MaterialList;
import com.ruoyi.product.domain.Procedure;
import com.ruoyi.product.service.IMaterialService;
import com.ruoyi.product.service.IProcedureService;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/product/material")
public class MaterialController extends BaseController {

	@Autowired
	private IMaterialService materialService;

	@Autowired
	private ISysDictDataService dictDataService;

	@Autowired
	private IProcedureService procedureService;

	//请求地址前部
	private String prefix = "product/material";

	//物料页面
	@RequiresPermissions("product:material:view")
	@GetMapping()
	public String produce(){
		return prefix + "/index";
	}

	//查询所有物料
	@PostMapping("/list")
	@RequiresPermissions("product:material:list")
	@ResponseBody
	public TableDataInfo list(Material material) {
		startPage();
		List<Material> list = materialService.selectMaterial(material);
		return getDataTable(list);
	}

	//查询所有物料
	@PostMapping("/materials")
	@ResponseBody
	public TableDataInfo materials(Material material) {
		List<Material> list = materialService.selectMaterial(material);
		Material newMaterial =  new Material();
		if(list.size()>0){
			newMaterial.setRemark(list.get(0).getRemark());
		}
		list.add(newMaterial);
		return getDataTable(list);
	}

	@GetMapping("/add/{procedureId}")
	public String addPage(@PathVariable("procedureId") Long procedureId,ModelMap modelMap){
		Procedure procedure = procedureService.selectProcedureById(procedureId);
		modelMap.put("procedure",procedure);
		return prefix + "/add";
	}

	@PostMapping("/add")
	@Log(title = "新增物料配方", businessType = BusinessType.INSERT)
	@RequiresPermissions("product:material:add")
	@ResponseBody
	public AjaxResult addSubmit(Material material){
		return toAjax(materialService.insertMaterial(material));
	}

	@GetMapping("/edit/{mid}")
	public String editPage(@PathVariable("mid") Long mid, ModelMap model){
		model.put("material", materialService.selectMaterialById(mid));
		return prefix + "/edit";

	}

	@PostMapping("/edit")
	@Log(title = "修改物料配方", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:material:edit")
	@ResponseBody
	public AjaxResult editSave(@Validated Material material){
		material.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(materialService.updateMaterial(material));
	}

	@PostMapping("/remove")
	@Log(title = "删除物料配方", businessType = BusinessType.DELETE)
	@RequiresPermissions("product:material:remove")
	@ResponseBody
	public AjaxResult removeSubmit(@Validated  String ids){
		try{
			int i = materialService.removeMaterialByIds(ids);
			return toAjax(i);
		}
		catch (Exception e){
			e.printStackTrace();
			return error(e.getMessage());
		}
	}

	@GetMapping("/editMater/{mid}")
	public String editMaterPage(@PathVariable("mid") Long mid, ModelMap model){
		model.put("material", materialService.selectMaterialById(mid));
		return prefix + "/editMater";

	}

	@GetMapping("/treeData")
	@ResponseBody
	public List<Ztree> treeData()
	{
		List<Ztree> ztreeList = new ArrayList<>();
		List<SysDictData> list = new ArrayList<>();
		SysDictData dictData = new SysDictData();
		dictData.setDictValue("0");
		dictData.setDictLabel("所有类型");
		list.add(dictData);
		list.addAll(dictDataService.selectDictDataByType("product_material_type"));
		for (SysDictData sysDictData : list) {
			Ztree ztree = new Ztree();
			ztree.setId(sysDictData.getDictValue()+"D");
			if(sysDictData.getDictValue().equals("0")){
				ztree.setpId(null);
			}else{
				ztree.setpId("0D");
			}
			ztree.setOpen(true);
			ztree.setName(sysDictData.getDictLabel());
			ztree.setTitle(sysDictData.getDictLabel());
			ztreeList.add(ztree);
		}
		Procedure procedure = new Procedure();
		List<Procedure> procedures = procedureService.selectProcedure(procedure);
		for(Procedure _p : procedures){
			Ztree ztree = new Ztree();
			ztree.setId(_p.getId()+"P");
			ztree.setpId(_p.getType()+"D");
			ztree.setOpen(true);
			ztree.setName(_p.getName());
			ztree.setTitle(_p.getName());
			ztreeList.add(ztree);
		}
		return ztreeList;
	}

	@PostMapping("/insertMaterialList")
	@ResponseBody
	public Object insertMaterialList(@RequestBody List<Material> materialData) throws ParseException {
		Message message = new Message();
		return materialService.insertMaterialList(materialData,message);
	}

	@RequiresPermissions("product:material:mobileMaterial")
	@RequestMapping("/mobileMaterial")
	@ResponseBody
	public Object mobileMaterial(Material material,HttpServletRequest request){
		String token = request.getHeader(SessionTokenConstants.sys_token);
		String sessionId = ShiroUtils.getSessionId();
		Message msg = new Message();
		if(StringUtils.isEmpty(sessionId)){
			msg.setCode(Message.getLogout());
			msg.setMsg("您已掉线，请重新登录");
			return msg;
		}
		if(!token.equals(sessionId)){
			msg.setCode(Message.getLogout());
			msg.setMsg("您已掉线，请重新登录");
			return msg;
		}
		try{
			msg.setData(materialService.mobileMaterial(material));
			msg.setCode(Message.getSuccess());
			msg.setMsg("操作成功");
		}catch (Exception e){
			msg.setCode(Message.getError());
			e.printStackTrace();
			msg.setMsg("系统出错，请联系管理员");
			return msg;
		}
		return msg;
	}

	@Log(title = "app保存物料", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:material:saveMaterial")
	@RequestMapping("/saveMaterial")
	@ResponseBody
	public Object saveMaterial (@RequestBody MaterialList materialList,HttpServletRequest request){
		String token = request.getHeader(SessionTokenConstants.sys_token);
		String sessionId = ShiroUtils.getSessionId();
		Message msg = new Message();
		if(StringUtils.isEmpty(sessionId)){
			msg.setCode(Message.getLogout());
			msg.setMsg("您已掉线，请重新登录");
			return msg;
		}
		if(!token.equals(sessionId)){
			msg.setCode(Message.getLogout());
			msg.setMsg("您已掉线，请重新登录");
			return msg;
		}
		try{
			msg = materialService.saveMaterial(materialList);
		}catch (Exception e){
			msg.setCode(Message.getError());
			e.printStackTrace();
			msg.setMsg("系统出错，请联系管理员");
			return msg;
		}
		return msg;
	}

	/**
	 * @author: louk
	 * @Description: :检查物料名称是否唯一
	 * @date: 2019/9/20 14:38
	 *
	 */
	@RequestMapping("/checkName")
	@ResponseBody
	public String checkName(Material material){

		return materialService.checkName(material);
	}


	//颜色标识
	@PostMapping("/updatesign")
	@ResponseBody
	public Object updatesign(Material material) {
		try {
			return materialService.updatesign(material);
		} catch (Exception e) {
			e.printStackTrace();
			return error(e.getMessage());
		}
	}



}
