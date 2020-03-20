package com.ruoyi.product.service;

import com.ruoyi.product.domain.CheckStandard;

import java.util.List;

/**
 * @author chenm
 * @create 2019-08-14 14:04
 */
public interface ICheckStandardService {

    public List<CheckStandard> selectCheckList(CheckStandard checkStandard);

    public String checkCheckNameUnique(CheckStandard check);

    public int insertCheckStandard(CheckStandard checkStandard);

    public CheckStandard selectCheckStandardById(Long id);

    public int updateCheckStandard(CheckStandard checkStandard);

    public int selectCheckCount(Long id);

    public int deleteCheckById(Long id);
}
