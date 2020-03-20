package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.product.domain.Procedure;
import com.ruoyi.product.service.IProcedureService;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenm
 * @create 2019-09-10 15:39
 */
@Controller
@RequestMapping("/product/procedure")
public class ProcedureController extends BaseController {

    @Autowired
    private IProcedureService procedureService;

    @Autowired
    private ISysDictDataService dictDataService;

    //请求地址前部
    private String prefix = "product/procedure";

    //物料页面
    @RequiresPermissions("product:procedure:view")
    @GetMapping()
    public String produce() {
        return prefix + "/index";
    }

    //查询所有物料
    @PostMapping("/list")
    @RequiresPermissions("product:procedure:list")
    @ResponseBody
    public TableDataInfo list(Procedure procedure) {
        startPage();
        List<Procedure> list = procedureService.selectProcedure(procedure);
        return getDataTable(list);
    }

    @GetMapping("/add/{type}")
    public String addPage(@PathVariable("type") String type, ModelMap modelMap){
        modelMap.put("type",type);
        return prefix + "/add";
    }

    @PostMapping("/add")
    @Log(title = "新增物料类型", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:procedure:add")
    @ResponseBody
    public AjaxResult addSubmit(Procedure procedure){
        return toAjax(procedureService.insertProcedure(procedure));
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable("id") Long id, ModelMap model){
        model.put("procedure", procedureService.selectProcedureById(id));
        return prefix + "/edit";

    }


    @PostMapping("/edit")
    @Log(title = "修改物料类型", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:procedure:edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Procedure procedure){
        return toAjax(procedureService.updateProcedure(procedure));
    }

    @PostMapping("/remove")
    @Log(title = "删除物料类型", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:procedure:remove")
    @ResponseBody
    public AjaxResult removeSubmit(@Validated  String ids){
        try{
            int i = procedureService.removeProcedureByIds(ids);
            return toAjax(i);
        }
        catch (Exception e){
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztreeList = new ArrayList<>();
        List<SysDictData> list = new ArrayList<>();
        SysDictData dictData = new SysDictData();
        dictData.setDictValue("0");
        dictData.setDictLabel("所有类型");
        list.add(dictData);
        list.addAll(dictDataService.selectDictDataByType("product_material_type"));
        for (SysDictData sysDictData : list) {
            Ztree ztree = new Ztree();
            ztree.setId(Long.valueOf(sysDictData.getDictValue()));
            if(sysDictData.getDictValue().equals("0")){
                ztree.setpId(null);
            }else{
                ztree.setpId("0");
            }
            ztree.setOpen(true);
            ztree.setName(sysDictData.getDictLabel());
            ztree.setTitle(sysDictData.getDictLabel());
            ztreeList.add(ztree);
        }
        return ztreeList;
    }
}
