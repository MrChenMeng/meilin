package com.ruoyi.product.service;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.product.domain.Standard;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 检测标准
 *
 * @author TYw
 * @date 2019/8/10 16:04
 */
public interface IStandardService {
	int removeByIds(String ids);

	int removeById(Long id,Long productId,Long checkId,HttpServletRequest req);

	int insert(Standard standard);

	List<Standard> select(Standard standard);

	List<Standard> selectByProductIds(String idStr);

	Standard selectById(Long id);

	int update(Standard standard,HttpServletRequest request);

	int insertList(Standard standard, List<Standard> list, HttpServletRequest request);

	Message editCheckState(Long productId, Long checkId, String status);

	Message checkDetected(List<Standard> detectedData);

	Message template(List<Standard> detectedData);

	List<Standard> selectByProductId(Long productId);

}
