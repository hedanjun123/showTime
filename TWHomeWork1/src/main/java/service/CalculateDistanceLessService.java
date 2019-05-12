package service;

/**
 * 计算从起始城市到终点城市、距离小于某个值的路线数
 *
 * @author: hdj
 * @date: 2019/5/12 22:40
 */
public class CalculateDistanceLessService extends  CalculateBaseService{

    public CalculateDistanceLessService(){
        super();
    }

    public boolean needAdd(int times, int distance) {
        if(distance < this.getMaxDistance()){
            return true;
        } else {
            return false;
        }
    }

    public boolean needNext(int times, int distance) {
        if(distance < this.getMaxDistance()){
            return true;
        } else {
            return false;
        }
    }

    protected String result(int totalCount) {
        return String.valueOf(totalCount);
    }
}
