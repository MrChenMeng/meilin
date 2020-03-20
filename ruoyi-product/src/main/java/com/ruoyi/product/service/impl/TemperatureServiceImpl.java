package com.ruoyi.product.service.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.product.domain.Temperature;
import com.ruoyi.product.entity.Search;
import com.ruoyi.product.mapper.TemperatureMapper;
import com.ruoyi.product.service.ITemperatureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenm
 * @create 2019-08-02 10:19
 */
@Service
public class TemperatureServiceImpl implements ITemperatureService {

    @Resource
    private TemperatureMapper temperatureMapper;

    @Override
    public int insertListTemp(List<Temperature> temperatureList, Long productId) {
        return temperatureMapper.insertListTemp(temperatureList,productId);
    }

    @Override
    public Temperature selectTemperatureByLineIdLastTime(Long lineId) {
        return temperatureMapper.selectTemperatureByLineIdLastTime(lineId);
    }

    @Override
    public List<Temperature> selectTemperatureTemplate(Long lineId) {
        return temperatureMapper.selectTemperatureTemplate(lineId);
    }

    @Override
    public int insertTemperature(Temperature temperature) {
        String[] machineArr = temperature.getMachineTypes().split(",");
        temperature.setMachineCount(machineArr.length);
        Integer num = temperatureMapper.insertTemperature(temperature);
        return num;
    }

    @Override
    public List<Temperature> selectTemperatureList(Temperature temperature) {
        List<Temperature> list = temperatureMapper.selectTemperatureList(temperature);
        return list;
    }

    @Override
    public Temperature selectTemperatureById(Long teId) {
        return temperatureMapper.selectTemperatureById(teId);
    }


    @Override
    public int updateTemperature(Temperature temperature) {
        Integer num = temperatureMapper.updateTemperature(temperature);
        return num;
    }


    @Override
    public int deleteTemperatureByIds(String ids) throws Exception {
        Long[] lineIds = Convert.toLongArray(ids);

        return temperatureMapper.deleteTemperatureByIds(lineIds);
    }

    @Override
    public List<Temperature> selectByIds(String ids) {
        return temperatureMapper.selectByIds(ids);
    }

    @Override
    @Transactional
    public int updateTemperatures(Search search) {
        String[] ids = search.getIdStr().split(",");
        String[] temps = search.getTempStr().split(",");
        for(int i = 0 ;i < ids.length ; i ++){
            Temperature temp = temperatureMapper.selectTemperatureById(Long.valueOf(ids[i]));
            if(search.getKey().equals("up") && search.getWord().equals("avg")){
                temp.setTemperatureUpAvg(temps[i]);
            }else if(search.getKey().equals("up") && search.getWord().equals("act")){
                temp.setTemperatureUpAct(temps[i]);
//                temp.setTemperatureUpAvg(String.valueOf(Long.parseLong(temps[i])));
            }else if(search.getKey().equals("down") && search.getWord().equals("avg")){
                temp.setTemperatureDownAvg(temps[i]);
            }else if(search.getKey().equals("down") && search.getWord().equals("act")){
                temp.setTemperatureDownAct(temps[i]);
//                temp.setTemperatureDownAvg(String.valueOf(Long.parseLong(temps[i])));
            }
            temperatureMapper.updateTemperature(temp);
        }
        return 1;
    }
}
