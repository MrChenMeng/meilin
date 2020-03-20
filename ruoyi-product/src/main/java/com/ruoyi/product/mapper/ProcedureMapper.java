package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.Procedure;

import java.util.List;

/**
 * @author chenm
 * @create 2019-09-10 16:48
 */
public interface ProcedureMapper {

    Procedure selectProcedureById(Long id);

    List<Procedure> selectProcedure(Procedure Procedure);

    int removeProcedureByIds(Long[] ids);

    int insertProcedure(Procedure Procedure);

    int updateProcedure(Procedure Procedure);
}
