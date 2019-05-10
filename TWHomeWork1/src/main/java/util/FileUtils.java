package util;

import common.CommonException;
import common.ErrorCode;

import java.io.*;
import java.util.stream.Stream;

/**
 * 文件处理工具类
 *
 * @author: hdj
 * @date: 2019-05-09 13:01
 */
public class FileUtils {

    public static String readTxt(String filePath) {
        BufferedReader br = null;
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
                StringBuilder sb = new StringBuilder();
                String lineTxt;
                while ((lineTxt = br.readLine()) != null) {
                    //System.out.println(lineTxt);
                    sb.append(lineTxt);
                }
                br.close();
                return sb.toString();
            } else {
                throw new CommonException(ErrorCode.FILE_NOT_FOUND, filePath);
            }
        } catch (Exception e) {
            throw new CommonException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        } finally {
            closeStream(br);
        }
    }

    private static void closeStream(Closeable closeable) {
        if (closeable  != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new CommonException(ErrorCode.FAIL_CLOSE_FILE);
            }
        }
    }

}
