package com.ruoyi.product.service.impl;

import com.ruoyi.common.core.page.Message;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.UuidUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.product.domain.*;
import com.ruoyi.product.enums.MeterEnum;
import com.ruoyi.product.mapper.*;
import com.ruoyi.product.service.IMaterialService;
import com.ruoyi.product.service.IProductService;
import com.ruoyi.product.service.ITemperatureService;
import com.ruoyi.product.unit.TransUtil;
import com.ruoyi.product.unit.UtilMethod;
import com.ruoyi.system.domain.SysDictData;
import com.ruoyi.system.mapper.SysDictDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.beans.Transient;
import java.lang.management.MemoryType;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private IMaterialService materialService;
    @Resource
    private ProductLineMapper productLineMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductHistoryMapper productHistoryMapper;
    @Resource
    private TemperatureMapper temperatureMapper;
    @Resource
    private SysDictDataMapper dictDataMapper;

    @Resource
    private QRCodeMapper codeMapper;

    @Autowired
    private ITemperatureService temperatureService;
    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private ProductPlanMapper planMapper;


    @Override
    public List<Product> selectProductList(Product product) {
        List<Product> list = productMapper.selectProductList(product);
        for (Product p : list) {
            if (p.getProductAddNum() == null) {
                p.setProductAddNum(0D);
            }
            Double completeSchedule = p.getProductAddNum() / p.getProductNum();
            BigDecimal b = new BigDecimal(completeSchedule);
            completeSchedule = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
            p.setCompleteSchedule(completeSchedule);
            String completeScheduleView = (String.valueOf(completeSchedule * 100) + "%");
            p.setCompleteScheduleView(completeScheduleView);
            Material material = new Material();
            material.setProductId(p.getProductId());
            material.setProductNo(p.getProductNo());
            material.setType("1");
            Integer materialCount = materialMapper.maxCount(material);
            p.setMaterialCount(null == materialCount ? 0 : materialCount);
            material.setType("2");
            Integer mixCount = materialMapper.maxCount(material);
            p.setMixCount(null == mixCount ? 0 : mixCount);
        }
        return list;
    }

    @Override
    public String selectTemperature(Long productId) {
        String temperature = productMapper.selectTemperature(productId);
        return temperature;
    }

    @Override
    public int count(Product product) {
        return productMapper.count(product);
    }

    @Override
    public List<Product> productList(Product product) {
        return productMapper.productList(product);
    }

    @Override
    @Transactional
    public Message saveProduct(Long id, Double weight) {
        Message msg = new Message();
        MeterEnum[] meterEnums = MeterEnum.values();
        QRCode code = codeMapper.selectQRCodeById(id);
        if (code == null) {
            msg.setCode(Message.getError());
            msg.setMsg("没有此二维码信息");
            return msg;
        }
        if (code.getValid() == 2) {
            msg.setCode(Message.getError());
            msg.setMsg("已经报工");
            return msg;
        }
        Product product = productMapper.selectProductByProductNo(code.getProductNo());
        List<SysDictData> dictDatas = dictDataMapper.selectDictDataByType("sys_product_unit");
        String codeUnit = "";
        String productUnit = "";
        int codeValue = 0;
        int productValue = 0;
        for (SysDictData dictData : dictDatas) {
            Message _m = TransUtil.trans(dictData.getDictLabel());
            if (_m.getCode() == Message.getSuccess()) {
                if (code.getUnit().equals(dictData.getDictValue())) {
                    codeUnit = dictData.getDictLabel();
                }
                if (product.getProductUnit().equals(dictData.getDictValue())) {
                    productUnit = dictData.getDictLabel();
                }
            } else {
                throw new BusinessException("数据字典的" + dictData.getDictLabel() + _m.getMsg());
            }
        }
        for (MeterEnum meterEnum : meterEnums) {
            if (codeUnit.equals(meterEnum.getKey())) {
                codeValue = meterEnum.getValue();
            }
            if (productUnit.equals(meterEnum.getKey())) {
                productValue = meterEnum.getValue();
            }
        }
        code.setValid(2);
        code.setSweepDate(new Date());
        code.setChengZhong(weight);
        code.setUpdateBy(ShiroUtils.getSysUser().getUserName());
        codeMapper.updateQRCode(code);
        weight = weight * codeValue / productValue + product.getProductAddNum();
        product.setProductAddNum(weight);
        productMapper.updateProduct(product);
        msg.setCode(Message.getSuccess());
        msg.setMsg("操作成功");
        return msg;
    }

    @Override
    public List<Product> capacity(Product product) {
        return productMapper.capacity(product);
    }

    @Override
    public List<Product> speed(Product product) {
        return productMapper.speed(product);
    }

    /**
     * 新增
     *
     * @param product
     */
    @Override
    @Transactional
    public Long insertProduct(Product product) throws Exception {
        Date date = new Date();
        String[] lineIdArr = product.getLineIds().split(",");
        Long productId = 0L;
        for (String lineId : lineIdArr) {
            ProductPlan plan = planMapper.selectByPlanId(product.getProductCode());
            if (!plan.getPlanName().equals(product.getProductName())) {
                throw new Exception("不支持修改产品名称");
            }
            String comment_code = "";
            String max_code = productMapper.getMaxProductNo(product);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); // 时间字符串产生方式 2090807
            String uid_pfix = format.format(date);
            if (!StringUtils.isBlank(max_code)) {
                int endNum = Integer.parseInt(max_code);//转为int类型 1
                int tmpNum = 100000 + endNum + 1;//订单号+1变成10002
                comment_code = uid_pfix + UtilMethod.subStr("" + tmpNum, 1);//把首位的1 去掉 再拼成2019080700002
            } else {
                comment_code = uid_pfix + "00001";
            }
            String uuid = UuidUtil.getUuid();
            product.setUuid(uuid);


            if (lineId.equals("1") || lineId.equals("2") || lineId.equals("3")) {
                String preStr = comment_code.substring(0, 8);
                String subStr = comment_code.substring(9);
                comment_code = preStr + 1 + subStr;
            }
            if (lineId.equals("4") || lineId.equals("5") || lineId.equals("6")) {
                String preStr = comment_code.substring(0, 8);
                String subStr = comment_code.substring(9);
                comment_code = preStr + 2 + subStr;
            }


            product.setProductNo(comment_code);//设置产品单号
            Long _lineId = Long.valueOf(lineId);
            product.setLineId(_lineId);
            product.setCreateBy(ShiroUtils.getUserId().toString());
            productMapper.insertProduct(product);
            Product _product = productMapper.selectProductByUuid(uuid);
            productId = _product.getProductId();
            //增加溫度
            Temperature temperature = new Temperature();
            temperature.setLineId(_lineId);
            temperature.setProductId(null);
            List<Temperature> temperatures = temperatureMapper.selectTemperatureListProductIdIsNull(temperature);
            for (Temperature _T : temperatures) {
                _T.setTeId(null);
                _T.setProductId(_product.getProductId());
                _T.setCreateBy(ShiroUtils.getUserId().toString());
                temperatureMapper.insertTemperature(_T);
            }
            DateFormat df2 = DateFormat.getDateTimeInstance();//可以精确到时分秒
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date updateTime = sdf.parse(df2.format(date));
            /*
             * 增加助剂
             * */
            Material material = new Material();
            material.setType("1");
            material.setProductName(product.getProductName());

            List<Material> materialList = materialMapper.selectByProductNameAndType(material);
            for (Material _M : materialList) {
                _M.setUuid(UuidUtil.getUuid());
                _M.setProductId(product.getProductId());
                _M.setProductNo(product.getProductNo());
                _M.setCreateTime(product.getCreateTime());
                _M.setUpdateTime(product.getUpdateTime());
                _M.setFlag(false);
                _M.setCount(0);
                _M.setDosageActual(0.0);
                _M.setUpdateTime(updateTime);
                materialMapper.insertMaterial(_M);
            }
            /*
             * 增加拌和
             * */
            material.setType("2");
            material.setProductName(product.getProductName());
            List<Material> banhuoList = materialMapper.selectByProductNameAndType(material);
            for (Material _M : banhuoList) {
                _M.setUuid(UuidUtil.getUuid());
                _M.setProductId(product.getProductId());
                _M.setProductNo(product.getProductNo());
                _M.setCreateTime(product.getCreateTime());
                _M.setUpdateTime(product.getUpdateTime());
                _M.setFlag(false);
                _M.setCount(0);
                _M.setDosageActual(0.0);
                _M.setUpdateTime(updateTime);
                materialMapper.insertMaterial(_M);
            }
        }
        return productId;
    }

    @Override
    public int updateProduct(Product product) throws Exception {
        ProductPlan plan = planMapper.selectByPlanId(product.getProductCode());
        if (!plan.getPlanName().equals(product.getProductName())) {
            throw new Exception("不支持修改产品名称");
        }
        Product productOld = productMapper.selectProductById(product.getProductId());
        recordHistory(product, productOld);
        if (product.getLineId() != null && !productOld.getLineId().equals(product.getLineId())) {
            temperatureMapper.deleteByproductId(product.getProductId());
            //增加溫度
            Temperature temperature = new Temperature();
            temperature.setLineId(product.getLineId());
            temperature.setProductId(null);
            List<Temperature> temperatures = temperatureMapper.selectTemperatureListProductIdIsNull(temperature);
            for (Temperature _T : temperatures) {
                _T.setTeId(null);
                _T.setProductId(product.getProductId());
                _T.setCreateBy(ShiroUtils.getUserId().toString());
                temperatureMapper.insertTemperature(_T);
            }
        }
        String productNo = product.getProductNo();
        if(product.getLineId()==1L || product.getLineId()==2L ||product.getLineId()==3L){
            String preStr = productNo.substring(0, 8);
            String subStr = productNo.substring(9);
            productNo = preStr + 1 + subStr;
        }else{
            String preStr = productNo.substring(0, 8);
            String subStr = productNo.substring(9);
            productNo = preStr + 2 + subStr;
        }
        product.setProductNo(productNo);
        return productMapper.updateProduct(product);
    }

    @Override
    public Product selectProductById(Long productId) {
        return productMapper.selectProductById(productId);
    }

    @Override
    public Product selectProductByProductNo(String productNo) {
        return productMapper.selectProductByProductNo(productNo);
    }

    @Override
    @Transactional
    public int deleteProductByIds(String ids) {
        Long[] productIds = Convert.toLongArray(ids);
        Product product = productMapper.selectProductById(productIds[0]);
        materialMapper.deleteByProductId(product.getProductId());
        return productMapper.deleteProductByIds(productIds);
    }

    @Override
    public int editCheckState(Long id, String state) {
        return productMapper.editState(id, state);
    }

    public void recordHistory(Product product, Product productOld) {
        StringBuffer stringBuffer = new StringBuffer();
        //单位变更记录
        if (product.getProductUnit() != null && !product.getProductUnit().equals(productOld.getProductUnit())) {
            String oldUnit = dictDataMapper.selectDictLabel("sys_product_unit", productOld.getProductUnit());
            String newUnit = dictDataMapper.selectDictLabel("sys_product_unit", product.getProductUnit());
            stringBuffer.append("单位变更：").append(oldUnit).append("——>").append(newUnit).append(",");
        }
        //包装规格变更记录
        if (product.getProductSpecs() != null && !product.getProductSpecs().equals(productOld.getProductSpecs())) {
            String oldSpecs = dictDataMapper.selectDictLabel("product_produce_specs", productOld.getProductSpecs());
            String newSpecs = dictDataMapper.selectDictLabel("product_produce_specs", product.getProductSpecs());
            stringBuffer.append("包装规格变更：").append(oldSpecs).append("——>").append(newSpecs).append(",");
        }
        //产线变更记录
        if (product.getLineId() != null && !product.getLineId().equals(productOld.getLineId())) {
            String oldLine = productLineMapper.selectProductLineById(productOld.getLineId()).getLineName();
            String newLine = productLineMapper.selectProductLineById(product.getLineId()).getLineName();
            stringBuffer.append("产线变更：").append(oldLine).append("——>").append(newLine).append(",");
        }
        //产品变更记录
        if (product.getProductName() != null && !product.getProductName().equals(productOld.getProductName())) {
            stringBuffer.append("产品变更：").append(productOld.getProductName()).append("——>").append(product.getProductName()).append(",");
        }
        //销售单号变更记录
        if (product.getOrderNo() != null && !product.getOrderNo().equals(productOld.getOrderNo())) {
            stringBuffer.append("销售单号变更：").append(productOld.getOrderNo()).append("——>").append(product.getOrderNo()).append(",");
        }
        //生产数量变更记录
        if (product.getProductNum() != null && !product.getProductNum().equals(productOld.getProductNum())) {
            stringBuffer.append("生产数量变更：").append(productOld.getProductNum()).append("——>").append(product.getProductNum()).append(",");
        }
        //助剂用量变更记录
        if (product.getMaterialDosage() != null && !product.getMaterialDosage().equals(productOld.getMaterialDosage())) {
            stringBuffer.append("助剂用量变更：").append(productOld.getMaterialDosage()).append("——>").append(product.getMaterialDosage()).append(",");
        }
        //拌和用量变更记录
        if (product.getMixDosage() != null && !product.getMixDosage().equals(productOld.getMixDosage())) {
            stringBuffer.append("拌和用量变更：").append(productOld.getMixDosage()).append("——>").append(product.getMixDosage()).append(",");
        }
        //开始时间变更记录
        if (product.getStartTime() != null && !product.getStartTime().equals(productOld.getStartTime())) {
            stringBuffer.append("生产日期变更：").append(productOld.getStartTime()).append("——>").append(product.getStartTime()).append(",");
        }
        //结束日期。。。
        if (product.getEndTime() != null && !product.getEndTime().equals(productOld.getEndTime())) {
            stringBuffer.append("结束日期变更：").append(productOld.getEndTime()).append("——>").append(product.getEndTime()).append(",");
        }
        //拌和温度。。。
        if (product.getMixTemperature() != null && !product.getMixTemperature().equals(productOld.getMixTemperature())) {
            stringBuffer.append("拌合温度更变：").append(productOld.getMixTemperature()).append("——>").append(product.getMixTemperature()).append(",");
        }
		/*
		//拌和配方。。。
		if (product.getMixFormula() != null && !product.getMixFormula().equals(productOld.getMixFormula())) {
			String oldMix = dictDataMapper.selectDictLabel("product_mix_formula", productOld.getMixFormula());
			String newMix = dictDataMapper.selectDictLabel("product_mix_formula", product.getMixFormula());
			stringBuffer.append("拌合配方更变：").append(oldMix).append("——>").append(newMix).append(",");
		}
		//助剂配方。。。
		if (product.getMaterialFormula() != null && !product.getMaterialFormula().equals(productOld.getMaterialFormula())) {
			String oldMat = dictDataMapper.selectDictLabel("product_mix_formula", productOld.getMaterialFormula());
			String newMat = dictDataMapper.selectDictLabel("product_material_formula", product.getMaterialFormula());
			stringBuffer.append("拌合配方更变：").append(oldMat).append("——>").append(newMat).append(",");
		}
		*/
        if (stringBuffer.length() != 0) {
            ProductHistory productHistory = new ProductHistory();
            productHistory.setUpdateBy(ShiroUtils.getUserId().toString());
            productHistory.setProductNo(productOld.getProductNo());
            productHistory.setRemark(stringBuffer.toString());
            productHistoryMapper.insertSelective(productHistory);
        }
    }


    @Override
    @Transactional
    public Long copyProduct(Long id) throws Exception {
        Long productId = null;
        Date date = new Date();
        Product product = new Product();

        //先根据id获取订单信息selectProductList
        if (id == null || "".equals(id)) {
            throw new Exception("ID为空");
        }
        product = productMapper.selectProductById(id);
        if (product == null) {
            throw new Exception("ID没有订单信息");
        }
        //开始新增订单信息
        product.setCreateTime(date);
        product.setCreateUserId(ShiroUtils.getUserId());
        String comment_code = "";
        String max_code = productMapper.getMaxProductNo(product);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd"); // 时间字符串产生方式 2090807
        String uid_pfix = format.format(date);
        if (!StringUtils.isBlank(max_code)) {
            int endNum = Integer.parseInt(max_code);//转为int类型 1
            int tmpNum = 100000 + endNum + 1;//订单号+1变成10002
            comment_code = uid_pfix + UtilMethod.subStr("" + tmpNum, 1);//把首位的1 去掉 再拼成2019080700002
        } else {
            comment_code = uid_pfix + "00001";
        }
        if (product.getLineId()==1L || product.getLineId()==2L || product.getLineId()==3L) {
            String preStr = comment_code.substring(0, 8);
            String subStr = comment_code.substring(9);
            comment_code = preStr + 1 + subStr;
        }
        if (product.getLineId()==4L || product.getLineId()==5L || product.getLineId()==6L) {
            String preStr = comment_code.substring(0, 8);
            String subStr = comment_code.substring(9);
            comment_code = preStr + 2 + subStr;
        }
        String uuid = UuidUtil.getUuid();
        product.setUuid(uuid);//uuid
        product.setProductNo(comment_code);//设置产品单号
        Integer over = productMapper.insertProduct(product);
        //得到新增的订单id
        productId = product.getProductId();
        if (over > 0) {
            //先得到选定的订单温度信息
            Temperature temperature = new Temperature();
            temperature.setProductId(id);
            List<Temperature> temperatureList = temperatureService.selectTemperatureList(temperature);
            if (!temperatureList.isEmpty() || temperatureList != null) {
                //开始新增温度信息
                int isture = temperatureService.insertListTemp(temperatureList, productId);
                if (isture < 0) {
                    throw new Exception("新增温度信息失败");
                }
            }
            //得到拌料信息
            Material material = new Material();
            material.setProductId(id);
            List<Material> listMaterial = materialService.selectMaterial(material);
            if (!listMaterial.isEmpty()) {
                for(Material _M : listMaterial){
                    _M.setUuid(UuidUtil.getUuid());
                    _M.setProductId(productId);
                    _M.setProductNo(comment_code);
                    _M.setCreateTime(date);
                    _M.setUpdateTime(date);
                    materialMapper.insertMaterial(_M);
                }
            }
        }
        return productId;
    }

    @Override
    public int examine(Long productId) {
        return productMapper.examine(productId);
    }
}















