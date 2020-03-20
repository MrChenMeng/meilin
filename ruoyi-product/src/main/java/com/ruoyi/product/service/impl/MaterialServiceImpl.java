package com.ruoyi.product.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.UuidUtil;
import com.ruoyi.product.domain.Material;
import com.ruoyi.product.domain.MaterialList;
import com.ruoyi.product.domain.Product;
import com.ruoyi.product.mapper.MaterialMapper;
import com.ruoyi.product.mapper.ProductHistoryMapper;
import com.ruoyi.product.mapper.ProductMapper;
import com.ruoyi.product.service.IMaterialService;
import com.ruoyi.system.mapper.SysDictDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements IMaterialService {

    @Resource
    private MaterialMapper materialMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private SysDictDataMapper dictDataMapper;
    @Resource
    private ProductHistoryMapper productHistoryMapper;

    @Override
    public int removeMaterialByIds(String ids) {
        Long[] mids = Convert.toLongArray(ids);
        int i = materialMapper.removeMaterialByIds(mids);
        return i;
    }


    @Override
    public int insertMaterial(Material material) {
        String uuid = UuidUtil.getUuid();
        material.setUuid(uuid);
        int i = materialMapper.insertMaterial(material);
        return i;
    }


    @Override
    public List<Material> selectMaterial(Material material) {
        List<Material> materialList = materialMapper.selectMaterial(material);

        return materialList;
    }

    @Override
    public Material selectMaterialById(Long mid) {
        Material material = materialMapper.selectMaterialById(mid);
        return material;
    }

    @Override
    public int updateMaterial(Material material) {
        return materialMapper.updateMaterial(material);
    }

    @Override
    public List<Material> mobileMaterial(Material material) {
        return materialMapper.mobileMaterial(material);
    }

    @Override
    @Transactional
    public Message saveMaterial(MaterialList materialList) {
        Message msg = new Message();
        Material material = new Material();
        if (null != materialList) {
            material.setType(materialList.getMaterialList().get(0).getType());
            material.setProductId(materialList.getMaterialList().get(0).getProductId());
            material.setUpdateTime(materialList.getMaterialList().get(0).getUpdateTime());
            List<Material> list = materialMapper.mobileSelectMaterials(material);
            if (list.isEmpty()) {
                materialMapper.removeMaterialwhenUpdate(material);
                msg.setCode(Message.getUpdate());
                msg.setMsg("助剂配方发生改变，请刷新页面获取新配方！！！");
                return msg;
            }
        }
        int count = materialMapper.maxCount(material);
        if(materialList.getMaterialList().get(0).getType().equals(1)){
            Product product = productMapper.selectProductById(materialList.getMaterialList().get(0).getProductId());
            if(count >= product.getMaterialDosage()){
                msg.setCode(Message.getUpdate());
                msg.setMsg("助剂缸数数量已完成，请勿上报！！！");
                return msg;
            }
        }
        if(materialList.getMaterialList().get(0).getType().equals(2)){
            Product product = productMapper.selectProductById(materialList.getMaterialList().get(0).getProductId());
            if(count >= product.getMixDosage()){
                msg.setCode(Message.getUpdate());
                msg.setMsg("拌和缸数数量已完成，请勿上报！！！");
                return msg;
            }
        }

        for (Material _M : materialList.getMaterialList()) {
            if (StringUtils.isEmpty(_M.getUuid())) {
                msg.setCode(Message.getError());
                msg.setMsg("uuid不能为空！！！");
                return msg;
            }
            if (_M.getProductId() == null) {
                msg.setCode(Message.getError());
                msg.setMsg("产品Id不能为空！！！");
                return msg;
            }
            if (null == _M.getDosageActual()) {
                msg.setCode(Message.getError());
                msg.setMsg("实际用量不能为空！！！");
                return msg;
            }
            Material material1 = materialMapper.selectMaterialByProductUUid(_M);
            material1.setMid(null);
            material1.setDosageActual(_M.getDosageActual());
            material1.setCount(count + 1);
            if (count == 0) {
                materialMapper.insertMaterial(material1);
            } else {
                materialMapper.updateMobileMaterial(material1);
            }
        }
        msg.setCode(Message.getSuccess());
        msg.setMsg("保存成功");
        return msg;
    }

    /**
     * @author: louk
     * @Description: :检查物料名称是否唯一
     * @date: 2019/9/20 14:38
     */
    @Override
    public String checkName(Material material) {
        //Long mId = StringUtils.isNull(material.getMid()) ? -1L : material.getMid();
        if (null != material.getName() && null != material.getProcedureId()) {
            List<Material> checkMaterial = materialMapper.checkName(material);
            if (StringUtils.isNotNull(checkMaterial) && checkMaterial.size() > 0) {
                return UserConstants.DICT_TYPE_NOT_UNIQUE;
            }

        }
        return UserConstants.DICT_TYPE_UNIQUE;
    }

    @Override
    @Transactional
    public Object insertMaterialList(List<Material> materialData, Message message) throws ParseException {
        if (materialData.size() == 0) {
            message.setCode(1);
            message.setMsg("数据不能为空");
        } else {
            for(int i=0; i< materialData.size(); i ++){
                materialData.get(i).setSort(i);
                materialMapper.updateMajor(materialData.get(i));
            }
            Date date = new Date();
            DateFormat df2 = DateFormat.getDateTimeInstance();//可以精确到时分秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date updateTime = sdf.parse(df2.format(date));

            Material material = new Material();
            material.setProductId(materialData.get(0).getProductId());
            material.setType(materialData.get(0).getType());
            List<Material> materialList = materialMapper.selectMaterial(material);
            if (materialList.size() == 0) {
                for (Material _M : materialData) {
                    if (StringUtils.isBlank(_M.getUuid())) {
                        _M.setUuid(UuidUtil.getUuid());
                    }
                    _M.setCount(0);
                    _M.setUpdateTime(updateTime);
                    materialMapper.insertMaterial(_M);
                }
            } else {
                //差集（数据库有的，页面没有.删除一行操作）
                List<Material> list1 = materialList.stream()
                        .filter(item -> !materialData.stream()
                                .map(e -> e.getUuid())
                                .collect(Collectors.toList())
                                .contains(item.getUuid()))
                        .collect(Collectors.toList());
                for (Material _M : list1) {
                    materialMapper.removeMaterialByMid(_M.getMid());
                }
                //差集（页面有的，数据库没有，新增一行操作）
                List<Material> list2 = materialData.stream()
                        .filter(item -> !materialList.stream()
                                .map(e -> e.getUuid())
                                .collect(Collectors.toList())
                                .contains(item.getUuid()))
                        .collect(Collectors.toList());
                for (Material _M : list2) {
                    _M.setUuid(UuidUtil.getUuid());
                    _M.setUpdateTime(updateTime);
                    _M.setCount(0);
                    materialMapper.insertMaterial(_M);
                }
                //交集
                List<Material> list3 = materialData.stream()
                        .filter(item -> materialList.stream()
                                .map(e -> e.getUuid())
                                .collect(Collectors.toList())
                                .contains(item.getUuid()))
                        .collect(Collectors.toList());
                for (Material _M : list3) {
                    if (_M.getUpdate() == 1 || list1.size() != 0 || list2.size() != 0) {
                        _M.setUpdateTime(updateTime);
                    } else {
                    }
                    Material _material = new Material();
                    _material.setUuid(_M.getUuid());
                    _material.setDrift(_M.getDrift());
                    _material.setType(_M.getType());
                    _material.setProductNo(_M.getProductNo());
                    materialMapper.updateDrift(_material);
                    materialMapper.updateMaterial(_M);
                }
            }
        }
        return message;
    }



    @Override
    public Object updatesign(Material material) {
        if (material.getMid() == null || "".equals(material.getMid())) {
            return "失败mid为空";
        }
        if (material.getMajor() == null || "".equals(material.getMajor())) {
            return "失败状态为空";
        }
        Integer istrue = materialMapper.updatesign(material);
        if (istrue > 0) {
            return "完成";
        }
        return "失败";
    }

}
