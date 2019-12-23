package com.myrobot.org.common;

/**
 * 机器状态码
 * @author Lixingxing
 */
public enum SceneType {
    launched("launched","启动"),
    initializing("initializing","初始化中"),
    initialized("initialized","初始化成功"),
    outoforder("outoforder","故障"),
    none("none","无场景"),
    setting("setting","设置"),
    updating("updating","更新中"),
    gocharging("gocharging","回充中"),
    charging("charging","充电中"),
    settled("settled","定点（可被交互打断）"),
    settled_interacting("settled_interacting","定点-交互（非移动中，有人发生交互，但还未进行购买）"),
    settled_selling("settled_selling","定点-售货（非移动中，有人进行购买）"),
    patrolling("patrolling","巡逻（移动中, 可被交互打断）"),
    patrolling_interacting("patrolling_interacting","巡逻-交互（非移动中，有人在交互，但还未进行购买）"),
    patrolling_selling("patrolling_selling","巡逻-售货（非移动中，有人在购买）"),
    patrolling_gocharging("patrolling_gocharging","巡逻-回充中（移动中， 不可被交互打断）"),
    patrolling_charging("patrolling_charging","巡逻-充电中（非移动中）");

    private String types;
    private String desc;
    SceneType(String types,String desc){
        this.types = types;
        this.desc = desc;
    }

    public SceneType getByTypes(String types){
        for (SceneType value : SceneType.values()) {
            if( value.types.equals(types)){
                return value;
            }
        }
        return SceneType.none;
    }

    public String getDesc(){
        return desc;
    }
}
