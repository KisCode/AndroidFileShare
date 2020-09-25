package demo.kiscode.fileshare.util;


import java.util.HashMap;
import java.util.Map;

import demo.kiscode.fileshare.R;


public class FileIcons {

    private static final Map<String, Integer> bigIconMap = new HashMap<String, Integer>();

    static {
        bigIconMap.put("xls", R.drawable.file_ic_detail_excel);
        bigIconMap.put("ppt", R.drawable.file_ic_detail_ppt);
        bigIconMap.put("doc", R.drawable.file_ic_detail_word);
        bigIconMap.put("xlsx", R.drawable.file_ic_detail_excel);
        bigIconMap.put("pptx", R.drawable.file_ic_detail_ppt);
        bigIconMap.put("docx", R.drawable.file_ic_detail_word);
        bigIconMap.put("pdf", R.drawable.file_ic_detail_pdf);
        bigIconMap.put("html", R.drawable.file_ic_detail_html);
        bigIconMap.put("htm", R.drawable.file_ic_detail_html);
        bigIconMap.put("txt", R.drawable.file_ic_detail_txt);
        bigIconMap.put("rar", R.drawable.file_ic_detail_rar);
        bigIconMap.put("zip", R.drawable.file_ic_detail_zip);
        bigIconMap.put("7z", R.drawable.file_ic_detail_zip);
        bigIconMap.put("mp4", R.drawable.file_ic_detail_mp4);
        bigIconMap.put("mp3", R.drawable.file_ic_detail_mp3);
        bigIconMap.put("png", R.drawable.file_ic_detail_png);
        bigIconMap.put("gif", R.drawable.file_ic_detail_gif);
        bigIconMap.put("jpg", R.drawable.file_ic_detail_jpg);
        bigIconMap.put("jpeg", R.drawable.file_ic_detail_jpg);
    }

    public static int getFileIconRes(String fileName) {
        String ext = FileUtil.getExtensionName(fileName).toLowerCase();
        Integer resId = bigIconMap.get(ext);
        if (resId == null) {
            return R.drawable.file_ic_detail_unknow;
        }

        return resId.intValue();
    }
}
