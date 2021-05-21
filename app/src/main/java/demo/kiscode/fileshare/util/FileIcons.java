package demo.kiscode.fileshare.util;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.DrawableRes;

import demo.kiscode.fileshare.R;

/**
 * Description:文件图片工具类
 * Author: keno
 * Date : 2020/9/25 17:42
 **/
public class FileIcons {
    /***
     * 根据文件名返回对应类型图标
     * @param fileName 文件名
     * @return 图标redId
     */
    public static @DrawableRes
    int getFileIconRes(String fileName) {
        String ext = FileUtil.getExtensionName(fileName).toLowerCase();
        switch (ext) {
            case "xls":
            case "xlsx":
                return R.mipmap.icon_excel;
            case "ppt":
            case "pptx":
                return R.mipmap.icon_ppt;
            case "doc":
            case "docx":
                return R.mipmap.icon_word;
            case "pdf":
                return R.mipmap.icon_pdf;
            case "html":
            case "htm":
                return R.mipmap.icon_html;
            case "txt":
                return R.mipmap.icon_txt;
            case "rar":
                return R.mipmap.icon_rar;
            case "zip":
            case "7z":
                return R.mipmap.icon_zip;
            case "mp4":
                return R.mipmap.icon_mp4;
            case "mp3":
                return R.mipmap.icon_mp3;
            case "png":
                return R.mipmap.icon_png;
            case "gif":
                return R.mipmap.icon_gif;
            case "jpg":
            case "jpeg":
                return R.mipmap.icon_jpg;
            default:
                return R.mipmap.icon_unknow_file;
        }
    }

    /***
     * 根据文件名返回对应类型图标
     * @param fileName 文件名
     * @return 图标redId
     */
    public static @DrawableRes
    int getFileIconRes(Context context, String fileName) {
        String ext = FileUtil.getExtensionName(fileName).toLowerCase();
        Resources resources = context.getResources();
        if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingExcel))) {
            return R.mipmap.icon_excel;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingPPT))) {
            return R.mipmap.icon_ppt;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingWord))) {
            return R.mipmap.icon_word;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingPdf))) {
            return R.mipmap.icon_pdf;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingWebText))) {
            return R.mipmap.icon_html;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingText))) {
            return R.mipmap.icon_txt;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingAudio))) {
            return R.mipmap.icon_mp3;
        }else if (FileUtil.checkEndsWithSuffixArray(ext, resources.getStringArray(R.array.fileEndingVideo))) {
            return R.mipmap.icon_mp4;
        }else{
            switch (ext) {
                case "rar":
                    return R.mipmap.icon_rar;
                case "zip":
                case "7z":
                    return R.mipmap.icon_zip;
                case "png":
                    return R.mipmap.icon_png;
                case "gif":
                    return R.mipmap.icon_gif;
                case "jpg":
                case "jpeg":
                    return R.mipmap.icon_jpg;
                default:
                    return R.mipmap.icon_unknow_file;
            }
        }
    }
}
