package demo.kiscode.fileshare.util;

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
                return R.drawable.file_ic_detail_excel;
            case "ppt":
            case "pptx":
                return R.drawable.file_ic_detail_ppt;
            case "doc":
            case "docx":
                return R.drawable.file_ic_detail_word;
            case "pdf":
                return R.drawable.file_ic_detail_pdf;
            case "html":
            case "htm":
                return R.drawable.file_ic_detail_html;
            case "txt":
                return R.drawable.file_ic_detail_txt;
            case "rar":
                return R.drawable.file_ic_detail_rar;
            case "zip":
            case "7z":
                return R.drawable.file_ic_detail_zip;
            case "mp4":
                return R.drawable.file_ic_detail_mp4;
            case "mp3":
                return R.drawable.file_ic_detail_mp3;
            case "png":
                return R.drawable.file_ic_detail_png;
            case "gif":
                return R.drawable.file_ic_detail_gif;
            case "jpg":
            case "jpeg":
                return R.drawable.file_ic_detail_jpg;
            default:
                return R.drawable.file_ic_detail_unknow;
        }
    }
}
