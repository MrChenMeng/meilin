package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.print.domain.PrintTemplate;
import com.ruoyi.print.service.IPrintTemplateService;
import com.ruoyi.product.constant.SessionTokenConstants;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.QRCode;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.IQRCodeService;
import com.ruoyi.product.unit.JSONUtil;
import com.ruoyi.product.unit.PatternUtil;
import com.ruoyi.product.unit.TransUtil;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author chenm
 * @create 2019-09-26 14:18
 */
@Controller
@RequestMapping("/product/code")
public class QRCodeController extends BaseController {

    private String prefix = "product/code";

    @Autowired
    private IQRCodeService qrCodeService;

    @Autowired
    private IPrintTemplateService printTemplateService;

    @Autowired
    private IProductService productService;

    @Autowired
    ISysDictDataService dictDataService;



    @PostMapping("/list")
    @RequiresPermissions("product:code:view")
    @ResponseBody
    public TableDataInfo list(QRCode qrCode) {
        startPage();
        List<QRCode> list = qrCodeService.selectQRCodeList(qrCode);
        int count = 0;
        List<QRCode> qrCodes = qrCodeService.selectCountGroup(qrCode);
        if(!qrCodes.isEmpty()){
            count = qrCodes.get(0).getCount();
        }
        for(QRCode code : list){
            code.setCount(count);
        }
        return getDataTable(list);
    }

    @PostMapping("/getCount")
    @ResponseBody
    public Object getCount(QRCode qrCode) {
        Message msg = new Message();
        try {
            List<QRCode> list = qrCodeService.selectCountGroup(qrCode);
            msg.setCode(Message.getSuccess());
            msg.setMsg("操作成功");
            msg.setData(list.size());
        }catch (Exception e){
            e.printStackTrace();
            msg.setCode(Message.getError());
            msg.setMsg("系统出错");
        }

        return msg;
    }

    @GetMapping("/add/{productNo}")
    @Transactional
    public String add(@PathVariable("productNo")String productNo,ModelMap map) {
        PrintTemplate printTemplate = new PrintTemplate();
        printTemplate.setStatus("0");
        List<PrintTemplate> printTemplateList = printTemplateService.selectPrintTemplateList(printTemplate);
        map.put("printTemplateList",printTemplateList);
        QRCode qrCode = new QRCode();
        List<QRCode> qrCodeList = qrCodeService.selectQRCodeList(qrCode);
        if(!qrCodeList.isEmpty()){
            qrCode.setPrintId(qrCodeList.get(0).getPrintId());
        }
        Product product = productService.selectProductByProductNo(productNo);
        qrCode.setProductNo(productNo);
        qrCode.setLineId(product.getLineId());
        String productSpecs = dictDataService.selectDictLabel("product_produce_specs",product.getProductSpecs()) ;//获取产品的包装规格
        qrCode.setWeight(Integer.valueOf(PatternUtil.extract(PatternUtil.regNum,productSpecs))); //赋值产品包装规格的重量
        List<SysDictData> dictDatas = dictDataService.selectDictDataByType("sys_product_unit");
        Message msg = TransUtil.trans(PatternUtil.extract(PatternUtil.regEnglish,productSpecs));
        if(msg.getCode() == Message.getError()){
            throw new BusinessException("产品的包装规格" + productSpecs + msg.getMsg());
        }
        for(SysDictData dictData : dictDatas){
            Message _m = TransUtil.trans(dictData.getDictLabel());
            if(_m.getCode() == Message .getSuccess()){
                if(product.getProductUnit().equals(dictData.getDictValue())){
                    qrCode.setProductUint(dictData.getDictLabel());
                }
            }else{
                throw new BusinessException("数据字典的" + dictData.getDictLabel() + _m.getMsg());
            }
        }
        map.put("qrCode",qrCode);
        map.put("dictDatas",dictDatas);
        return prefix + "/add";
    }

