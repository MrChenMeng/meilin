package com.ruoyi.product.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.product.domain.Energy;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.domain.TemperaturePanel;
import com.ruoyi.product.service.IEnergyService;
import com.ruoyi.product.service.IProductLineService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.ITemperaturePanelSevice;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenm
 * @create 2019-10-17 18:26
 */
@Controller
@RequestMapping("/product/report")
public class ReportController extends BaseController {

    private String prefix = "product/report";

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductLineService productLineService;

    @Autowired
    private IEnergyService energyService;

    @Autowired
    private ITemperaturePanelSevice temperaturePanelSevice;

    @GetMapping("/capacity")
    private String capacity(ModelMap map){
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines",productLines);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("nowTime",sdf.format(new Date()));
        return prefix + "/capacity" ;
    }


    @PostMapping("/capacity")
    @ResponseBody
    public TableDataInfo capacity(Product product){
        List<Product> products = productService.capacity(product);
        startPage();
        List<Product> list = productService.capacity(product);
        TableDataInfo dataInfo = getDataTable(list);
        dataInfo.setData(products);
        return dataInfo;
    }

    @GetMapping("/energy")
    private String energy(ModelMap map){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("nowTime",sdf.format(new Date()));
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines",productLines);
        return prefix + "/energy";
    }


    @PostMapping("/energy")
    @ResponseBody
    public TableDataInfo energy(Energy energy){
        List<Energy> energys = energyService.energy(energy);
        startPage();
        List<Energy> list = energyService.energy(energy);
        TableDataInfo dataInfo = getDataTable(list);
        dataInfo.setData(energys);
        return dataInfo;
    }

    @GetMapping("/speed")
    private String speed(ModelMap map){
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines",productLines);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("nowTime",sdf.format(new Date()));
        return prefix + "/speed" ;
    }

    @PostMapping("/speed")
    @ResponseBody
    public TableDataInfo speed(Product product){
        startPage();
        List<Product> list = productService.speed(product);
        return getDataTable(list);
    }

    @GetMapping("/trace")
    private String trace(ModelMap map){
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines",productLines);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        map.put("nowTime",sdf.format(new Date()));
        return prefix + "/trace" ;
    }

    @PostMapping("/trace")
    @ResponseBody
    public TableDataInfo trace (Product product){
        return null;
    }

    @GetMapping("/temperature")
    private String template(ModelMap map){
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines",productLines);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        map.put("startTime",simpleDateFormat.format(new Date()));
        map.put("endTime",sdf.format(new Date()));
        return prefix + "/temperature" ;
    }

    @PostMapping("/temperature")
    @ResponseBody
    public TableDataInfo temperature (TemperaturePanel temperaturePanel){
        startPage();
        List<TemperaturePanel> list = temperaturePanelSevice.temperature(temperaturePanel);
        TableDataInfo dataInfo = getDataTable(list);
        if(!list.isEmpty()){
            List<TemperaturePanel> temperaturePanels = new ArrayList<>();
            temperaturePanels.add(list.get(0));
            temperaturePanels.add(list.get(1));
            dataInfo.setData(temperaturePanels);
        }else{
            dataInfo.setData(null);
        }
       /* dataInfo.setData(energys);*/
        return dataInfo;
    }

    @PostMapping("/getTime")
    @ResponseBody
    public Message getTime (){
        Message msg = new Message();
        msg.setCode(Message.getSuccess());
        Map<String ,String> dateMap = new HashedMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        long currentTime = System.currentTimeMillis() ;
        Date startTime = new Date(currentTime - 60 * 60 * 1000);
        dateMap.put("startTime",sdf.format(startTime));
        Date endTime = new Date(currentTime);
        dateMap.put("endTime",sdf.format(endTime));
        msg.setData(dateMap);
        return msg;
    }
}
