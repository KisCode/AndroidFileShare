package demo.kiscode.fileshare.util;

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
        long size = 0;
        File[] files = dir.listFiles();
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
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                fileList.addAll(getAllFile(file));
            } else {
                fileList.add(file);
            }
        }
        return fileList;
    }


    // 关于文件后缀的常量
    public static final class FileSuffix {
        public static final String JPG = ".jpg";

        public static final String PNG = ".png";

        public static final String M4A = ".m4a";

        public static final String THREE_3GPP = ".3gp";

        public static final String BMP = ".bmp";

        public static final String MP4 = ".mp4";

        public static final String AMR_NB = ".amr";

        public static final String APK = ".apk";

        public static final String AAC = ".aac";
    }

    // 关于mimetype的常量
    public static final class MimeType {
        public static final String MIME_JPEG = "image/jpeg";

        public static final String MIME_PNG = "image/png";

        public static final String MIME_BMP = "image/x-MS-bmp";

        public static final String MIME_GIF = "image/gif";

        public static final String MIME_AUDIO_3GPP = "audio/3gpp";

        public static final String MIME_AUDIO_MP4 = "audio/mp4";

        public static final String MIME_AUDIO_M4A = "audio/m4a";

        public static final String MIME_AUDIO_AMR_NB = "audio/amr";

        public static final String MIME_AUDIO_AAC = "audio/aac";

        public static final String MIME_TXT = "txt/txt";// 用 于PC长消息

        public static final String MIME_WAPPUSH_SMS = "message/sms";

        public static final String MIME_WAPPUSH_TEXT = "txt/wappush"; // 文字wappush

        public static final String MIME_MUSIC_LOVE = "music/love"; // 爱音乐

        public static final String MIME_MUSIC_XIA = "music/xia"; // 虾米音乐

        public static final String MIME_VIDEO_3GPP = "video/3gpp";

        public static final String MIME_VIDEO_ALL = "video/*";

        public static final String MIME_LOCATION_GOOGLE = "location/google";
    }
} 