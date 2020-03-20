package com.ruoyi.print.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.print.domain.PrintTemplate;
import com.ruoyi.print.mapper.PrintTemplateMapper;
import com.ruoyi.print.service.IPrintTemplateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenm
 * @create 2019-09-17 15:04
 */
@Service
public class PrintTemplateImpl implements IPrintTemplateService {

    @Resource
    private PrintTemplateMapper printTemplateMapper;

    @Override
    public PrintTemplate selectPrintTemplateById(Long id) {
        return printTemplateMapper.selectPrintTemplateById(id);
    }

    @Override
    public List<PrintTemplate> selectPrintTemplateList(PrintTemplate printTemplate) {
        return printTemplateMapper.selectPrintTemplateList(printTemplate);
    }

    @Override
    public int insertPrintTemplate(PrintTemplate printTemplate) {
        printTemplate.setCreateBy(ShiroUtils.getLoginName());
        return printTemplateMapper.insertPrintTemplate(printTemplate);
    }

    @Override
    public int updatePrintTemplate(PrintTemplate printTemplate) {
        return printTemplateMapper.updatePrintTemplate(printTemplate);
    }

    @Override
    public int deletePrintTemplateById(Long id) {
        return printTemplateMapper.deletePrintTemplateById(id);
    }

    @Override
    public int deletePrintTemplateByIds(String ids) {
        Long[] idArr = Convert.toLongArray(ids);
        return printTemplateMapper.deletePrintTemplateByIds(idArr);
    }
}
