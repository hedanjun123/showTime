package entity;

/**
 * 路线
 *  途径两个城市
 *
 * @author: hdj
 * @date: 2019-05-09 12:57
 */
public class Route {

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
     * @param startCity
     * @param endCity
     */
    public Route(City startCity, City endCity) {
        this(startCity, endCity, null);
    }

    /**
     * 构造方法
     * @param startCity
     * @param endCity
     * @param distance
     */
    public Route(City startCity, City endCity, Integer distance) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.distance = distance;
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
        return "Route{" +
                "startCity=" + startCity.getName() +
                ", endCity=" + endCity.getName() +
                ", distance=" + distance +
                '}';
    }
}
