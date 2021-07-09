package demo.kiscode.fileshare.contants;

/**
 * Description: 文件路径缓存
 *  1. FilesDir：Context.getFilesDir() 对应物理路径为/data/user/0/{packageName}/files
 *  2. CacheDir：Context.getCacheDir() 对应物理路径为/data/user/0/{packageName}/cache
 *  3. ExternalFilesDir：Environment.getExternalStorageDirectory() 对应物理路径为/storage/emulated/0/Android/data/{packageName}/files
 *  4. ExternalCacheDir：Context.getExternalFilesDir(String) 对应物理路径为/storage/emulated/0/Android/data/{packageName}/cache
 *  5. ExternalStorageDirectory：Context.getExternalCacheDir() 对应物理路径为/storage/emulated/0/Download
 * Author: keno
 * Date : 2021/5/14 16:51
 **/
public enum PathType {
    FilesDir, CacheDir, ExternalFilesDir, ExternalCacheDir, ExternalStorageDirectory
}