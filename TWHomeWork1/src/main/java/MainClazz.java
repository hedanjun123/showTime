import common.Constants;
import entity.City;
import entity.CityRoute;
import service.CityRouteService;
import service.CityService;
import util.FileUtils;


/**
 * 程序入口
 *
 * @author: hdj
 * @date: 2019-05-09 11:44
 */
public class MainClazz {

    public static void main(String[] args){

        //1、获取文件的输入数据
        String inputStr = FileUtils.readTxt(Constants.FILE_PATH);

        //2、初始化初始数据
        CityRouteService.init(inputStr);

        //3、打印所有城市
        CityService.printAllCity();

        //问题1
        System.out.println("Output #1: "+CityService.calculateCityDistance("A","B","C"));

        //问题2
        System.out.println("Output #2: "+CityService.calculateCityDistance("A","D"));

        //问题3
        System.out.println("Output #3: "+CityService.calculateCityDistance("A","D","C"));

        //问题4
        System.out.println("Output #4: "+CityService.calculateCityDistance("A","E","B","C","D"));

        //问题5
        System.out.println("Output #5: "+CityService.calculateCityDistance("A","E","D"));

    }

}
