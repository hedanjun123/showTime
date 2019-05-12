package service;

/**
 * 计算从起始城市到终点城市、途径route等于某个值的路线数
 *
 * @author: hdj
 * @date: 2019/5/12 20:48
 */
public class CalculateNumEqualService extends CalculateBaseService{

    public CalculateNumEqualService(){
        super();
    }

    public boolean needAdd(int times, int distance) {
        if (times == this.getMaxNum()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean needNext(int times, int distance) {
        if(times < this.getMaxNum()){
            return true;
        } else {
            return false;
        }
    }

    protected String result(int totalCount) {
        return String.valueOf(totalCount);
    }
}
