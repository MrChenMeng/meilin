package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.domain.CheckStandard;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.domain.Standard;
import com.ruoyi.product.service.ICheckStandardService;
import com.ruoyi.product.service.IProductLineService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.IStandardService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("product/detected")
public class DetectedController extends BaseController {


	@Autowired
	private IProductLineService productLineService;

	@Autowired
	private IStandardService standardService;

	@Autowired
	private ICheckStandardService checkStandardService;

	@Autowired
	private IProductService productService;

	private String prefix = "product/detected";

	@GetMapping()
	@RequiresPermissions("product:detected:view")
	public String detected(ModelMap map){
		List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
		List<CheckStandard> checkStandardList = checkStandardService.selectCheckList(new CheckStandard());
		map.put("productLines",productLines);
		map.put("checkStandardList",checkStandardList);
		List<Ztree> ztrees = new ArrayList<>();
		for(CheckStandard _check: checkStandardList){
			Ztree _ztree = new Ztree();
			_ztree.setOpen(true);
			_ztree.setpId(_check.getParentId());
			_ztree.setName(_check.getCheckName());
			_ztree.setTitle(_check.getCheckName());
			_ztree.setId(_check.getId());
			ztrees.add(_ztree);
		}
		map.put("ztrees",ztrees);
		return prefix + "/menu";
	}
	//界面中产品的查询

	@RequiresPermissions("product:detected:list")
	@PostMapping("/list/{productId}/{checkId}")
	@ResponseBody
	public TableDataInfo list(@PathVariable("productId") Long productId , @PathVariable("checkId") Long checkId, HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		session.removeAttribute("standardlist");
		Standard standard = new Standard();
		standard.setProductId(productId);
		standard.setCheckId(checkId);
		List<Standard> list = new ArrayList<>();
		List<Standard> hasProductIdList = standardService.select(standard);
		if(hasProductIdList.size()>0){
			list = hasProductIdList;
		}else{
			if(productId != 0 && checkId != 0){
				standard.setProductId(null);
				List<Standard> nullProductIdList = standardService.select(standard);
				if(nullProductIdList.size()>0){
					list = nullProductIdList;
				}else{
					CheckStandard check = checkStandardService.selectCheckStandardById(checkId);
					Long  parentId = check.getParentId();
					if(parentId == null){
						standard.setCheckId(0L);
					}else {
						standard.setCheckId(parentId);
					}
					list = standardService.select(standard);
					if(list.isEmpty()){

						check = checkStandardService.selectCheckStandardById(parentId);
						if(null == check){
                            return getDataTable(list);
                        }
						parentId = check.getParentId();
						if(parentId == null){
							standard.setCheckId(0L);
						}else {
							standard.setCheckId(parentId);
						}
                        list = standardService.select(standard);
					}
				}
				session.setAttribute("standardlist",list);
			}
		}
		return getDataTable(list);

	}



	@PostMapping("/check")
	@Log(title = "检测通过", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:detected:check")
	@ResponseBody
	public Message checkProduct(Long productId,Long checkId){
		Message msg = new Message();
		return standardService.editCheckState(productId,checkId,"2");
	}


	@PostMapping("/checkDetected")
	@Log(title = "检测完成", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:detected:checkDetected")
	@ResponseBody
	public Message checkDetected(@RequestBody List<Standard> detectedData){
		return standardService.checkDetected(detectedData);
	}

	@RequiresPermissions("product:detected:return")
	@PostMapping("/return")
	@ResponseBody
	public AjaxResult returnSave(@Validated Product product)
	{
		try {
			product.setUpdateBy(ShiroUtils.getLoginName());
			return toAjax(productService.updateProduct(product));
		}catch (Exception e){
			e.printStackTrace();
			return error(e.getMessage());
		}
	}

	@PostMapping("/template")
	@Log(title = "设为模板", businessType = BusinessType.UPDATE)
	@RequiresPermissions("product:detected:template")
	@ResponseBody
	public Message template(@RequestBody List<Standard> detectedData){
		return standardService.template(detectedData);
	}

	/**
	 * 导出
//	 */
	@RequiresPermissions("product:detected:export")
	@PostMapping("/export")
	@ResponseBody
	public Message export(String idStr)
	{
		Message msg = new Message();

		String[] idArr = idStr.split(",");
		for(int i = 0;i < idArr.length; i++){
			Product product = productService.selectProductById(Long.valueOf(idArr[i]));
			if(product.getCheckId() == 0){
				msg.setCode(Message.getError());
				msg.setMsg("此单号尚未进行行业检测");
				return msg;
			}
		}
		msg.setCode(Message.getSuccess());
		msg.setMsg("查询成功");
		return msg;
	}

	/**
	 * 导出
	 */
	@Log(title = "检测单导出", businessType = BusinessType.EXPORT)
	@GetMapping("/download")
	public void download(String idStr,  HttpServletResponse response)throws Exception {
		List<Standard> list = standardService.selectByProductIds(idStr);
	    String[] idArr = idStr.split(",");
		String[][] content = new String[idArr.length][list.size()+3];
		//excel文件名
		String fileName = "日检报告单"+System.currentTimeMillis()+".xls";
		//sheet名
		String sheetName = "杭州美临塑业有限公司日检报告单";
		//excel标题
		String[] title = new String[list.size()+3];
		title[0] = "检测日期";
		title[1] = "型号";
		title[2] = "班次(批号)";
		for(int i =0;i < list.size();i++ ){
			title[3+i] = list.get(i).getStandardName();
		}

	    for(int  i =0 ;i < idArr.length ; i++){
            Product product = productService.selectProductById(Long.valueOf(idArr[i]));
            Standard standard = new Standard();
            standard.setProductId(Long.valueOf(idArr[i]));
            standard.setCheckId(product.getCheckId());
            List<Standard> standardList = standardService.select(standard);
            if(!standardList.isEmpty()){
				content[i][0] = product.getCheckTime();
				content[i][1] = product.getProductName();
				content[i][2] = product.getProductNo();
				for(int j = 0;j < list.size();j ++){
					for (int z = 0 ;z<standardList.size();z++){
						if(list.get(j).getStandardName().equals(standardList.get(z).getStandardName())){
							content[i][3+j] = standardList.get(z).getDetectedNumber();
							break;
						}else{
							content[i][3+j] = "——";
						}
					}
				}
            }
        }
		//创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

		//响应到客户端
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(),"ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
