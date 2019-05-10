package service;

import common.CommonException;
import common.Constants;
import common.ErrorCode;
import entity.CityRoute;
import util.StringUtils;

/**
 * description
 *
 * @author: hdj
 * @date: 2019-05-09 14:48
 */
public class CityRouteService {

    /**
     *  初始化所有路线
     * @param inputStr
     */
    public static void init(String inputStr) {
        try {
            if (StringUtils.isBlank(inputStr)) {
                throw new CommonException(ErrorCode.SYSTEM_ERROR, "文件内容不能为空!");
            }
            String[] inputStrArray = inputStr.split(Constants.SPLIT_STR);
            for (String routeStr : inputStrArray) {
                CityRoute cityRoute = new CityRoute(routeStr);
                CityRoute.cityRouteMap.put(cityRoute.routeKey(), cityRoute);
            }
        } catch (CommonException e) {
            System.out.println(e.getMsg());
        }
    }

}
