package com.ruoyi.product.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.domain.Temperature;
import com.ruoyi.product.domain.TemperaturePanel;
import com.ruoyi.product.service.IProductLineService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.ITemperaturePanelSevice;
import com.ruoyi.product.service.ITemperatureService;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.weChat.weChat.WeChatMsgSend;
import com.ruoyi.weChat.weChat.WeChatUrlData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * 微信企业推送
 * @author chenm
 * @create 2020-01-13 10:50
 */
@RestController
@RequestMapping("/product/weChatEnter")
public class WeChatEnterController {


    @Autowired
    private IProductService productService;

    @Autowired
    private IProductLineService productLineService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    @Autowired
    private ITemperaturePanelSevice temperaturePanelSevice;

    @Autowired
    private ITemperatureService temperatureService;


    @GetMapping("/warm")
    public AjaxResult warm(TemperaturePanel t){
        String contentVakue = "[INDUSFO--MES消息推送]\n";
        WeChatMsgSend swx = new WeChatMsgSend();
        boolean flag = false;
        try {
            List<SysDictData> dictUsers = sysDictDataService.selectDictDataByType("product_template_user");
            List<SysDictData> dictDatas = sysDictDataService.selectDictDataByType("product_template_warm");
            Double upWarm = null;
            Double downWarm = null ;
            if(dictDatas.isEmpty()){
                return AjaxResult.error();
            }
            if(null == dictUsers){
                return AjaxResult.error("无推送人员");
            }
            for(SysDictData dictData : dictDatas){
                if(dictData.getDictLabel().equals("上限")){
                    upWarm = Double.valueOf(dictData.getDictValue());
                }
                if(dictData.getDictLabel().equals("下限")){
                    downWarm = Double.valueOf(dictData.getDictValue());
                }
            }
            TemperaturePanel temperaturePanel = temperaturePanelSevice.selectById(t.getId());

            if(temperaturePanel.getNumber()==11L){
                List<Temperature> temperatures = temperatureService.selectTemperatureTemplate(1L);

                if(null != upWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线一：一区上机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线一：二区上机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线一：三区上机温度超标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线一：四区上机温度超标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线一：五区上机温度超标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线一：六区上机温度超标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线一：七区上机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线一：八区上机温度超标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                }

                if(null != downWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线一：一区上机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线一：二区上机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线一：三区上机温度不达标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线一：四区上机温度不达标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线一：五区上机温度不达标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线一：六区上机温度不达标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线一：七区上机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线一：八区上机温度不达标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                }
            }

            if(temperaturePanel.getNumber()==21L){
                List<Temperature> temperatures = temperatureService.selectTemperatureTemplate(2L);
                if(null != upWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线二：一区上机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线二：二区上机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线二：三区上机温度超标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线二：四区上机温度超标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线二：五区上机温度超标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线二：六区上机温度超标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线二：七区上机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线二：八区上机温度超标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(8).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线二：九区上机温度超标(设定温度" + temperatures.get(8).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(9).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线二：十区上机温度超标(设定温度" + temperatures.get(9).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues11())){
                        flag = true;
                        contentVakue += "产线二：一区下机温度超标(设定温度" + temperatures.get(0).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues11()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues12())){
                        flag = true;
                        contentVakue += "产线二：二区下机温度超标(设定温度" + temperatures.get(1).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues12()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues13())){
                        flag = true;
                        contentVakue += "产线二：三区下机温度超标(设定温度" + temperatures.get(2).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues13()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues14())){
                        flag = true;
                        contentVakue += "产线二：四区下机温度超标(设定温度" + temperatures.get(3).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues14()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(10).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues15())){
                        flag = true;
                        contentVakue += "产线二：换网区下机温度超标(设定温度" + temperatures.get(10).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues15()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(11).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues16())){
                        flag = true;
                        contentVakue += "产线二：机头区下机温度超标(设定温度" + temperatures.get(11).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues16()+"℃)\n";
                    }
                }

                if(null != downWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线二：一区上机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线二：二区上机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线二：三区上机温度不达标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线二：四区上机温度不达标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线二：五区上机温度不达标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线二：六区上机温度不达标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线二：七区上机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线二：八区上机温度不达标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(8).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线二：九区上机温度不达标(设定温度" + temperatures.get(8).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(9).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线二：十区上机温度不达标(设定温度" + temperatures.get(9).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues11())){
                        flag = true;
                        contentVakue += "产线二：一区下机温度不达标(设定温度" + temperatures.get(0).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues11()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues12())){
                        flag = true;
                        contentVakue += "产线二：二区下机温度不达标(设定温度" + temperatures.get(1).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues12()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues13())){
                        flag = true;
                        contentVakue += "产线二：三区下机温度不达标(设定温度" + temperatures.get(2).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues13()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureDownAct()) + downWarm < Double.valueOf(temperaturePanel.getValues14())){
                        flag = true;
                        contentVakue += "产线二：四区下机温度不达标(设定温度" + temperatures.get(3).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues14()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(10).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues15())){
                        flag = true;
                        contentVakue += "产线二：换网区下机温度不达标(设定温度" + temperatures.get(10).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues15()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(11).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues16())){
                        flag = true;
                        contentVakue += "产线二：机头区下机温度不达标(设定温度" + temperatures.get(11).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues16()+"℃)\n";
                    }
                }
            }

            if(temperaturePanel.getNumber()==31L){
                List<Temperature> temperatures = temperatureService.selectTemperatureTemplate(3L);
                if(null != upWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线三：一区上机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线三：二区上机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线三：三区上机温度超标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues4())){
                        contentVakue += "产线三：四区上机温度超标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线三：五区上机温度超标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线三：六区上机温度超标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线三：机头区上机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线三：一区下机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线三：二区下机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线三：机头区下机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                }

                if(null != downWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线三：一区上机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线三：二区上机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线三：三区上机温度不达标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线三：四区上机温度不达标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线三：五区上机温度不达标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线三：六区上机温度不达标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线三：机头区上机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线三：一区下机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线三：二区下机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线三：机头区下机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                }
            }

            if(temperaturePanel.getNumber()==41L){
                List<Temperature> temperatures = temperatureService.selectTemperatureTemplate(4L);
                if(null != upWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线四：一区上机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线四：二区上机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线四：三区上机温度超标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线四：四区上机温度超标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线四：五区上机温度超标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线四：六区上机温度超标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线四：七区上机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线四：八区上机温度超标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(8).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线四：九区上机温度超标(设定温度" + temperatures.get(8).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(9).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线四：十区上机温度超标(设定温度" + temperatures.get(9).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues11())){
                        flag = true;
                        contentVakue += "产线四：一区下机温度超标(设定温度" + temperatures.get(0).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues11()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues12())){
                        flag = true;
                        contentVakue += "产线四：二区下机温度超标(设定温度" + temperatures.get(1).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues12()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues13())){
                        flag = true;
                        contentVakue += "产线四：三区下机温度超标(设定温度" + temperatures.get(2).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues13()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues14())){
                        flag = true;
                        contentVakue += "产线四：四区下机温度超标(设定温度" + temperatures.get(3).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues14()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(10).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues15())){
                        flag = true;
                        contentVakue += "产线四：换网区下机温度超标(设定温度" + temperatures.get(10).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues15()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(11).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues16())){
                        flag = true;
                        contentVakue += "产线四：机头区下机温度超标(设定温度" + temperatures.get(11).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues16()+"℃)\n";
                    }
                }

                if(null != downWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线四：一区上机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线四：二区上机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线四：三区上机温度不达标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线四：四区上机温度不达标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线四：五区上机温度不达标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线四：六区上机温度不达标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线四：七区上机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线四：八区上机温度不达标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(8).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线四：九区上机温度不达标(设定温度" + temperatures.get(8).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(9).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线四：十区上机温度不达标(设定温度" + temperatures.get(9).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues11())){
                        flag = true;
                        contentVakue += "产线四：一区下机温度不达标(设定温度" + temperatures.get(0).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues11()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues12())){
                        flag = true;
                        contentVakue += "产线四：二区下机温度不达标(设定温度" + temperatures.get(1).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues12()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues13())){
                        flag = true;
                        contentVakue += "产线四：三区下机温度不达标(设定温度" + temperatures.get(2).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues13()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureDownAct()) + downWarm < Double.valueOf(temperaturePanel.getValues14())){
                        flag = true;
                        contentVakue += "产线四：四区下机温度不达标(设定温度" + temperatures.get(3).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues14()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(10).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues15())){
                        flag = true;
                        contentVakue += "产线四：换网区下机温度不达标(设定温度" + temperatures.get(10).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues15()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(11).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues16())){
                        flag = true;
                        contentVakue += "产线四：机头区下机温度不达标(设定温度" + temperatures.get(11).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues16()+"℃)\n";
                    }
                }
            }

            if(temperaturePanel.getNumber()==51L){
                List<Temperature> temperatures = temperatureService.selectTemperatureTemplate(5L);
                if(null != upWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线五：一区上机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线五：二区上机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线五：三区上机温度超标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线五：四区上机温度超标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线五：五区上机温度超标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线五：六区上机温度超标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线五：七区上机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线五：八区上机温度超标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(8).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线五：九区上机温度超标(设定温度" + temperatures.get(8).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(9).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线五：十区上机温度超标(设定温度" + temperatures.get(9).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues11())){
                        flag = true;
                        contentVakue += "产线五：一区下机温度超标(设定温度" + temperatures.get(0).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues11()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues12())){
                        flag = true;
                        contentVakue += "产线五：二区下机温度超标(设定温度" + temperatures.get(1).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues12()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues13())){
                        flag = true;
                        contentVakue += "产线五：三区下机温度超标(设定温度" + temperatures.get(2).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues13()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues14())){
                        flag = true;
                        contentVakue += "产线五：四区下机温度超标(设定温度" + temperatures.get(3).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues14()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(10).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues15())){
                        flag = true;
                        contentVakue += "产线五：换网区下机温度超标(设定温度" + temperatures.get(10).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues15()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(11).getTemperatureDownAct()) + upWarm < Double.valueOf(temperaturePanel.getValues16())){
                        flag = true;
                        contentVakue += "产线五：机头区下机温度超标(设定温度" + temperatures.get(11).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues16()+"℃)\n";
                    }
                }

                if(null != downWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线五：一区上机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线五：二区上机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线五：三区上机温度不达标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线五：四区上机温度不达标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线五：五区上机温度不达标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线五：六区上机温度不达标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线五：七区上机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线五：八区上机温度不达标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(8).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues9())){
                        flag = true;
                        contentVakue += "产线五：九区上机温度不达标(设定温度" + temperatures.get(8).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues9()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(9).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues10())){
                        flag = true;
                        contentVakue += "产线五：十区上机温度不达标(设定温度" + temperatures.get(9).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues10()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(0).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues11())){
                        flag = true;
                        contentVakue += "产线五：一区下机温度不达标(设定温度" + temperatures.get(0).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues11()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues12())){
                        flag = true;
                        contentVakue += "产线五：二区下机温度不达标(设定温度" + temperatures.get(1).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues12()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues13())){
                        flag = true;
                        contentVakue += "产线五：三区下机温度不达标(设定温度" + temperatures.get(2).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues13()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureDownAct()) + downWarm < Double.valueOf(temperaturePanel.getValues14())){
                        flag = true;
                        contentVakue += "产线五：四区下机温度不达标(设定温度" + temperatures.get(3).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues14()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(10).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues15())){
                        flag = true;
                        contentVakue += "产线五：换网区下机温度不达标(设定温度" + temperatures.get(10).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues15()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(11).getTemperatureDownAct()) - downWarm > Double.valueOf(temperaturePanel.getValues16())){
                        flag = true;
                        contentVakue += "产线五：机头区下机温度不达标(设定温度" + temperatures.get(11).getTemperatureDownAct()+"℃实际温度"+temperaturePanel.getValues16()+"℃)\n";
                    }
                }
            }

            if(temperaturePanel.getNumber()==61L){
                List<Temperature> temperatures = temperatureService.selectTemperatureTemplate(6L);
                if(null != upWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线六：一区上机温度超标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线六：二区上机温度超标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线六：三区上机温度超标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线六：四区上机温度超标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线六：五区上机温度超标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线六：六区上机温度超标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线六：七区上机温度超标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct()) + upWarm < Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线六：八区上机温度超标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                }

                if(null != downWarm){
                    if(Double.valueOf(temperatures.get(0).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues1())){
                        flag = true;
                        contentVakue += "产线六：一区上机温度不达标(设定温度" + temperatures.get(0).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues1()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(1).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues2())){
                        flag = true;
                        contentVakue += "产线六：二区上机温度不达标(设定温度" + temperatures.get(1).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues2()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(2).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues3())){
                        flag = true;
                        contentVakue += "产线六：三区上机温度不达标(设定温度" + temperatures.get(2).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues3()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(3).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues4())){
                        flag = true;
                        contentVakue += "产线六：四区上机温度不达标(设定温度" + temperatures.get(3).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues4()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(4).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues5())){
                        flag = true;
                        contentVakue += "产线六：五区上机温度不达标(设定温度" + temperatures.get(4).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues5()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(5).getTemperatureUpAct()) - downWarm > Double.valueOf(temperaturePanel.getValues6())){
                        flag = true;
                        contentVakue += "产线六：六区上机温度不达标(设定温度" + temperatures.get(5).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues6()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(6).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues7())){
                        flag = true;
                        contentVakue += "产线六：七区上机温度不达标(设定温度" + temperatures.get(6).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues7()+"℃)\n";
                    }
                    if(Double.valueOf(temperatures.get(7).getTemperatureUpAct())  - downWarm > Double.valueOf(temperaturePanel.getValues8())){
                        flag = true;
                        contentVakue += "产线六：八区上机温度不达标(设定温度" + temperatures.get(7).getTemperatureUpAct()+"℃实际温度"+temperaturePanel.getValues8()+"℃)\n";
                    }
                }
            }
            if(flag){
                contentVakue+="[PS:本消息为系统自动推送，取消订阅请联系管理员]";
                String token = swx.getToken("ww8dddce323a11b4c7","RWlJVEAXc3rR499KZSl2F9l-vTEwlyYPlsjpjkguHpk");
                for(SysDictData dictUser : dictUsers){
                    String postdata = swx.createpostdata(dictUser.getDictValue(), "text", 1000004, "content",contentVakue);
                    swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE,(new WeChatUrlData()).getSendMessage_Url(), postdata, token);
                }
            }
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("微信推送失败,不影响系统其他功能操作");
    }



    /*
     * 企业微信订单推送
     * */
    @GetMapping("/push/{productId}")
    public AjaxResult push(@PathVariable("productId") Long productId) {
        WeChatMsgSend swx = new WeChatMsgSend();
        try {
            Product product = productService.selectProductById(productId);
            ProductLine line = productLineService.selectProductLineById(product.getLineId());
            String contentVakue = "生产产线: " + line.getLineName() + "\n";
            contentVakue += "产品名称：" + product.getProductName() + "\n";
            contentVakue += "生产单号：" + product.getProductNo() + "\n";
            contentVakue += "拌和缸数：" + product.getMixDosage()+"缸\n";
            contentVakue += "助剂缸数：" + product.getMaterialDosage()+"缸\n";
            contentVakue += "生产数量：" + product.getProductNum()+sysDictDataService.selectDictLabel("sys_product_unit",product.getProductUnit()) + "\n";
            contentVakue += "生产时间：" + product.getStartTime() + "\n";
            contentVakue += "交货时间：" + product.getEndTime();

            String token = swx.getToken("ww8dddce323a11b4c7","RWlJVEAXc3rR499KZSl2F9l-vTEwlyYPlsjpjkguHpk");
            String postdata = swx.createpostdata("QianMeiLing", "text", 1000004, "content",contentVakue);
            swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE,(new WeChatUrlData()).getSendMessage_Url(), postdata, token);

            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("微信推送失败,不影响系统其他功能操作");
    }
}
