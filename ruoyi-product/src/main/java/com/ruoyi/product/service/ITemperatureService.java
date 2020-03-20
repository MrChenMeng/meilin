package com.ruoyi.product.service;

import com.ruoyi.product.domain.Temperature;
import com.ruoyi.product.entity.Search;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author chenm
 * @create 2019-08-02 10:19
 */
public interface ITemperatureService {

    /**
     * 新增
     */
    public int insertTemperature(Temperature temperature);


    /**
     * 查询产品管理数据
     */
    public List<Temperature> selectTemperatureList(Temperature temperature);


    public Temperature selectTemperatureById(Long lineId);


    /**
     * 修改信息
     */
    public int updateTemperature(Temperature temperature);


    public int deleteTemperatureByIds(String ids) throws Exception;

    public List<Temperature> selectByIds(String ids);

    public int updateTemperatures(Search search);

    int  insertListTemp(List<Temperature> temperatureList,Long productId);

    Temperature selectTemperatureByLineIdLastTime(Long lineId);

    List<Temperature> selectTemperatureTemplate(Long lineId);

}
