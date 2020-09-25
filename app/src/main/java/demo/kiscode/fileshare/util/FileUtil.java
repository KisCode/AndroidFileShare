package demo.kiscode.fileshare.util;

/**
 * Description:
 * Author: kanjianxiong
 * Date : 2020/9/21 16:49
 **/
public class FileUtil {


    // 获取文件扩展名
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }
} 