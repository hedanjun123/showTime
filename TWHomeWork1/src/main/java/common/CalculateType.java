package common;

import service.CalculateDistanceMinService;
import service.CalculateNumEqualService;
import service.CalculateNumLessEqualService;
import service.CalculateDistanceLessService;

public enum CalculateType {
    RouteCountLessEqual ("路线次数小于等于某个值", CalculateNumLessEqualService.class),
    RouteCountEqual     ("路线次数等于某个值",     CalculateNumEqualService.class),
    RouteDistanceLess   ("路线距离小于某个值",     CalculateDistanceLessService.class),
    RouteDistanceMin    ("路线距离的最小值",       CalculateDistanceMinService.class),
    ;

    public String value;
    public Class clazz;

    CalculateType(String value, Class clazz){
        this.value = value;
        this.clazz = clazz;
    }
}
