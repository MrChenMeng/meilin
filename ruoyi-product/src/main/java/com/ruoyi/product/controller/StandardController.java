package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.domain.Standard;
import com.ruoyi.product.service.ICheckStandardService;
import com.ruoyi.product.service.IStandardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/product/standard")
public class StandardController extends BaseController {
	@Autowired
	private IStandardService standardService;

	@Autowired
	private ICheckStandardService checkStandardService;

	//请求地址前部
	private String prefix = "product/standard";

	//物料页面
	@RequiresPermissions("product:standard:view")
	@GetMapping()
	public String produce(){
		return prefix + "/index";
	}

	//查询所有物料
	@PostMapping("/list")
	@RequiresPermissions("product:standard:view")
	@ResponseBody
	public TableDataInfo list(Standard standard) {
		startPage();
		List<Standard> list = standardService.select(standard);
		return getDataTable(list);
	}

	@GetMapping("/add/{id}")
	public String addPage(@PathVariable("id")Long id,ModelMap modelMap){
		modelMap.put("check",checkStandardService.selectCheckStandardById(id));
		return prefix + "/add";
	}

	@PostMapping("/add")
	@Log(title = "检测标准新增", businessType = BusinessType.INSERT)
	@RequiresPermissions("product:standard:add")
	@ResponseBody
	public AjaxResult addSubmit(Standard standard){
		return toAjax(standardService.insert(standard));
	}

	@GetMapping("/edit/{id}")
	public String editPage(@PathVariable("id") Long id, ModelMap model){
		model.put("standard", standardService.selectById(id));
		return prefix + "/edit";

	}

	@PostMapping("/edit")
	@Log(title = "检测标准修改", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:standard:edit")
	@ResponseBody
	public AjaxResult editSave(Standard standard){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		standard.setUpdateBy(ShiroUtils.getLoginName());
		return toAjax(standardService.update(standard,request));
	}

	@PostMapping("/remove")
	@Log(title = "检测标准删除", businessType = BusinessType.DELETE)
	@RequiresPermissions("product:standard:remove")
	@ResponseBody
	public AjaxResult removeSubmit(@Validated  String ids){
		try{
			int i = standardService.removeByIds(ids);
			return toAjax(i);
		}
		catch (Exception e){
			e.printStackTrace();
			return error(e.getMessage());
		}
	}

}
