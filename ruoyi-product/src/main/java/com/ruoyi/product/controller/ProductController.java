package com.ruoyi.product.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.constant.SessionTokenConstants;
import com.ruoyi.product.domain.Material;
import com.ruoyi.product.domain.Procedure;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.ProductLine;
import com.ruoyi.product.service.IMaterialService;
import com.ruoyi.product.service.IProcedureService;
import com.ruoyi.product.service.IProductLineService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.system.service.ISysDictDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品
 *
 * @author chenm
 * @create 2019-07-30 19:28
 */
@Controller
@RequestMapping("/product/produce")
public class ProductController extends BaseController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductLineService productLineService;

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private IProcedureService procedureService;

    private String prefix = "product/produce";



    /**
     * 主页面
     */
    @RequiresPermissions("procuct:produce:view")
    @GetMapping()
    public String produce(ModelMap map) {
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines", productLines);
        Procedure procedure = new Procedure();
        List<Procedure> procedureList = procedureService.selectProcedure(procedure);
        Map<String, List<Procedure>> procedureMap = procedureList.stream().collect(Collectors.groupingBy(Procedure::getType, Collectors.toList()));
        map.put("procedureMap", procedureMap);
        return prefix + "/menu";
    }

    /**
     * 添加任务页面
     */
    @GetMapping("/add")
    public String add(ModelMap map) {
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines", productLines);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        map.put("startTime", sdf.format(new Date()));
        return prefix + "/add";
    }


    /**
     * 查询集合
     */
    @RequiresPermissions("product:produce:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Product product) {
        startPage();
        List<Product> list = productService.selectProductList(product);
        return getDataTable(list);
    }

    //查询产线温度
    @PostMapping("/selectTemperature")
    @ResponseBody
    public String selectTemperature(Long productId) {
        return productService.selectTemperature(productId);
    }


    @Log(title = "保存温度", businessType = BusinessType.UPDATE)
    @PostMapping("/editTemperature")
    @RequiresPermissions("product:produce:editTemperature")
    @ResponseBody
    public AjaxResult editTemperature(String temperature, Long productId) {
        try {
            Product product = new Product();
            product.setProductId(productId);
            product.setMixTemperature(temperature);
            return toAjax(productService.updateProduct(product));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 新增保存产品
     */
    @Log(title = "新增任务单", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:produce:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Product product) {
        try {
            product.setCreateUserId(ShiroUtils.getUserId());
            product.setCreateTime(DateUtils.getNowDate());
            Long productId = productService.insertProduct(product);
            if (productId > 0) {
                AjaxResult ajaxResult = new AjaxResult(AjaxResult.Type.SUCCESS, "操作成功", productId);
                return ajaxResult;
            } else {
                return error();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    /**
     * 修改产品类型
     */
    @GetMapping("/edit/{productId}")
    public String edit(@PathVariable("productId") Long productId, ModelMap map) {
        List<ProductLine> productLines = productLineService.selectProductLineList(new ProductLine());
        productLines.sort(Comparator.comparing(ProductLine::getOrderNum));
        map.put("productLines", productLines);
        map.put("product", productService.selectProductById(productId));
        return prefix + "/edit";
    }

    /**
     * 修改保存产品类型
     */
    @Log(title = "修改任务单", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:produce:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@Validated Product product) {
        try {
            product.setUpdateBy(ShiroUtils.getLoginName());
            return toAjax(productService.updateProduct(product));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @GetMapping("/printWork/{productId}")
    public String printWork(@PathVariable("productId") Long productId, ModelMap map) {
        Product product = productService.selectProductById(productId);
        product.setProductUnit(dictDataService.selectDictLabel("sys_product_unit", product.getProductUnit()));
        product.setProductSpecs(dictDataService.selectDictLabel("product_produce_specs", product.getProductSpecs()));
        Long lineId = product.getLineId();
        map.put("product", product);
        map.put("productLine", productLineService.selectProductLineById(lineId));
        return prefix + "/printWork";
    }

    @GetMapping("/printCode/{productNo}")
    public String printCode(@PathVariable("productNo") String productNo, ModelMap map) {
        map.put("productNo", productNo);
        return "product/code/index";
    }


    @Log(title = "删除任务单", businessType = BusinessType.DELETE)
    @RequiresPermissions("product:produce:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        try {
            return toAjax(productService.deleteProductByIds(ids));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @Log(title = "任务单完工", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:produce:over")
    @PostMapping("/editCheckState")
    @ResponseBody
    public AjaxResult editCheckState(@RequestParam("id") Long id) {
        Product product = productService.selectProductById(id);
            /*
            if(product.getProductNum()-(product.getProductAddNum()==null ? 0:product.getProductAddNum())>0){
                return AjaxResult.error("数量未达到生产数量");
            }
            */
        return toAjax(productService.editCheckState(id, "2"));
    }

    @PostMapping("/getChickId")
    @ResponseBody
    public Object getChickId(Long productId) {
        Product product = productService.selectProductById(productId);
        Message msg = new Message();
        msg.setCode(0);
        msg.setMsg("查询成功");
        msg.setData(product);
        return msg;
    }


    @PostMapping("/mobileIndex")
    @RequiresPermissions("product:produce:mobileIndex")
    @ResponseBody
    public Object mobileIndex(Product product, HttpServletRequest request) {
        String token = request.getHeader(SessionTokenConstants.sys_token);
        String sessionId = ShiroUtils.getSessionId();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++" + sessionId);
        Message msg = new Message();
        if (StringUtils.isEmpty(sessionId)) {
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        if (!token.equals(sessionId)) {
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        try {
            product.setStart((product.getPageindex() - 1) * product.getPagesize());
            product.setEnd(product.getPageindex() * product.getPagesize());
            product.setStatus("1");
            msg.setCount(productService.count(product));
            msg.setData(productService.productList(product));
            msg.setCode(Message.getSuccess());
            msg.setMsg("操作成功");
        } catch (Exception e) {
            msg.setCode(Message.getError());
            msg.setMsg("系统出错，请联系管理员");
            e.printStackTrace();
            return msg;
        }
        return msg;
    }

    @RequiresPermissions("product:produce:saveProduct")
    @PostMapping("/saveProduct")
    @ResponseBody
    public Object saveProduct(HttpServletRequest request, Long id, Double weight) {
        String token = request.getHeader(SessionTokenConstants.sys_token);
        String sessionId = ShiroUtils.getSessionId();
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++" + sessionId);
        Message msg = new Message();
        if (StringUtils.isEmpty(sessionId)) {
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        if (!token.equals(sessionId)) {
            msg.setCode(Message.getLogout());
            msg.setMsg("您已掉线，请重新登录");
            return msg;
        }
        try {
            msg = productService.saveProduct(id, weight);
        } catch (Exception e) {
            msg.setCode(Message.getError());
            msg.setMsg("系统出错，请联系管理员");
            e.printStackTrace();
            return msg;
        }
        return msg;
    }


    //新增一键复制功能
    @Log(title = "复制任务单", businessType = BusinessType.INSERT)
    @RequiresPermissions("product:produce:copyProduct")
    @PostMapping("/copyProduct/{id}")
    @ResponseBody
    public Object copyProduct(@PathVariable("id") Long id) {
        try {
            Long productId = productService.copyProduct(id);
            if (productId > 0) {
                AjaxResult ajaxResult = new AjaxResult(AjaxResult.Type.SUCCESS, "操作成功", productId);
                return ajaxResult;
            } else {
                return error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }

    @Log(title = "任务单审核", businessType = BusinessType.UPDATE)
    @RequiresPermissions("product:produce:examine")
    @PostMapping("/examine/{productId}")
    @ResponseBody
    public AjaxResult examine(@PathVariable("productId") Long productId) {
        try {
           return toAjax(productService.examine(productId));
        } catch (Exception e) {
            e.printStackTrace();
            return error(e.getMessage());
        }
    }


}
