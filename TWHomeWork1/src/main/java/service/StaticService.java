package service;

import common.CalculateType;
import common.CommonException;
import common.Constants;
import common.ErrorCode;
import entity.City;
import entity.Route;
import util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * description
 *
 * @author: hdj
 * @date: 2019-05-09 14:45
 */
public class StaticService {

    /**
     * 所有城市信息Map，key为城市名，value为城市对象
     */
    public static Map<String, City> cityMap = new HashMap<String, City>();

    /**
     * 所有路线Map，key为路线唯一标识，value为路线对象
     */
    public static Map<String, Route> routeMap = new HashMap();

    /**
     *  初始化所有城市和路线
     * @param inputStr
     */
    public static void init(String inputStr) {
        try {
            if (StringUtils.isBlank(inputStr)) {
                throw new CommonException(ErrorCode.SYSTEM_ERROR, "文件内容不能为空!");
            }
            //初始化城市、线路
            String[] inputStrArray = inputStr.split(Constants.SPLIT_STR);
            for (String routeStr : inputStrArray) {
                Route route = initRoute(routeStr);
                routeMap.put(routeKey(route.getStartCity(), route.getEndCity()), route);
            }
        } catch (CommonException e) {
            System.out.println(e.getMsg());
        }
    }

    /**
     * 初始化路线
     * @param routeStr
     */
    public static Route initRoute(String routeStr) {
        if (StringUtils.isBlank(routeStr)) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线不能为空！");
        }
        routeStr = routeStr.trim();
        if (routeStr.length() < Constants.MIN_ROUTE_STR_LENGTH) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线"+routeStr+"]有误，请输入形如“AB5”这样的路线，并用“,”隔开！");
        }
        City startCity = StaticService.findCity(String.valueOf(routeStr.charAt(0)));
        City endCity = StaticService.findCity(String.valueOf(routeStr.charAt(1)));

        if (startCity.equals(endCity)) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线["+routeStr+"]的起始城市不能等于终点城市！");
        }
        //校验该路线是否存在
        String routeKey = routeKey(startCity, endCity);
        if (routeMap.get(routeKey) != null) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "路线["+routeStr+"]的起始城市、终点城市对应的路线已存在！");
        }
        Integer distance = StringUtils.strToInteger(routeStr.substring(2));
        Route route = new Route(startCity, endCity, distance);
        startCity.addRoute(route);
        return route;
    }

    /**
     * 生成路线的唯一标识
     * @return
     */
    public static String routeKey(City startCity, City endCity) {
        if (startCity == null || endCity == null) {
            return null;
        }
        return startCity.getName()+endCity.getName();
    }

    /**
     * 根据城市名字，从CityMap查找city
     * @param name
     * @return
     */
    public static City findCity(String name){
        City city = cityMap.get(name);
        if (city == null) {
            city =new City(name);
            cityMap.put(name, city);
        }
        return city;
    }

    /**
     * 打印所有城市的信息
     */
    public static void printAllCity() {
        Set<Map.Entry<String,City>> entrySet = cityMap.entrySet();
        for(Map.Entry<String,City> entry : entrySet){
            System.out.println(entry.getValue());
        }
    }

    /**
     * 计算多个城市之间的距离
     * @param cityNames
     * @return
     */
    public static String calculateCityDistance(String... cityNames){
        if (cityNames == null || cityNames.length < Constants.MIN_CITY_SIZE) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "请输入至少两个城市名称");
        }
        Integer distance = 0;
        City firstCity = null;
        City nexCity = null;
        for (String cityName : cityNames) {
            City city = cityMap.get(cityName);
            if(firstCity == null){
                firstCity = city;
                continue;
            }
            nexCity = city;
            Route route = findRoute(firstCity, nexCity);
            if (route == null) {
                return Constants.NO_SUCH_ROUTE;
            }
            distance += route.getDistance();
            firstCity = nexCity;
        }
        return String.valueOf(distance);
    }

    /**
     * 计算路线的个数
     * @param startCityName
     * @param endCityName
     * @param maxNum
     * @param maxDistance
     * @param calculateType
     * @return
     */
    public static String calculateRouteCountOrDistance(String startCityName, String endCityName, int maxNum, int maxDistance, CalculateType calculateType) {
        if (StringUtils.isBlank(startCityName) || StringUtils.isBlank(endCityName)) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, "城市名称不能为空");
        }
        try {
            City startCity = cityMap.get(startCityName);
            City endCity = cityMap.get(endCityName);
            if (startCity == null || endCity == null) {
                return Constants.NO_SUCH_ROUTE;
            }
            CalculateBaseService baseService = (CalculateBaseService)calculateType.clazz.newInstance();
            baseService.setStartCity(startCity);
            baseService.setEndCity(endCity);
            baseService.setMaxNum(maxNum);
            baseService.setMaxDistance(maxDistance);
            return baseService.calculate();
        } catch (InstantiationException e) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } catch (IllegalAccessException e) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }

    /**
     * 寻找从cityA到cityB的路线
     * @param cityA
     * @param cityB
     * @return
     */
    public static Route findRoute(City cityA, City cityB){
        if (cityA == null || cityB == null) {
            return null;
        }
        Set<Route> routes = cityA.getRoutes();
        if (routes == null || routes.size()==0) {
            return null;
        }
        for (Route route : routes) {
            if (route.getEndCity().getName().equals(cityB.getName())) {
                return route;
            }
        }
        return null;
    }

}