    /**
     * 新增保存产品
     */
    @Log(title = "新增二维码", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:code:add")
    @PostMapping("/add")
    @ResponseBody
    public Message addSave(QRCode qrCode){
        qrCode.setCreateBy(ShiroUtils.getSysUser().getCreateBy());
        qrCode.setCreateTime(DateUtils.getNowDate());
        return qrCodeService.insertQRCode(qrCode);
    }


    @GetMapping("/edit/{id}/{count}")
    public String edit(@PathVariable("id") Long id,@PathVariable("count")Integer count, ModelMap map)
    {
        QRCode qrCode = qrCodeService.selectQRCodeById(id);
        List<SysDictData> dictDatas = dictDataService.selectDictDataByType("sys_product_unit");
        map.put("dictDatas",dictDatas);
        qrCode.setCount(count);
        map.put("qrCode",qrCode);
        PrintTemplate printTemplate = new PrintTemplate();
        printTemplate.setStatus("0");
        List<PrintTemplate> printTemplateList = printTemplateService.selectPrintTemplateList(printTemplate);
        map.put("printTemplateList",printTemplateList);
        return prefix + "/edit";
    }

    @GetMapping("print/{productNo}")
    public String print(@PathVariable("productNo")String productNo,String idStr,String classNumber,String jianYan,String checkCode,ModelMap map) throws ParseException {
        QRCode qrCode = new QRCode();
        qrCode.setProductNo(productNo);
        qrCode.setIdStr(idStr);
        List<QRCode> qrCodeList = qrCodeService.selectQRCodeList(qrCode);
        Product product = productService.selectProductByProductNo(productNo);
        PrintTemplate printTemplate = printTemplateService.selectPrintTemplateById(qrCodeList.get(0).getPrintId());
        List<String> printDatas = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");

        for(QRCode code : qrCodeList){
            Map<String, Object> jsonMap = new HashMap<String, Object>();
            jsonMap.put("checkCode",checkCode);
            jsonMap.put("productSpecs",dictDataService.selectDictLabel("product_produce_specs",product.getProductSpecs()));
            jsonMap.put("name",product.getProductName());
            jsonMap.put("productNo",product.getProductNo());
            jsonMap.put("classNumber",classNumber);
            jsonMap.put("userName",jianYan);
            jsonMap.put("startTime",dateFormat.format(new Date()));
            jsonMap.put("printCode",code.getUuid());
            printDatas.add(JSONUtil.mapToJson(jsonMap));
        }
        map.put("printDatas",printDatas);
        map.put("printTemplate",printTemplate);
        map.put("idStr",idStr);
        return prefix + "/view";
    }


    @PostMapping("/checkOver")
    @ResponseBody
    public AjaxResult checkOver(String idStr){
        return toAjax(qrCodeService.checkOver(idStr));
    }

    @Log(title = "编辑二维码", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:code:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated QRCode qrCode)
    {
        return toAjax(qrCodeService.updateQRCode(qrCode));
    }


    @Log(title = "删除二维码", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:code:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
            return toAjax(qrCodeService.deleteQRCodeByIds(ids));
    }

    @RequiresPermissions("product:code:input")
    @PostMapping("/input")
    @ResponseBody
    public Object input(HttpServletRequest request){
        String token = request.getHeader(SessionTokenConstants.sys_token);
        String sessionId = ShiroUtils.getSessionId();
        Message msg = new Message();
        if(StringUtils.isEmpty(sessionId) ){
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        if(!token.equals(sessionId)){
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        msg.setCode(Message.getSuccess());
        msg.setMsg("操作成功");
        return msg;
    }

    @RequiresPermissions("product:code:mobileIndex")
    @PostMapping("/mobileIndex")
    @ResponseBody
    public Object mobileIndex(String uuid, HttpServletRequest request){
        String token = request.getHeader(SessionTokenConstants.sys_token);
        String sessionId = ShiroUtils.getSessionId();
        Message msg = new Message();
        if(StringUtils.isEmpty(sessionId) ){
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        if(!token.equals(sessionId)){
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        if(uuid.length()!=13){
            msg.setCode(Message.getError());
            msg.setMsg("二维码长度为13位");
            return msg;
        }
        QRCode qrCode = qrCodeService.selectQRCodeByUuid(uuid);
        if(qrCode == null){
            msg.setCode(Message.getError());
            msg.setMsg("不是有效的二维码");
            return msg;
        }
        if(qrCode.getDataState() == 2){
            msg.setCode(Message.getError());
            msg.setMsg("二维码已被删除");
            return msg;
        }
        Product product = productService.selectProductByProductNo(qrCode.getProductNo());
        if(product.getDatastate() == 2){
            msg.setCode(Message.getError());
            msg.setMsg("此订单已作废");
            return msg;
        }
        Double completeSchedule = product.getProductAddNum() / product.getProductNum();
        BigDecimal b = new BigDecimal(completeSchedule);
        completeSchedule = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double Surplus = product.getProductNum() - product.getProductAddNum();

        List<SysDictData> dictDatas = dictDataService.selectDictDataByType("sys_product_unit");
        for(SysDictData dictData : dictDatas){
            Message _m = TransUtil.trans(dictData.getDictLabel());
            if(_m.getCode() == Message .getSuccess()){
                if(product.getProductUnit().equals(dictData.getDictValue())){
                    qrCode.setProductUint(dictData.getDictLabel());
                }
                if(qrCode.getUnit().equals(dictData.getDictValue())){
                    qrCode.setCodeUnit(dictData.getDictLabel());
                }
            }else{
                throw new BusinessException("数据字典的" + dictData.getDictLabel() + _m.getMsg());
            }
        }

        qrCode.setCompleteSchedule(completeSchedule);
        qrCode.setSurplus(Surplus);
        qrCode.setProductAddNum(product.getProductAddNum());//产品累计数量
        msg.setData(qrCode);
        msg.setCode(Message.getSuccess());
        msg.setMsg("操作成功");
        return msg;
    }

}
