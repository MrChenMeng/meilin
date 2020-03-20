package com.ruoyi.product.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.product.domain.ProductHistory;
import com.ruoyi.product.service.IProductHistoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product/history")
public class ProductHistoryController extends BaseController {

	@Autowired
	private IProductHistoryService productHistoryService;

	@GetMapping("/list/{productId}")
	@RequiresPermissions("product:history:view")
	public String historyPage(@PathVariable("productId") Long productId, ModelMap modelMap){
		modelMap.put("productId",productId);
		return "product/produce/history";
	}

	@PostMapping("/list/{productId}")
	@ResponseBody
	public TableDataInfo list(@PathVariable("productId") Long productId) {
		startPage();
		List<ProductHistory> list = productHistoryService.selectByProductId(productId);
		return getDataTable(list);
	}
}
