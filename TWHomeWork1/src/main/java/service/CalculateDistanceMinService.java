package service;

import entity.City;

import java.util.Map;

/**
 * 计算从起始城市到终点城市的最小距离
 *
 * @author: hdj
 * @date: 2019/5/12 22:56
 */
public class CalculateDistanceMinService extends CalculateBaseService{
    private int minDistance = Integer.MAX_VALUE;

    public boolean needAdd(int times, int distance) {
        if (distance < minDistance) {
            minDistance = distance;
            return true;
        } else {
            return false;
        }
    }

    public boolean needNext(int times, int distance) {
        if (distance < minDistance) {
            return true;
        } else {
            return false;
        }
    }

    protected String result(int totalCount) {
        return String.valueOf(minDistance);
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }
}
