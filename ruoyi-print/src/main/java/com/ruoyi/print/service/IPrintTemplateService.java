package com.ruoyi.print.service;

import com.ruoyi.print.domain.PrintTemplate;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-17 15:02
 */
public interface IPrintTemplateService {

    public PrintTemplate selectPrintTemplateById(Long id);

    public List<PrintTemplate> selectPrintTemplateList(PrintTemplate printTemplate);

    public int insertPrintTemplate(PrintTemplate printTemplate);

    public int updatePrintTemplate(PrintTemplate printTemplate);

    public int deletePrintTemplateById(Long id);

    public int deletePrintTemplateByIds(String ids);

}
