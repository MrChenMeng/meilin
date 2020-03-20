package com.ruoyi.product.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.UuidUtil;
import com.ruoyi.product.domain.Procedure;
import com.ruoyi.product.mapper.ProcedureMapper;
import com.ruoyi.product.service.IProcedureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenm
 * @create 2019-09-10 16:49
 */
@Service
public class ProcedureServiceImpl implements IProcedureService {

    @Resource
    private ProcedureMapper procedureMapper;

    @Override
    public Procedure selectProcedureById(Long id) {
        Procedure procedure = procedureMapper.selectProcedureById(id);
        return procedure;
    }

    @Override
    public List<Procedure> selectProcedure(Procedure procedure) {
        List<Procedure> procedures =  procedureMapper.selectProcedure(procedure);
        return procedures;
    }

    @Override
    public int removeProcedureByIds(String idStr) {
        Long[] ids = Convert.toLongArray(idStr);
        int i = procedureMapper.removeProcedureByIds(ids);
        return i;
    }

    @Override
    public int insertProcedure(Procedure procedure) {
        String uuid = UuidUtil.getUuid();
        procedure.setUuid(uuid);
        int i = procedureMapper.insertProcedure(procedure);
        return i;
    }

    @Override
    public int updateProcedure(Procedure procedure) {
        return procedureMapper.updateProcedure(procedure);
    }
}
