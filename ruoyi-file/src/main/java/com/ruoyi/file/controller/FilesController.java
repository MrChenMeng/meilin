package com.ruoyi.file.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.base.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.file.domain.Files;
import com.ruoyi.file.domain.PlaysOrder;
import com.ruoyi.file.service.IFilesService;
import com.ruoyi.file.service.IPlayOrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * @author chenm
 * @create 2019-08-19 19:42
 */
@Controller
@RequestMapping("/file/manage")
public class FilesController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(FilesController.class);
    private static String Save_Url = FileUploadUtils.getDefaultBaseDir();
    private String prefix = "file/manage";

    @Autowired
    private IFilesService filesService;

    @Autowired
    private IPlayOrderService playOrderService;

    @RequiresPermissions("file:manage:view")
    @GetMapping()
    public String files() {
        return prefix + "/file";
    }

    /**
     * 查询文件上传列表
     */
    @RequiresPermissions("file:manage:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Files files) {
        startPage();
        List<Files> list = filesService.selectFilesList(files);
        return getDataTable(list);
    }

    /**
     * 查询文件上传列表(不带显示的)
     */
    @RequiresPermissions("file:manage:list")
    @PostMapping("/listWithNoSave")
    @ResponseBody
    public TableDataInfo listWithNoSave(Files files) {
        startPage();
        List<Files> list = filesService.selectFilesListNoSave(files);
        return getDataTable(list);
    }

    /**
     * 导出文件上传列表
     */
    @RequiresPermissions("file:manage:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Files files) {
        List<Files> list = filesService.selectFilesList(files);
        ExcelUtil<Files> util = new ExcelUtil<Files>(Files.class);
        return util.exportExcel(list, "files");
    }

    /**
     * 新增文件上传
     */
    @GetMapping("/add")
    public String add(ModelMap mmap) {
        Files files = new Files();
        mmap.put("files", files);
        return prefix + "/add";
    }

    /**
     * 新增保存文件上传
     */
    @RequiresPermissions("file:manage:add")
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult save(MultipartFile file, Files files) {
        int rtn = 0;
        Boolean isFile = false;
        try {
            files = dealFile(file, files);
            if (files.getUpdateFlag() == 1 && files.getId() > 0) {//修改
                if (file != null) {
                    isFile = true;
                }
                rtn = filesService.updateFiles(files, isFile);
            } else {//新增
                rtn = filesService.insertFiles(files);
            }
            if (file != null && !"2".equals(files.getType())) {
                File desc = FileUploadUtils.getAbsoluteFile(Save_Url, files.getUrl());
                file.transferTo(desc);
            }
        } catch (Exception e) {
            log.error("保存失败，请检查后重试！", e);
            return error(e.getMessage());
        }
        return toAjax(rtn);
    }


    /**
     * 新增保存文件上传
     */
    @RequiresPermissions("file:manage:add")
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/addFileToShow")
    @ResponseBody
    public AjaxResult addFileToShow(String list, Long channelId) {
        String[] ids = list.split(",");
        int rtn = 0;
        if (StringUtils.isEmpty(ids) || ids.length < 1) {
            throw new BaseException("文件不能为空！");
        }
        int order = 0;
        PlaysOrder lastModel = playOrderService.selectMaxLimit(channelId);
        if (lastModel != null) {//取到最大的排序
            order = lastModel.getOrderBy();
        }
        for (int i = 0; i < ids.length; i++) {
            order = order + 1;
            Files files = filesService.selectFilesById(Integer.valueOf(ids[i]));
            if (files == null) {
                throw new BaseException("文件已被删除！");
            }
            PlaysOrder playsOrder = getOrderModel(files, channelId);
            if (playsOrder != null ) {
                playsOrder.setOrderBy(order);
                rtn = playOrderService.insertOrder(playsOrder);
            }

        }
        return toAjax(rtn);
    }

    private PlaysOrder getOrderModel(Files files, Long channelId) {
        PlaysOrder playsOrder = new PlaysOrder();
        playsOrder.setChannelId(channelId);
        playsOrder.setContent(files.getContent());
        playsOrder.setFileName(files.getFileName());
        playsOrder.setUr(files.getUrl());
        playsOrder.setTypes(Integer.valueOf(files.getType()));
        playsOrder.setSuffix(files.getSuffix());
        return playsOrder;
    }


    /**
     * 修改文件View
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, ModelMap mmap) {
        Files files = filesService.selectFilesById(id);
        mmap.put("files", files);
        String fileUrl = "/profile/" + files.getFileName() + "." + files.getSuffix();
        mmap.put("fileUrl", fileUrl);
        return prefix + "/add";
    }

    /**
     * 修改保存文件上传
     */
    @RequiresPermissions("file:manage:edit")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(MultipartFile file, Files files) {
        return toAjax(filesService.updateFiles(files, false));
    }

    /**
     * 删除文件上传
     */
    @RequiresPermissions("file:manage:remove")
    @Log(title = "文件上传", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(filesService.deleteFilesByIds(ids));
    }

    @RequestMapping("/listOrder")
    @ResponseBody
    public TableDataInfo getShow(PlaysOrder playOrder) {
        List<PlaysOrder> playOrderList = playOrderService.selectOrderList(playOrder);
        return getDataTable(playOrderList);
    }

    @RequestMapping("/listOrderPage")
    public String listOrder() {
        return prefix + "/orderShow";
    }

    /**
     * 检验文件名是否唯一
     *
     * @param files
     * @return
     */
    @PostMapping("/checkFileNameUnqiue")
    @ResponseBody
    public String checkFileNameUnqiue(Files files) {
        return filesService.checkFileNameUnqiue(files);
    }

    private Files dealFile(MultipartFile file, Files files) throws Exception {
        if (file == null || "2".equals(files.getType())) return files;

        String suffix = FileUploadUtils.dealName(file.getOriginalFilename());
        if (StringUtils.isEmpty(suffix)) throw new Exception();

        String name = Save_Url + files.getFileName() + "." + suffix;
        files.setUrl(name);
        files.setSuffix(suffix);

        return files;
    }

    @RequestMapping("/show/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        if (id != null && id > 0){
            model.addAttribute("id",id);
        }
        return prefix + "/lunbo";
    }
}
