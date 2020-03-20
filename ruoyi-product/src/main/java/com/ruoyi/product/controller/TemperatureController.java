package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.domain.Temperature;
import com.ruoyi.product.entity.Search;
import com.ruoyi.product.service.IProductLineService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.ITemperatureService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenm
 * @create 2019-08-02 10:17
 */
@Controller
@RequestMapping("/product/temperature")
public class TemperatureController extends BaseController {

    private String prefix = "product/temperature";

    @Autowired
    private ITemperatureService temperatureService;

    @Autowired
    private IProductLineService productLineService;

    @Autowired
    private IProductService productService;


    /**
     * 首页
     */
    @RequiresPermissions("product:temperature:view")
    @GetMapping()
    public String produce() {
        return prefix + "/index";
    }

    /**
     * 添加产线页面
     */
    @GetMapping("/add/{lineId}")
    public String add(@PathVariable("lineId") Long lineId, ModelMap model) {
        model.put("productLine", productLineService.selectProductLineById(lineId));
        return prefix + "/add";
    }

    @Log(title = "新增温度面板", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:temperature:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Temperature temperature) {
        temperature.setCreateUser(ShiroUtils.getUserId());
        return toAjax(temperatureService.insertTemperature(temperature));
    }

    /**
     * 查询
     **/

    @PostMapping("/list")
    @RequiresPermissions("product:temperature:list")
    @ResponseBody
    public TableDataInfo list(Temperature temperature) {
        startPage();
        List<Temperature> list = temperatureService.selectTemperatureList(temperature);
        return getDataTable(list);
    }


    /**
     * 修改 
     */
    @GetMapping("/edit/{teId}")
    public String edit(@PathVariable("teId") Long teId, ModelMap model)
    {
        Temperature temperature = temperatureService.selectTemperatureById(teId);
        model.put("temperature", temperature);
        boolean up = false;
        boolean down = false;
        if(temperature.getMachineTypes().contains("1")){
            up = true;
        }
        if(temperature.getMachineTypes().contains("3")){
            down = true;
        }
        model.put("up",up);
        model.put("down",down);
        model.put("lines", productLineService.selectProductLineList(new ProductLine()));
        model.put("machineTyps",temperature.getMachineTypes());
        return prefix + "/edit";
    }

    /**
     * 修改保存产线类型
     */
    @Log(title = "修改温度", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:temperature:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Temperature temperature)
    {
        temperature.setUpdateBy(ShiroUtils.getLoginName());
        return toAjax(temperatureService.updateTemperature(temperature));
    }


    @Log(title = "删除温度", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:temperature:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        try
        {
            return toAjax(temperatureService.deleteTemperatureByIds(ids));
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 展示模板
     */
    @RequestMapping(value = "/template",method = { RequestMethod.GET, RequestMethod.POST })
    public String template(Search search, ModelMap model) {
        Long lineId;
        if(null != search.getLineId()){
            lineId = search.getLineId();
        }else{
            Product product = productService.selectProductById(search.getProductId());
            lineId =product .getLineId();
        }
        Temperature temperature = new Temperature();
        temperature.setLineId(lineId);
        temperature.setProductId(search.getProductId());

        List<Temperature> temperatureList = temperatureService.selectTemperatureList(temperature);
        temperatureList.sort(Comparator.comparing(Temperature::getOrderNum));
        model.put("temperatureList",temperatureList);
        model.put("productLine",productLineService.selectProductLineById(lineId));

        List<Temperature> temperatures = temperatureList.stream().collect(
                Collectors.collectingAndThen( Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Temperature::getMachineTypes))), ArrayList::new)
        );
        Set<String> set = new TreeSet<>();
        String str = new String();
        for(Temperature t : temperatures){
            str += t.getMachineTypes();
            str += ",";
        }
       String[] strArr = str.split(",");
        for(String s : strArr){
            set.add(s);
        }
        model.put("set",set);
        boolean up = false;
        boolean down = false;
        for(String flag:set){
            if(set.contains("1")){
                up = true;
            }
            if(set.contains("3")){
                down = true;
            }
        }
        model.put("up",up);
        model.put("down",down);
        return "template::switch";
    }

    /**
     * 修改温度弹框
     */
    @GetMapping("/editTemp")
    public String editTemp( Search search,ModelMap map)
    {
        map.put("key",search.getKey());
        map.put("word",search.getWord());
        List<Temperature> temperatureList = temperatureService.selectByIds(search.getIdStr());
        map.put("temperatureList",temperatureList);
        return prefix + "/temp";
    }

    /**
     * 修改保存产线类型
     */
    @Log(title = "修改区域温度", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:temperature:edit")
    @PostMapping("/saveTemp")
    @ResponseBody
    public AjaxResult saveTemp( Search search)
    {
        try
        {
            return toAjax(temperatureService.updateTemperatures(search));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }



    @Log(title = "小程序修改区域温度", businessType = BusinessType.UPDATE)
    @RequestMapping("/saveTempApp")
    @ResponseBody
    public AjaxResult saveTempApp(@RequestBody Search search)
    {
        try
        {
            return toAjax(temperatureService.updateTemperatures(search));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

}
