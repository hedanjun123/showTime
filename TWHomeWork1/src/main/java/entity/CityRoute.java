package entity;

import common.CommonException;
import common.Constants;
import common.ErrorCode;
import service.CityService;
import util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 路线类
 *
 * @author: hdj
 * @date: 2019-05-09 12:57
 */
public class CityRoute {

    /**
     * 所有路线Map，key为路线唯一标识，value为路线对象
     */
    public static Map<String, CityRoute> cityRouteMap = new HashMap();

    /**
     * 路线起点城市
     */
    private City startCity;

    /**
     * 路线终点城市
     */
    private City endCity;

    /**
     * 距离
     */
    private Integer distance;

    /**
     * 构造方法
     * @param routeStr
     */
    public CityRoute(String routeStr) {
        if (StringUtils.isBlank(routeStr)) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线不能为空！");
        }
        routeStr = routeStr.trim();
        if (routeStr.length() < Constants.MIN_ROUTE_STR_LENGTH) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线"+routeStr+"]有误，请输入形如“AB5”这样的路线，并用“,”隔开！");
        }
        this.startCity = CityService.findCity(String.valueOf(routeStr.charAt(0)));
        this.endCity = CityService.findCity(String.valueOf(routeStr.charAt(1)));

        if (startCity.equals(endCity)) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线["+routeStr+"]的起始城市不能等于终点城市！");
        }
        //校验该路线是否存在
        String routeKey = this.routeKey();
        if (cityRouteMap.get(routeKey) != null) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线["+routeStr+"]的起始城市、终点城市对应的路线已存在！");
        }
        Integer distance = StringUtils.strToInteger(routeStr.substring(2));
        this.distance = distance;
        this.startCity.addCityRoute(this);
    }

    /**
     * 生成路线的唯一标识
     * @return
     */
    public String routeKey() {
        return this.startCity.getName()+this.endCity.getName();
    }

    /** get set 方法**/
    public City getStartCity() {
        return startCity;
    }

    public void setStartCity(City startCity) {
        this.startCity = startCity;
    }

    public City getEndCity() {
        return endCity;
    }

    public void setEndCity(City endCity) {
        this.endCity = endCity;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "CityRoute{" +
                "startCity=" + startCity.getName() +
                ", endCity=" + endCity.getName() +
                ", distance=" + distance +
                '}';
    }
}
