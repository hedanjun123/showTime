package service;

import common.CommonException;
import common.Constants;
import common.ErrorCode;
import entity.City;
import entity.CityRoute;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * description
 *
 * @author: hdj
 * @date: 2019-05-09 14:45
 */
public class CityService {

    /**
     * 根据城市名字，从CityMap查找city
     * @param name
     * @return
     */
    public static City findCity(String name){
        City city = City.cityMap.get(name);
        if (city == null) {
            city =new City(name);
            City.cityMap.put(name, city);
        }
        return city;
    }

    /**
     * 打印所有城市的信息
     */
    public static void printAllCity() {
        Set<Map.Entry<String,City>> entrySet = City.cityMap.entrySet();
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
            City city = City.cityMap.get(cityName);
            if(firstCity == null){
                firstCity = city;
                continue;
            }
            nexCity = city;
            CityRoute cityRoute = findCityRoute(firstCity, nexCity);
            if (cityRoute == null) {
                return Constants.NO_SUCH_ROUTE;
            }
            distance += cityRoute.getDistance();
            firstCity = nexCity;
        }
        return String.valueOf(distance);
    }

    /**
     * 寻找从cityA到cityB的路线
     * @param cityA
     * @param cityB
     * @return
     */
    public static CityRoute findCityRoute(City cityA, City cityB){
        if (cityA == null || cityB == null) {
            return null;
        }
        Set<CityRoute> routes = cityA.getRoutes();
        if (routes == null || routes.size()==0) {
            return null;
        }
        for (CityRoute route : routes) {
            if (route.getEndCity().getName().equals(cityB.getName())) {
                return route;
            }
        }
        return null;
    }
}
