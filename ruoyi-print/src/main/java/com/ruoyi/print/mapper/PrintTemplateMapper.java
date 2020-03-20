package com.ruoyi.print.mapper;

import com.ruoyi.print.domain.PrintTemplate;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-17 15:00
 */
public interface PrintTemplateMapper {

    public PrintTemplate selectPrintTemplateById(Long id);

    public List<PrintTemplate> selectPrintTemplateList(PrintTemplate printTemplate);

    public int insertPrintTemplate(PrintTemplate printTemplate);

    public int updatePrintTemplate(PrintTemplate printTemplate);

    public int deletePrintTemplateById(Long id);

    public int deletePrintTemplateByIds(Long[] ids);

}
