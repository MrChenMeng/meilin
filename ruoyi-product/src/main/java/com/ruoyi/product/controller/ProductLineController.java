package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.constant.ProductLineConstants;
import com.ruoyi.product.constant.SessionTokenConstants;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.service.IProductLineService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 产线
 */
@Controller
@RequestMapping("/product/productLine")
public class ProductLineController extends BaseController {

    @Autowired
    private IProductLineService productLineService;


    private String prefix = "product/productLine";


    /**
     * 首页
     */
    @RequiresPermissions("product:productLine:view")
    @GetMapping()
    public String produce() {
        return prefix + "/index";
    }

    /**
     * 添加产线页面
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    /**
     * 新增产线
     */
    @Log(title = "新增产线", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:productLine:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProductLine productLine) {
        productLine.setCreateUserId(ShiroUtils.getUserId());
        productLine.setCreateTime(DateUtils.getNowDate());

        if (ProductLineConstants.DICT_TYPE_NOT_UNIQUE.equals(productLineService.checkLineCode(productLine)))
        {
            return error("新增产线'" + productLine.getLineName() + "'失败，产线编码已存在");
        }
        productLine.setCreateBy(ShiroUtils.getLoginName());
        return toAjax(productLineService.insertProductLine(productLine));

    }



    /**
     * 查询
     */

    @PostMapping("/list")
    @RequiresPermissions("product:productLine:list")
    @ResponseBody
    public TableDataInfo list(ProductLine productLine) {
        startPage();
        List<ProductLine> list = productLineService.selectProductLineList(productLine);
        return getDataTable(list);
    }

    /**
     * 校验编码
     */
    @PostMapping("/checkLineCode")
    @ResponseBody
    public String checkLineCode(ProductLine productLine)
    {
        return productLineService.checkLineCode(productLine);
    }


    /**
     * 修改产线类型
     */
    @GetMapping("/edit/{lineId}")
    public String edit(@PathVariable("lineId") Long lineId, ModelMap mmap)
    {
        mmap.put("productLine", productLineService.selectProductLineById(lineId));
        return prefix + "/edit";
    }

    /**
     * 修改保存产线类型
     */
    @Log(title = "修改产线", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:productLine:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated ProductLine productLine)
    {
        if (ProductLineConstants.DICT_TYPE_NOT_UNIQUE.equals(productLineService.checkLineCode(productLine)))
        {
            return error("修改产线'" + productLine.getLineName() + "'失败，产线类型已存在");
        }
        productLine.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(productLineService.updateProductLine(productLine));
    }

    @Log(title = "删除产线", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:productLine:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(productLineService.deleteProductLineByIds(ids));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData()
    {
        List<Ztree> ztrees = new ArrayList<>();
        List<ProductLine> productLines = new ArrayList<>();
        ProductLine productLine = new ProductLine();
        productLine.setLineId(0L);
        productLine.setPid(null);
        productLine.setLineName("所有产线");
        productLines.add(productLine);
        productLines.addAll(productLineService.selectProductLineList(new ProductLine())) ;
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        if (null != productLines) {
            for (ProductLine line : productLines) {
                Ztree node = new Ztree();
                node.setId(line.getLineId());
                node.setpId(line.getPid() == null ? null : line.getPid());
                node.setOpen(true);
                node.setName(line.getLineName());
                ztrees.add(node);
            }
        }
        return ztrees;
    }

    @PostMapping("/mobileIndex")
    @ResponseBody
    public Object mobileIndex(ProductLine plan, HttpServletRequest request){
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
            msg.setCode(Message.getSuccess());
            msg.setMsg("操作成功");
            msg.setData(productLineService.selectProductLineList(plan));
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode(Message.getError());
            msg.setMsg("系统出错，请联系管理员");
            return msg;
        }
        return msg;
    }

}
