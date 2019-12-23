package com.myrobot.org.common;

import com.myrobot.org.buy.model.ScheduleModel;

/**
 * 出货补货状态码
 * @author Lixingxing
 */
public enum Substates {
    no10000(10000, "出货空闲"),
    no10001(10001, "出货前下降电梯"),
    no10002(10002, "出货切换滑轨电机"),
    no10003(10003, "出货旋转转盘"),
    no10004(10004, "出货电梯上升货物"),
    no10005(10005, "出货电梯到位,等待取走货物"),
    no10006(10006, "出货失败"),
    no20000(20000, "补货空闲"),
    no20001(20001, "补货前打开后门"),
    no20002(20002, "补货提示切换货物补入"),
    no20003(20003, "补货旋转转盘"),
    no20004(20004, "补货等待短按按键确认补入"),
    no20005(20005, "补货等待长按按键补货完成"),
    no20006(20006, "补货完成,关闭后门");

    private int types;
    private String desc;

    Substates(int types, String desc) {
        this.types = types;
        this.desc = desc;
    }

    public static Substates getByTypes(int types) {
        for (Substates value : Substates.values()) {
            if (value.types == types) {
                return value;
            }
        }
        return Substates.no10000;
    }

    public String getDesc() {
        return desc;
    }
}
