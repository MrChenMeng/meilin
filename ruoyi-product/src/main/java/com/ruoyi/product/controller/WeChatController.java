package com.ruoyi.product.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.domain.TemperaturePanel;
import com.ruoyi.product.service.IProductLineService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.ITemperaturePanelSevice;
import com.ruoyi.system.service.ISysDictDataService;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenm
 * 微信公众号推送
 * @create 2019-12-17 13:28
 */
@RestController
@RequestMapping("/product/weChat")
public class WeChatController extends BaseController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductLineService productLineService;

    @Autowired
    private ISysDictDataService sysDictDataService;



    /*
     * 微信测试账号推送
     * */
    @GetMapping("/push/{productId}")
    public AjaxResult push(@PathVariable("productId") Long productId) {
        //1，配置
        WxMpInMemoryConfigStorage wxStorage = new WxMpInMemoryConfigStorage();
        wxStorage.setAppId("wx6966c9554de22ed1");//appid
        wxStorage.setSecret("e151471c2a69e81efe8ebf63adaf29ad");//appsecret
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxStorage);

        //2,推送消息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser("oewbfvhPdBUXYj4rjf9WEzAgsjyg")//要推送的用户openid
                .templateId("TeXJxP-eniJ4eyIZnN5524CFIIZDxLAqAk6MQ2CSDGc")//模版id
//                .url("" )//点击模版消息要访问的网址
                .build();
            //3,如果是正式版发送模版消息，这里需要配置你的信息

        Product product = productService.selectProductById(productId);

        WxMpTemplateData first = new WxMpTemplateData();
        first.setColor("#000000");
        first.setName("first");
        first.setValue("今日下单详情");
        templateMessage.addData(first);

        WxMpTemplateData keyword1 = new WxMpTemplateData();
        keyword1.setColor("#000000");
        keyword1.setName("keyword1");
        ProductLine line = productLineService.selectProductLineById(product.getLineId());
        keyword1.setValue(line.getLineName());
        templateMessage.addData(keyword1);

        WxMpTemplateData keyword2 = new WxMpTemplateData();
        keyword2.setColor("#000000");
        keyword2.setName("keyword2");
        keyword2.setValue(product.getProductName());
        templateMessage.addData(keyword2);

        WxMpTemplateData keyword3 = new WxMpTemplateData();
        keyword3.setColor("#000000");
        keyword3.setName("keyword3");
        keyword3.setValue(product.getProductNo());
        templateMessage.addData(keyword3);

        WxMpTemplateData keyword4 = new WxMpTemplateData();
        keyword4.setColor("#000000");
        keyword4.setName("keyword4");
        keyword4.setValue(product.getMixDosage()+"缸");
        templateMessage.addData(keyword4);

        WxMpTemplateData keyword5 = new WxMpTemplateData();
        keyword5.setColor("#000000");
        keyword5.setName("keyword5");
        keyword5.setValue(product.getMaterialDosage()+"缸");
        templateMessage.addData(keyword5);

        WxMpTemplateData keyword6 = new WxMpTemplateData();
        keyword6.setColor("#000000");
        keyword6.setName("keyword6");
        keyword6.setValue(product.getProductNum()+sysDictDataService.selectDictLabel("sys_product_unit",product.getProductUnit()));
        templateMessage.addData(keyword6);

        WxMpTemplateData keyword7 = new WxMpTemplateData();
        keyword7.setColor("#000000");
        keyword7.setName("keyword7");
        keyword7.setValue(product.getStartTime());
        templateMessage.addData(keyword7);

        WxMpTemplateData keyword8 = new WxMpTemplateData();
        keyword8.setColor("#000000");
        keyword8.setName("keyword8");
        keyword8.setValue(product.getEndTime());
        templateMessage.addData(keyword8);

        WxMpTemplateData remark = new WxMpTemplateData();
        remark.setColor("#000000");
        remark.setName("remark");
        remark.setValue("生产任务单创建完成");
        templateMessage.addData(remark);
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error("微信推送失败,不影响系统其他功能操作");
    }
}
