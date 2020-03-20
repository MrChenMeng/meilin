package com.ruoyi.product.service.impl;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.product.constant.CheckStandardConstants;
import com.ruoyi.product.domain.CheckStandard;
import com.ruoyi.product.mapper.CheckStandardMapper;
import com.ruoyi.product.service.ICheckStandardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenm
 * @create 2019-08-14 14:06
 */
@Service
public class CheckStandardServiceImp implements ICheckStandardService {

    @Resource
    private CheckStandardMapper checkStandardMapper;

    @Override
    public List<CheckStandard> selectCheckList(CheckStandard checkStandard) {
        return checkStandardMapper.selectCheckList(checkStandard);
    }

    @Override
    public String checkCheckNameUnique(CheckStandard check) {
        Long id = StringUtils.isNull(check.getId()) ? -1L : check.getId();
        CheckStandard info = checkStandardMapper.checkCheckNameUnique(check.getCheckName(), check.getParentId());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != id.longValue())
        {
            return CheckStandardConstants.CHECK_NAME_NOT_UNIQUE;
        }
        return CheckStandardConstants.CHECK_NAME_UNIQUE;
    }

    @Override
    public int insertCheckStandard(CheckStandard checkStandard) {
        CheckStandard info = checkStandardMapper.selectCheckById(checkStandard.getParentId());
        checkStandard.setAncestors(checkStandard.getAncestors()+"," + checkStandard.getParentId());
        return checkStandardMapper.insertCheckStandard(checkStandard);
    }

    @Override
    public CheckStandard selectCheckStandardById(Long id) {
        return checkStandardMapper.selectCheckById(id);
    }

    @Override
    public int updateCheckStandard(CheckStandard checkStandard) {
        CheckStandard newCheck = checkStandardMapper.selectCheckById(checkStandard.getParentId());
        CheckStandard oldCheck = selectCheckStandardById(checkStandard.getId());
        if (StringUtils.isNotNull(newCheck) && StringUtils.isNotNull(oldCheck))
        {
            String newAncestors = newCheck.getAncestors() + "," + newCheck.getId();
            String oldAncestors = oldCheck.getAncestors();
            checkStandard.setAncestors(newAncestors);
            updateCheckChildren(checkStandard.getId(), newAncestors, oldAncestors);
        }
        int result = checkStandardMapper.updateCheckStandard(checkStandard);

        return result;
    }

    @Override
    public int selectCheckCount(Long id) {
        CheckStandard checkStandard = new CheckStandard();
        checkStandard.setParentId(id);
        return checkStandardMapper.selectCheckCount(checkStandard);
    }

    @Override
    public int deleteCheckById(Long id) {
        return checkStandardMapper.deleteCheckById(id);
    }


    public void updateCheckChildren(Long id, String newAncestors, String oldAncestors)
    {
        List<CheckStandard> children = checkStandardMapper.selectChildrenCheckById(id);
        for (CheckStandard child : children)
        {
            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
        }
        if (children.size() > 0)
        {
            checkStandardMapper.updateCheckChildren(children);
        }
    }
}
