package com.ruoyi.product.service.impl;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.UuidUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.domain.Standard;
import com.ruoyi.product.mapper.ProductMapper;
import com.ruoyi.product.mapper.StandardMapper;
import com.ruoyi.product.service.IStandardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StandardServiceImpl implements IStandardService {

	@Resource
	StandardMapper standardMapper;
	@Resource
	ProductMapper productMapper;


	@Override
	public int removeByIds(String ids) {
		Long[] sids = Convert.toLongArray(ids);
		int i = standardMapper.removeMaterialByIds(sids);
		return i;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public int removeById(Long id,Long productId,Long checkId,HttpServletRequest req) {
		List<Standard> standardList = (ArrayList)req.getSession().getAttribute("standardlist");
		if(standardList==null){
			int i = standardMapper.deleteByPrimaryKey(id);
			List<Standard> list = standardMapper.selectByProductId(productId);
			if(list.size()== 0){
				productMapper.removeCheckId(productId);
			}
			return i;
		}
		else{
			int i = 0;
			for(Standard standard:standardList){
				if(!standard.getId().equals(id)){
					standard.setProductId(productId);
					standard.setCheckId(checkId);
					standard.setCreateBy(ShiroUtils.getSysUser().getUserName());
					i = insert(standard);
				}
			}
			Product product = new Product();
			product.setProductId(productId);
			product.setCheckId(checkId);
			productMapper.updateProduct(product);
			req.getSession().removeAttribute("standardlist");
			return i;
		}
	}

	@Override
	public int insert(Standard standard) {
		standard.setUuid(UuidUtil.getUuid());
		standard.setCreateBy(ShiroUtils.getSysUser().getUserName());
		int i = standardMapper.insertSelective(standard);
		return i;
	}

	@Override
	public List<Standard> select(Standard standard) {
		List<Standard> list = standardMapper.selectAll(standard);
		return list;
	}

	@Override
	public List<Standard> selectByProductIds(String idStr) {
		Long[] productIds = Convert.toLongArray(idStr);
		return standardMapper.selectByProductIds(productIds);
	}

	@Override
	public Standard selectById(Long id) {
		Standard standard = standardMapper.selectByPrimaryKey(id);
		return standard;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public int update(Standard standard,HttpServletRequest request) {
		int i = 0;
		//判断是否是第一次
		//session中为空的话就是第二次
		if(request.getSession().getAttribute("standardlist")==null){
			i = standardMapper.updateByPrimaryKeySelective(standard);
		}
		//为空则第一次修改 将session中的数据插入
		else{
			List<Standard> standardList = (ArrayList)request.getSession().getAttribute("standardlist");
			List<Standard> list = standardMapper.selectByProductId(standard.getProductId());
			if(!list.isEmpty()){
				return -1;
			}
			for(Standard s:standardList){
				if(s.getId().equals(standard.getId())){
					if(standard.getRemark()!=null){
						s.setRemark(standard.getRemark());
					}
					if(standard.getLowerNumber()!=null){
						s.setLowerNumber(standard.getLowerNumber());
					}
					if(standard.getUpperNumber()!=null){
						s.setUpperNumber(standard.getUpperNumber());
					}
					if(standard.getDetectedNumber()!=null){
						s.setDetectedNumber(standard.getDetectedNumber());
					}
					if(standard.getStandardNumber()!=null){
						s.setStandardNumber(standard.getStandardNumber());
					}
					if(standard.getStandardName()!=null){
						s.setStandardName(standard.getStandardName());
					}
					s.setProductId(standard.getProductId());
					s.setCheckId(standard.getCheckId());
					s.setCreateBy(ShiroUtils.getSysUser().getUserName());
					i = standardMapper.insertSelective(s);
				}else{
					s.setProductId(standard.getProductId());
					s.setCheckId(standard.getCheckId());
					s.setCreateBy(ShiroUtils.getSysUser().getUserName());
					i= standardMapper.insertSelective(s);
				}
			}
			request.getSession().removeAttribute("standardlist");
			if(i!=-1 && i!=0){
				Product product = new Product();
				product.setProductId(standard.getProductId());
				product.setCheckId(standard.getCheckId());
				productMapper.updateProduct(product);
			}
		}
		return i;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public int insertList(Standard standard, List<Standard> list, HttpServletRequest request) {
		standard.setUuid(UuidUtil.getUuid());
		int i = 0;
		standardMapper.insertSelective(standard);
		if(null != list){
			for(Standard _standard : list){
				standard.setUuid(_standard.getUuid());
				List<Standard> standards = standardMapper.selectAll(standard);
				if(standards.size() == 0){
					_standard.setProductId(standard.getProductId());
					_standard.setCheckId(standard.getCheckId());
					_standard.setCreateBy(ShiroUtils.getSysUser().getUserName());
					standardMapper.insertSelective(_standard);
				}else{
					continue;
				}
			}
		}
		i =1;
		Product product = productMapper.selectProductById(standard.getProductId());
		product.setCheckId(standard.getCheckId());
		productMapper.updateProduct(product);
		HttpSession session = request.getSession();
		session.removeAttribute("standardlist");
		return i;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Message editCheckState(Long productId, Long checkId, String status) {
		Message msg = new Message();
		Product product = new Product();
		product.setProductId(productId);
		product.setCheckStatus(status);
		Product product1 = productMapper.selectProductById(productId);
		List<Standard> standardList = standardMapper.selectByProductId(productId);
		if(standardList.isEmpty()){
			msg.setCode(Message.getError());
			msg.setMsg("该计划单无检测项目，该表数据为模板");
			return msg;
		}
		if(!product1.getCheckId().equals(checkId)){
			msg.setCode(Message.getUpdate());
			msg.setMsg("该计划单已经检测");
			return msg;
		}
		product.setCheckId(checkId);
		productMapper.updateProduct(product);
		msg.setCode(Message.getSuccess());
		msg.setMsg("操作成功");
		return msg;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Message checkDetected(List<Standard> detectedData) {
		Message msg = new Message();
		msg.setCode(Message.getSuccess());
		msg.setMsg("操作成功");
		if(detectedData.isEmpty()){
			msg.setCode(Message.getError());
			msg.setMsg("数据不能为空");
		}else{
			List<Standard> standardList = standardMapper.selectByProductId(detectedData.get(0).getProductId());
			if(standardList.isEmpty()){
				for(Standard _S : detectedData){
					if(StringUtils.isBlank(_S.getUuid())){
						_S.setUuid(UuidUtil.getUuid());
					}
					standardMapper.insertSelective(_S);
				}
			}else{
				//差集（数据库有的，页面没有.删除一行操作）
				List<Standard> list1 = standardList.stream()
						.filter(item -> !detectedData.stream()
						.map(e -> e.getUuid())
						.collect(Collectors.toList())
						.contains(item.getUuid()))
						.collect(Collectors.toList());
				for(Standard _S:list1){
					standardMapper.deleteByPrimaryKey(_S.getId());
				}
				//差集（页面有的，数据库没有，新增一行操作）
				List<Standard> list2 = detectedData.stream()
						.filter(item -> !standardList.stream()
						.map(e -> e.getUuid())
						.collect(Collectors.toList())
						.contains(item.getUuid()))
						.collect(Collectors.toList());
				for(Standard _S:list2){
					_S.setUuid(UuidUtil.getUuid());
					standardMapper.insertSelective(_S);
				}
				//交集
				List<Standard> list3 = detectedData.stream()
						.filter(item ->standardList.stream()
						.map(e -> e.getUuid())
						.collect(Collectors.toList())
						.contains(item.getUuid()))
						.collect(Collectors.toList());
				for(Standard _S:list3){
					standardMapper.updateByPrimaryKeySelective(_S);
				}
			}
			Product product = productMapper.selectProductById(detectedData.get(0).getProductId());
			product.setCheckId(detectedData.get(0).getCheckId());
			product.setCheckStatus("2");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			product.setCheckTime(sdf.format(new Date()));
			productMapper.updateProduct(product);
		}
		return msg;
	}

	@Override
	public Message template(List<Standard> detectedData) {
		Message msg = new Message();
		msg.setCode(Message.getSuccess());
		msg.setMsg("操作成功");
		if(detectedData.isEmpty()){
			msg.setCode(Message.getError());
			msg.setMsg("数据不能为空");
		}else{
			standardMapper.deleteByTemplate(detectedData.get(0).getCheckId());
			for(Standard standard : detectedData){
				standard.setUuid(UuidUtil.getUuid());
				insert(standard);
			}
		}
		return msg;
	}

	@Override
	public List<Standard> selectByProductId(Long productId) {
		return standardMapper.selectByProductId(productId);
	}

}
