package demo.kiscode.fileshare.util;

import android.app.Activity;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: kisCode
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

    public static boolean checkEndsWithSuffixArray(String suffix, String[] fileSuffixArray) {
        for (String targetSuffix : fileSuffixArray) {
            if (targetSuffix.toLowerCase().endsWith(suffix))
                return true;
        }
        return false;
    }

    /***
     * 获取指定文件夹下所有文件大小总和
     * @param dir 文件夹
     * @return
     */
    public static long getDirTotalSize(File dir) {
        if (dir == null) return 0;

        long size = 0;
        File[] files = dir.listFiles();
        if (files == null) return 0;
        for (File file : files) {
            if (file.isDirectory()) {
                size += getDirTotalSize(file);
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /***
     * 获取指定文件夹下所有文件
     * @param dir 文件夹
     * @return 文件集合
     */
    public static List<File> getAllFile(File dir) {
        List<File> fileList = new ArrayList<>();
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return fileList;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return fileList;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                fileList.addAll(getAllFile(file));
            } else {
                fileList.add(file);
            }
        }
        return fileList;
    }
}