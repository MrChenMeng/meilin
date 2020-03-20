package com.ruoyi.product.mapper;

import com.ruoyi.product.domain.CheckStandard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenm
 * @create 2019-08-14 14:07
 */
public interface CheckStandardMapper {

    public List<CheckStandard> selectCheckList(CheckStandard checkStandard);

    public CheckStandard checkCheckNameUnique(@Param("checkName")String checkName, @Param("parentId")Long parentId);

    public CheckStandard selectCheckById(Long parentId);

    public int insertCheckStandard(CheckStandard checkStandard);

    public List<CheckStandard> selectChildrenCheckById(Long id);

    public int updateCheckChildren( @Param("checks") List<CheckStandard> checks);

    public int updateCheckStandard(CheckStandard checkStandard);

    public int selectCheckCount(CheckStandard checkStandard);

    public int deleteCheckById(Long id);
}
