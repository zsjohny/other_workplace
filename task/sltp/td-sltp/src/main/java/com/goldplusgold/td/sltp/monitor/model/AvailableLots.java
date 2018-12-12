package com.goldplusgold.td.sltp.monitor.model;

import com.goldplusgold.td.sltp.monitor.conf.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户可用手数
 * Created by Administrator on 2017/5/15.
 */
public class AvailableLots {
    private String contractName;
    private Map<String, Integer> map = new HashMap<>();

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public void setBearLots(Integer availableLots){
        map.put(Constant.BEARBULL_BEAR, availableLots);
    }

    public void setBullLots(Integer availableLots){
        map.put(Constant.BEARBULL_BULL, availableLots);
    }

    public Integer getBearLots(){
        return map.get(Constant.BEARBULL_BEAR);
    }

    public Integer getBullLots(){
        return map.get(Constant.BEARBULL_BULL);
    }

}
