package com.ruoyi.quartz.task;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.product.domain.Temperature;
import com.ruoyi.product.domain.TemperaturePanel;
import com.ruoyi.product.service.ITemperaturePanelSevice;
import com.ruoyi.product.service.ITemperatureService;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import com.ruoyi.weChat.weChat.WeChatMsgSend;
import com.ruoyi.weChat.weChat.WeChatUrlData;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{

    @Autowired
    private ITemperaturePanelSevice temperaturePanelSevice;

    @Autowired
    private ITemperatureService temperatureService;

    @Autowired
    private ISysDictDataService sysDictDataService;

    private static final Logger log = LoggerFactory.getLogger(RyTask.class);


    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    /*数据库定时备份*/
    public void backup()
    {
        String filepath =  "e:/database/"+DateUtils.getCurrentYear() + "/" + DateUtils.getCurrentMonth() + "/" + DateUtils.getCurrentDay()+ "/meilin.sql"; // 备份的路径地址
        FileUtils.createParentDir(filepath);        //创建文件夹
        Runtime runtime = Runtime.getRuntime();     //获取Runtime实例
        String database = "ieis-meilin";            // 需要备份的数据库名
        String user = "root";                       //数据库账号
        String password = "root";                   // 数据库密码
        //执行命令
        String stmt = "mysqldump -h localhost -u " + user + " -p" + password + " --databases "+database+" --tables > "+filepath;
        System.out.println(stmt);
        try {
            String[] command = { "cmd", "/c", stmt};
            Process process = runtime.exec(command);
            InputStream input = process.getInputStream();
            System.out.println(IOUtils.toString(input, "UTF-8"));
            //若有错误信息则输出
            InputStream errorStream = process.getErrorStream();
            System.out.println(IOUtils.toString(errorStream, "gbk"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AjaxResult templeWarn(){
        String contentVakue = "[INDUSFO--MES消息推送]\n";
        log.info("INDUSFO--MES消息推送");
        try{
            WeChatMsgSend swx = new WeChatMsgSend();
            boolean flag = false;
            List<SysDictData> dictUsers = sysDictDataService.selectDictDataByType("product_template_user");
            List<SysDictData> dictDatas = sysDictDataService.selectDictDataByType("product_template_warm");
            if(dictDatas.isEmpty()){
                return AjaxResult.error();
            }
            if(null == dictUsers){
                return AjaxResult.error("无推送人员");
            }
            Double upWarm = null;
            Double downWarm = null ;
            for(SysDictData dictData : dictDatas){
                if(dictData.getDictLabel().equals("上限")){
                    upWarm = Double.valueOf(dictData.getDictValue());
                }
                if(dictData.getDictLabel().equals("下限")){
                    downWarm = Double.valueOf(dictData.getDictValue());
                }
            }
            List<Long>  ids = temperaturePanelSevice.top12();
            for(Long id : ids){
                TemperaturePanel temperaturePanel = temperaturePanelSevice.selectById(id);

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
        }catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("微信推送失败,不影响系统其他功能操作");
    }
}
