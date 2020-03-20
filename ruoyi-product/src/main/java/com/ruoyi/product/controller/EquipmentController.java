package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.constant.SessionTokenConstants;
import com.ruoyi.product.domain.Equipment;
import com.ruoyi.product.service.IEquipmentService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/product/equipment")
public class EquipmentController extends BaseController {

	@Autowired
	private IEquipmentService equipmentService;

	private String prefix = "product/equipment";

	/**
	 * 主页面跳转
	 *
	 * @param * @param
	 * @return java.lang.String
	 * @author TYw
	 * @date 2019/8/30 16:13
	 */
	@GetMapping()
	@RequiresPermissions("product:equipment:view")
	public String page() {
		return prefix + "/index";
	}

	/**
	 * 查询集合
	 *
	 * @param * @param equipment
	 * @return com.ruoyi.common.core.page.TableDataInfo
	 * @author TYw
	 * @date 2019/8/30 16:12
	 */
	@RequiresPermissions("product:equipment:list")
	@PostMapping("/list")
	@ResponseBody
	public TableDataInfo list(Equipment equipment) {
		startPage();
		List<Equipment> list = equipmentService.selectAll(equipment);
		return getDataTable(list);
	}

	/*新增设备
	 *
	 * @author TYw
	 * @date 2019/8/30 16:19
	 * @param  * @param equipment
	 * @return com.ruoyi.common.core.domain.AjaxResult
	 */
	@Log(title = "设备新增", businessType = BusinessType.INSERT)
	@RequiresPermissions("product:equipment:add")
	@PostMapping("/add")
	@ResponseBody
	public AjaxResult addSave(Equipment equipment) {
		String val = equipmentService.check(equipment);
		if (val.equals(UserConstants.DICT_TYPE_NOT_UNIQUE)) {
			return AjaxResult.error("ip地址重复");
		}
		return toAjax(equipmentService.insertEquipment(equipment));
	}

	/**
	 * 修改设备
	 *
	 * @param * @param equipment
	 * @return com.ruoyi.common.core.domain.AjaxResult
	 * @author TYw
	 * @date 2019/8/30 16:17
	 */
	@Log(title = "修改设备", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:equipment:edit")
	@PostMapping("/edit")
	@ResponseBody
	public AjaxResult editSave(Equipment equipment) {
		return toAjax(equipmentService.updateEquipment(equipment));
	}

	/**
	 * 删除设备
	 *
	 * @param * @param ids
	 * @return com.ruoyi.common.core.domain.AjaxResult
	 * @author TYw
	 * @date 2019/8/30 16:17
	 */
	@Log(title = "删除设备", businessType = BusinessType.DELETE)
	@RequiresPermissions("product:produce:remove")
	@PostMapping("/remove")
	@ResponseBody
	public AjaxResult remove(@Validated String ids) {
		return toAjax(equipmentService.deleteEquipment(ids));
	}

	/*新增页面跳转
	 *
	 * @author TYw
	 * @date 2019/8/30 16:22
	 * @param  * @param
	 * @return java.lang.String
	 */
	@GetMapping("/add")
	public String toAddPage() {
		return prefix + "/add";
	}

	/*修改页面跳转
	 *  
	 * @author TYw
	 * @date 2019/8/30 16:25
	 * @param  * @param id
	 * @param modelMap
	 * @return java.lang.String
	 */
	@GetMapping("/edit/{id}")
	public String toUpdatePage(@PathVariable Long id, ModelMap modelMap) {
		Equipment equipment = equipmentService.selectById(id);
		modelMap.put("equipment", equipment);
		return prefix + "/edit";
	}

	@PostMapping("/check")
	@ResponseBody
	public String checkLineCode(Equipment equipment) {
		return equipmentService.check(equipment);
	}

	@RequiresPermissions("product:equipment:mobileIndex")
	@PostMapping("/mobileIndex")
	@ResponseBody
	public Object mobileIndex(Equipment equipment, HttpServletRequest request){
		String token = request.getHeader(SessionTokenConstants.sys_token);
		String sessionId = ShiroUtils.getSessionId();
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++"+sessionId);
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
			msg.setCount(equipmentService.count(equipment));
			msg.setCode(Message.getSuccess());
			msg.setMsg("操作成功");
			msg.setData(equipmentService.equipmentList(equipment));
		}catch (Exception e){
			e.printStackTrace();
			msg.setCode(Message.getError());
			msg.setMsg("系统出错，请联系管理员");
			return msg;
		}
		return msg;
	}
}
