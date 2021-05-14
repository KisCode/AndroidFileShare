package demo.kiscode.fileshare.biz;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import demo.kiscode.fileshare.contants.PathType;

/**
 * Description:
 * Author: keno
 * Date : 2021/5/14 16:09
 **/
public class FileBiz {

    public static File getDirByCode(Context context, PathType pathType) {
        switch (pathType) {
            case CacheDir:
                return context.getCacheDir();
            case FilesDir:
                return context.getFilesDir();
            case ExternalCacheDir:
                return context.getExternalCacheDir();
            case ExternalFilesDir:
                return context.getExternalFilesDir(null);
            case ExternalStorageDirectory:
                return Environment.getExternalStorageDirectory();
        }
        return null;
    }
} 