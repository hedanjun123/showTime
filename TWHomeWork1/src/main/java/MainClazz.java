import common.CalculateType;
import common.Constants;
import service.StaticService;
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
        StaticService.init(inputStr);

        //3、打印所有城市
        StaticService.printAllCity();

        //问题1
        System.out.println("Output #1: "+StaticService.calculateCityDistance("A","B","C"));

        //问题2
        System.out.println("Output #2: "+StaticService.calculateCityDistance("A","D"));

        //问题3
        System.out.println("Output #3: "+StaticService.calculateCityDistance("A","D","C"));

        //问题4
        System.out.println("Output #4: "+StaticService.calculateCityDistance("A","E","B","C","D"));

        //问题5
        System.out.println("Output #5: "+StaticService.calculateCityDistance("A","E","D"));

        //问题6
        System.out.println("Output #6: "+StaticService.calculateRouteCountOrDistance("C","C", 3, Integer.MAX_VALUE, CalculateType.RouteCountLessEqual));

        //问题7
        System.out.println("Output #7: "+StaticService.calculateRouteCountOrDistance("A","C", 4, Integer.MAX_VALUE, CalculateType.RouteCountEqual));

        //问题8
        System.out.println("Output #8: "+StaticService.calculateRouteCountOrDistance("A","C", Integer.MAX_VALUE, Integer.MAX_VALUE, CalculateType.RouteDistanceMin));

        //问题9
        System.out.println("Output #9: "+StaticService.calculateRouteCountOrDistance("B","B", Integer.MAX_VALUE, Integer.MAX_VALUE, CalculateType.RouteDistanceMin));

        //问题10
        System.out.println("Output #10: "+StaticService.calculateRouteCountOrDistance("C","C", Integer.MAX_VALUE, 30, CalculateType.RouteDistanceLess));


    }

}
