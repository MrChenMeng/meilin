package com.ruoyi.print.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.print.domain.PrintTemplate;
import com.ruoyi.print.service.IPrintTemplateService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-16 16:09
 */
@Controller
@RequestMapping("/print/manage")
public class PrintTemplateController extends BaseController {

    private String prefix = "print/manage";

    @Autowired
    private IPrintTemplateService printTemplateService;


    @RequiresPermissions("print:manage:view")
    @GetMapping()
    public String index() {
        return prefix + "/index";
    }


    @RequiresPermissions("print:manage:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(PrintTemplate printTemplate) {
        startPage();
        List<PrintTemplate> list = printTemplateService.selectPrintTemplateList(printTemplate);
        return getDataTable(list);
    }

    /**
     * 新增文件上传
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    @PostMapping("/add")
    @RequiresPermissions("print:manage:add")
    @ResponseBody
    public AjaxResult addSubmit(PrintTemplate printTemplate){
        return toAjax(printTemplateService.insertPrintTemplate(printTemplate));
    }

    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap map)
    {
        PrintTemplate printTemplate = printTemplateService.selectPrintTemplateById(id);
        map.put("printTemplate", printTemplate);
        return prefix + "/edit";
    }

    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(PrintTemplate printTemplate)
    {
        return toAjax(printTemplateService.updatePrintTemplate(printTemplate));
    }

    @PostMapping("/edit")
    @RequiresPermissions("print:manage:edit")
    @ResponseBody
    public AjaxResult editSave(PrintTemplate printTemplate){
        return toAjax(printTemplateService.updatePrintTemplate(printTemplate));
    }

    @PostMapping("/remove")
    @RequiresPermissions("product:standard:remove")
    @ResponseBody
    public AjaxResult removeSubmit(@Validated String ids){
        return toAjax(printTemplateService.deletePrintTemplateByIds(ids));
    }

    /**
     * 文件预览
     */
    @GetMapping("/preview/{id}")
    public String preview(@PathVariable("id") long id, ModelMap map) {
        PrintTemplate printTemplate = printTemplateService.selectPrintTemplateById(id);
        map.put("printTemplate", printTemplate);
        return prefix + "/preview";
    }

}
