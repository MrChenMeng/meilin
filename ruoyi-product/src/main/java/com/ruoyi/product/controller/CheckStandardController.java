package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.constant.CheckStandardConstants;
import com.ruoyi.product.domain.CheckStandard;
import com.ruoyi.product.entity.Search;
import com.ruoyi.product.service.ICheckStandardService;
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
 * @create 2019-08-14 14:02
 */
@Controller
@RequestMapping("/product/check")
public class CheckStandardController extends BaseController {

    private String prefix = "product/check";

    @Autowired
    private ICheckStandardService checkStandardService;

    @RequiresPermissions("product:check:view")
    @GetMapping()
    public String index()
    {
        return prefix + "/index";
    }

    @RequiresPermissions("product:check:list")
    @PostMapping("/list")
    @ResponseBody
    public List<CheckStandard> list(CheckStandard checkStandard)
    {
        List<CheckStandard> checkStandardList = checkStandardService.selectCheckList(checkStandard);
        return checkStandardList;
    }

    @GetMapping("/add/{id}")
    public String add( @PathVariable("id") Long id,ModelMap map)
    {
        map.put("check",checkStandardService.selectCheckStandardById(id));
        return prefix + "/add";
    }

    /**
     * 新增保存部门
     */
    @Log(title = "检测部门", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:check:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(@Validated CheckStandard checkStandard)
    {
        if (CheckStandardConstants.CHECK_NAME_NOT_UNIQUE.equals(checkStandardService.checkCheckNameUnique(checkStandard)))
        {
            return error("新增检测'" + checkStandard.getCheckName() + "'失败，名称已存在");
        }
        checkStandard.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(checkStandardService.insertCheckStandard(checkStandard));
    }

    /**
     * 选择行业树
     */
    @GetMapping("/selectCheckTree/{id}")
    public String selectCheckTree(@PathVariable("id") Long id, ModelMap map)
    {
        map.put("check",checkStandardService.selectCheckStandardById(id));
        return prefix + "/tree";
    }

  
    /**
     * 修改
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap map)
    {
        CheckStandard check = checkStandardService.selectCheckStandardById(id);
        map.put("check", check);
        return prefix + "/edit";
    }

    /**
     * 保存
     */
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:check:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated CheckStandard checkStandard)
    {
        if (CheckStandardConstants.CHECK_NAME_NOT_UNIQUE.equals(checkStandardService.checkCheckNameUnique(checkStandard)))
        {
            return error("修改检测列表'" + checkStandard.getCheckName() + "'失败，名称已存在");
        }
        else if (null != checkStandard.getParentId() && checkStandard.getParentId().equals(checkStandard.getId()))
        {
            return error("修改检测列表'" + checkStandard.getCheckName() + "'失败，上级部门不能是自己");
        }
        checkStandard.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(checkStandardService.updateCheckStandard(checkStandard));
    }

    /**
     * 删除
     */
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:check:remove")
    @GetMapping("/remove/{id}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("id") Long id)
    {
        if (checkStandardService.selectCheckCount(id) > 0)
        {
            return AjaxResult.warn("存在下级部门,不允许删除");
        }
        return toAjax(checkStandardService.deleteCheckById(id));
    }


    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData(@Validated Search search)
    {
        List<Ztree> ztrees = new ArrayList<>();
        List<CheckStandard> checkStandardList = new ArrayList<>();
        if(!StringUtils.isBlank(search.getIsNull()) && search.getIsNull().equals("parent")){
            CheckStandard checkStandard = new CheckStandard();
            checkStandard.setCheckName("无上级");
            checkStandardList.add(checkStandard);
        }

        checkStandardList.addAll(checkStandardService.selectCheckList(new CheckStandard()));
        for(CheckStandard _check : checkStandardList){
            Ztree ztree = new Ztree();
            ztree.setId(_check.getId());
            ztree.setpId(_check.getParentId() == null ? null : _check.getParentId());
            ztree.setOpen(true);
            ztree.setTitle(_check.getCheckName());
            ztree.setName(_check.getCheckName());
            ztrees.add(ztree);
        }
        return ztrees;
    }


    /**
     * 校验名称
     */
    @PostMapping("/checkCheckNameUnique")
    @ResponseBody
    public String checkCheckNameUnique(CheckStandard check)
    {
        return checkStandardService.checkCheckNameUnique(check);
    }

}
