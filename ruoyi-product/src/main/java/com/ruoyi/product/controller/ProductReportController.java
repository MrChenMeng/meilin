package com.ruoyi.product.controller;

import com.ruoyi.common.core.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chenm
 * @create 2019-09-04 9:12
 */
@Controller()
@RequestMapping("/product/report")
public class ProductReportController extends BaseController {

    private String prefix = "product/report";

    @RequiresPermissions("product:report:view")
    @GetMapping()
    public String index(){
        return prefix+"/index";
    }

}
