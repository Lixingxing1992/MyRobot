package com.myrobot.org.buy.model;

import com.myrobot.org.common.SceneType;

import java.util.ArrayList;
import java.util.List;

/**
 * 出货补货状态
 *
 * @author Lixingxing
 */
public class ScheduleModel {
    /**
     * states : ship
     * substates : XXX
     * data : ["goods_code","floor","position","order_id"]
     */
    public String states;
    public String substates;
    public List<String> data = new ArrayList<>();

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getSubstates() {
        return substates;
    }

    public void setSubstates(String substates) {
        this.substates = substates;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
