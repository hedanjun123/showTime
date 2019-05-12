package entity;

import java.util.*;

/**
 * 城市类
 *
 * @author: hdj
 * @date: 2019-05-09 12:56
 */
public class City {

    /**
     * 城市名称
     */
    private String name;

    /**
     * 以当前城市为起点的路线
     */
    private Set<Route> routes;

//    /**
//     * 以当前城市为起点的“多节点路线”
//     */
//    private Set<RouteMultiple> routeMultiples;
//
//    /**
//     * 以当前城市为起点的循环路线
//     */
//    private Set<RouteMultipleCircle> routeMultipleCircles;

    /***
     * 构造方法
     * @param name
     */
    public City(String name) {
        this.name = name;
    }

    /**
     * 给城市增加路线
     * @param Route
     */
    public void addRoute(Route Route) {
        if (routes == null) {
            routes = new HashSet<Route>();
        }
        if (Route != null) {
            routes.add(Route);
        }
    }

    /** get set 方法**/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", routes=" + routes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        City city = (City) o;
        return city.name.equals(name);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (name == null ? 0 : name.hashCode());
        return result;
    }
}
