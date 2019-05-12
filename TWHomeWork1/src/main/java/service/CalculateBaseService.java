package service;

import common.Constants;
import entity.City;
import entity.Route;

import java.util.Set;

/**
 * description.
 *
 * @author: hdj
 * @date: 2019/5/12 20:27
 */
public abstract class CalculateBaseService {

    private City startCity;
    private City endCity;
    private int maxNum;
    private int maxDistance;

    public CalculateBaseService(){
    }

    /**
     * 计算
     * @return
     */
    public String calculate() {
        if (startCity == null || endCity == null) {
            return Constants.NO_SUCH_ROUTE;
        }
        int totalCount = 0;
        Set<Route> routes = startCity.getRoutes();
        for (Route route : routes) {
            int times = 1;
            int distance = route.getDistance();
            if (route.getEndCity().equals(endCity)) {
                if (needAdd(times, distance)) {
                    totalCount++;
                }
            }
            if (needNext(times, distance)) {
                totalCount += getTotalCount(route, times, distance);
            }
        }
        return result(totalCount);
    }

    /**
     * 查找城市路线
     * @param route
     * @param times
     * @return
     */
    private int getTotalCount(Route route, int times, int distance) {
        if (route== null) {
            return 0;
        }
        times++;
        if (times > maxNum) {
            return 0;
        }
        int totalCount = 0;
        City city = route.getEndCity();
        Set<Route> routes = city.getRoutes();
        for (Route routeTemp : routes) {
            int distanceTemp = routeTemp.getDistance()+distance;
            if (routeTemp.getEndCity().equals(endCity)) {
                if (needAdd(times, distanceTemp)) {
                    totalCount++;
                }
            }
            if (needNext(times, distanceTemp)) {
                totalCount += getTotalCount(routeTemp, times, distanceTemp);
            }
        }
        return totalCount;
    }

    /**
     * 是否累加路线数
     * @param times
     * @param distance
     * @return
     */
    public abstract boolean needAdd(int times, int distance);

    /**
     * 是否递归计算子路线
     * @param times
     * @param distance
     * @return
     */
    public abstract boolean needNext(int times, int distance);

    /**
     * 返回结果
     * @param totalCount
     * @return
     */
    protected abstract String result(int totalCount);

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

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }
}
