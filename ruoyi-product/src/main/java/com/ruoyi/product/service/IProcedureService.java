package com.ruoyi.product.service;

import com.ruoyi.product.domain.Procedure;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-10 16:49
 */
public interface IProcedureService {

    Procedure selectProcedureById(Long id);

    List<Procedure> selectProcedure(Procedure procedure);

    int removeProcedureByIds(String idStr);

    int insertProcedure(Procedure procedure);

    int updateProcedure(Procedure procedure);

}
