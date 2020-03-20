package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.entity.ExcelErrorData;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.GsonUtil;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.product.domain.ProductPlan;
import com.ruoyi.product.service.IProductPlanService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenm
 * @create 2019-08-06 23:46
 */
@Controller
@RequestMapping("/product/plan")
public class ProductPlanController extends BaseController {

    @Autowired
    private IProductPlanService productPlanService;

    private String prefix = "product/plan";
    /**
     * 首页
     */
    @GetMapping()
    public String produce() {
        return prefix + "/index";
    }

    /**
     * 打开新增产品页面
     */
    @GetMapping("/addPlan")
    public String addPlan()
    {
        return "product/produce/addPlan";
    }

    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存产品
     * @return
     */
    @Log(title = "新增产品计划单", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:plan:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated ProductPlan plan)
    {
        plan.setDataState(1);
        int i = productPlanService.insertProductPlan(plan);
        if(i!=0){
            return AjaxResult.success(plan);
        }else{
            return AjaxResult.error("新增失败");
        }
    }


    /**
     * 修改产线类型
     */
    @GetMapping("/edit/{lineId}")
    public String edit(@PathVariable("lineId") Long planId, ModelMap mmap)
    {
        mmap.put("productPlan",productPlanService.selectByPlanId(planId));

        return prefix + "/edit";
    }

    /**
     * 修改保存产线类型
     */
    @Log(title = "修改产品类型", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:plan:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ProductPlan plan)
    {
        return toAjax(productPlanService.updateProductPlan(plan));
    }


    /**
     * 校验编码
     */
    @PostMapping("/checkProductCodeUnique")
    @ResponseBody
    public String checkProductCodeUnique(ProductPlan plan)
    {
        String result =  productPlanService.checkProductCodeUnique(plan);
        return result;
    }

    /**
     * 获取产品输入补全
     */
    @GetMapping("/productSug")
    @ResponseBody
    public AjaxResult productSug(ProductPlan plan)
    {
        AjaxResult ajax = new AjaxResult();
        ajax.put("code", 200);
        ajax.put("value", productPlanService.selectProductPlanList(plan));
        return ajax;
    }

    /**
     * 数据表格导入
     *
     * @return
     */
    @Log(title = "计划单导入", businessType = BusinessType.IMPORT)
    @RequiresPermissions("product:plan:import")
    @RequestMapping("json/ExcelImport")
    @ResponseBody
    public Object jsonExcelImport(@RequestParam("file") MultipartFile file, Boolean way, HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        Message msg = new Message();
        List<ExcelErrorData> errorDatas = new ArrayList<>();
        response.setContentType("text/html;charset=UTF-8");
        Workbook wb = ExcelUtil.getWorkbook(file.getInputStream(), ExcelUtil.isExcel2007(file.getOriginalFilename()));
        if (wb == null) {
            errorDatas.add(ExcelErrorData.getExcelErrorData(0, 0, "文件解析错误"));
        } else {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null) {
                msg.setCode(2);
                msg.setMsg("未找到需要导入的表格");
                errorDatas.add(ExcelErrorData.getExcelErrorData(0, 0, "未找到需要导入的表格"));
            } else {
                int rowCount = sheet.getLastRowNum();
                if (rowCount < 1) {
                    msg.setCode(2);
                    msg.setMsg("未找到需要导入的数据，请确认表格是否保存！！！");
                    errorDatas.add(ExcelErrorData.getExcelErrorData(0, 0, "未找到需要导入的数据，请确认表格是否保存！！！"));
                } else {
                    List<ProductPlan> addProductPlans = new ArrayList<>();
                    for (int i = 1; i <= rowCount; i++) {
                        boolean needAdd = true;
                        String planName = StringUtils.trimToEmpty(getCellValue(sheet, i, 0, false)).replaceAll("\\s*", "");
                        ProductPlan plan = new ProductPlan();
                        int m = i + 1;
                        if (StringUtils.isBlank(planName)) {
                            needAdd = false;
                            errorDatas.add(ExcelErrorData.getExcelErrorData(m, 1, "产品名称不能为空"));
                        }else if(null != productPlanService.selectByName(planName)){
                            needAdd = false;
                            errorDatas.add(ExcelErrorData.getExcelErrorData(m, 1, "产品名称数据库已存在"));
                        }else{
                            for (int j = 1; j < i; j++) {
                                String oldLineName = StringUtils.trimToEmpty(getCellValue(sheet, j, 0, false)).replaceAll("\\s*", "");
                                if (oldLineName.equals(planName)) {
                                    needAdd = false;
                                    errorDatas.add( ExcelErrorData.getExcelErrorData(j+1, 1, "与表格中第" + m + "行存在相同产品名称"));
                                }
                            }
                        }
                        plan.setPlanName(planName);
                        if (needAdd) {
                            addProductPlans.add(plan);
                        } else {
                            msg.setCode(2);
                            msg.setMsg("导入数据中存在异常信息");
                        }

                    }

                    if (msg.getCode() == 2 && !way) {
                        msg.setMsg(msg.getMsg() + ",中止导入");
                    } else {
                        if (addProductPlans.size() > 0) {
                            try {
                                productPlanService.saveProductPlans(addProductPlans);
                                msg.setMsg(StringUtils.isBlank(msg.getMsg()) ? "导入成功" : msg.getMsg());
                                if (msg.getMsg().equals("导入成功")) {
                                    msg.setCode(1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                msg.setMsg("导入失败");
                            }
                        }
                    }
                    msg.setData(GsonUtil.toJson(errorDatas));
                }
            }
        }
        msg.setData(GsonUtil.toJson(errorDatas));
//        response.getWriter().println(GsonUtil.toJson(msg));
        return GsonUtil.toJson(msg);
    }

    private String getCellValue(Sheet sheet, int r, int c, boolean date) {
        Cell cell = sheet.getRow(r).getCell(c);
        String data = "";
        try {
            data = cell.getStringCellValue();
        } catch (Exception e) {
            e.printStackTrace();
            data = null;
        }
        return data;
    }

    /**
     * 导出
     */
    @Log(title = "计划单导出", businessType = BusinessType.EXPORT)
    @RequiresPermissions("product:plan:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProductPlan plan)
    {
        List<ProductPlan> list = productPlanService.selectProductPlanList(plan);
        ExcelUtil<ProductPlan> util = new ExcelUtil<ProductPlan>(ProductPlan.class);
        return util.exportExcel(list, "产品数据");
    }


    /**
     * 查询
     */

    @PostMapping("/list")
    @RequiresPermissions("product:plan:view")
    @ResponseBody
    public TableDataInfo list(ProductPlan plan) {
        startPage();
        List<ProductPlan> list = productPlanService.selectProductPlanList(plan);
        return getDataTable(list);
    }

    /**
     * @author: louk
     * @Description: :删除产品
     * @date: 2019/9/21 11:09
     *
     */

    @Log(title = "删除计划单", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:plan:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(productPlanService.deleteProductPlanByIds(ids));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }


}
