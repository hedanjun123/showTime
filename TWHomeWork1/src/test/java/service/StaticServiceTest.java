package service;

import common.CalculateType;
import common.Constants;
import entity.Route;
import org.junit.Assert;
import org.junit.Test;

public class StaticServiceTest {

    @Test
    public void init() {
        StaticService.init("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");
        Assert.assertTrue(StaticService.routeMap.size() == 9);
        Assert.assertTrue(StaticService.cityMap.size() == 5);
        Assert.assertTrue(StaticService.cityMap.get("A").getRoutes().size() == 3);
    }

    @Test
    public void initRoute() {
        Route route = StaticService.initRoute("AB5");
        Assert.assertTrue("A".equals(route.getStartCity().getName()));
        Assert.assertTrue("B".equals(route.getEndCity().getName()));
        Assert.assertTrue(5== route.getDistance());
    }

    @Test
    public void calculateCityDistance() {
        StaticService.init("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");
        Assert.assertTrue("9".equals(StaticService.calculateCityDistance("A","B","C")));
        Assert.assertTrue("5".equals(StaticService.calculateCityDistance("A","D")));
        Assert.assertTrue("13".equals(StaticService.calculateCityDistance("A","D","C")));
        Assert.assertTrue("22".equals(StaticService.calculateCityDistance("A","E","B","C","D")));
        Assert.assertTrue(Constants.NO_SUCH_ROUTE.equals(StaticService.calculateCityDistance("A","E","D")));
    }

    @Test
    public void calculateRouteCountOrDistance() {
        StaticService.init("AB5, BC4, CD8, DC8, DE6, AD5, CE2, EB3, AE7");
        Assert.assertTrue("2".equals(StaticService.calculateRouteCountOrDistance("C","C", 3, Integer.MAX_VALUE, CalculateType.RouteCountLessEqual)));
        Assert.assertTrue("3".equals(StaticService.calculateRouteCountOrDistance("A","C", 4, Integer.MAX_VALUE, CalculateType.RouteCountEqual)));
        Assert.assertTrue("9".equals(StaticService.calculateRouteCountOrDistance("A","C", Integer.MAX_VALUE, Integer.MAX_VALUE, CalculateType.RouteDistanceMin)));
        Assert.assertTrue("9".equals(StaticService.calculateRouteCountOrDistance("B","B", Integer.MAX_VALUE, Integer.MAX_VALUE, CalculateType.RouteDistanceMin)));
        Assert.assertTrue("7".equals(StaticService.calculateRouteCountOrDistance("C","C", Integer.MAX_VALUE, 30, CalculateType.RouteDistanceLess)));
    }

}