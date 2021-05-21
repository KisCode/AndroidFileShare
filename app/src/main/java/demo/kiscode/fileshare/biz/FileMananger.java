package demo.kiscode.fileshare.biz;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.contants.FileMIME;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.util.FileUtil;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Description:
 * Author: keno
 * Date : 2021/5/14 16:09
 **/
public class FileMananger {

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
                return Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS );
        }
        return null;
    }

    private static String getExternalStorageDownloadDir(Context context) {
        return DIRECTORY_DOWNLOADS + File.separator + context.getString(R.string.app_name);
    }

    /***
     * 根据文件名获取文件对应的MIME类型
     * @param context 上下文Context 用于获取getResources()
     * @param fileName 文件名如 zxc.doc
     * @return FileMIME
     */
    public static FileMIME getMIME(@NonNull Context context, String fileName) {
        String fileExtension = FileUtil.getExtensionName(fileName);
        if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingImage))) {
            return FileMIME.IMAGE;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingAudio))) {
            return FileMIME.AUDIO;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingVideo))) {
            return FileMIME.VIDEO;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingZipPackage))) {
            return FileMIME.ZIP;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingWebText))) {
            return FileMIME.HTML;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingText))) {
            return FileMIME.TEXT;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingWord))) {
            return FileMIME.WORD;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingExcel))) {
            return FileMIME.EXCEL;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingPPT))) {
            return FileMIME.PPT;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingPdf))) {
            return FileMIME.PDF;
        } else if (FileUtil.checkEndsWithSuffixArray(fileExtension, context.getResources().getStringArray(R.array.fileEndingApkPackage))) {
            return FileMIME.APK;
        }
        return FileMIME.UNKWNO;
    }


    /***
     * 获取外部存储路径uri
     * @param fileName 文件名 zxc.doc
     * @return 文件uri
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static Uri getExternalStorageDownloadFileUri(Context context, String fileName) {
        String relativePath = getExternalStorageDownloadDir(context);
        ContentResolver resolver = context.getContentResolver();
        //设置文件参数到ContentValues
        ContentValues values = new ContentValues();
        //设置文件名
        values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
        //设置文件类型
        String mimeType = FileMananger.getMIME(context, fileName).getValue();
        values.put(MediaStore.Downloads.MIME_TYPE, mimeType);

        //设置文件相对路径
        values.put(MediaStore.Downloads.RELATIVE_PATH, relativePath);
        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        return resolver.insert(external, values);
    }


    public static void writeIO(ContentResolver resolver, Uri inUri, Uri outUri, OnReceiveCallback onCallBack) {
        try {
            OutputStream outputStream = resolver.openOutputStream(outUri);
            InputStream inputStream = resolver.openInputStream(inUri);
            writeIO(resolver, inputStream, outputStream, onCallBack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeIO(ContentResolver resolver, Uri inUri, Uri outUri) {
        writeIO(resolver, inUri, outUri, null);
    }

    private static void writeIO(ContentResolver resolver, InputStream inputStream, OutputStream outputStream, OnReceiveCallback onCallBack) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream is null");
        }

        if (outputStream == null) {
            throw new IllegalArgumentException("inputStream is null");
        }
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                executeWriteIO(inputStream, outputStream, onCallBack);
            }
        });
    }

    private static void executeWriteIO(InputStream inputStream, OutputStream outputStream, OnReceiveCallback onCallBack) {
        try {
            byte[] buffer = new byte[4096];
            int count = 0;
            long length = 0;

            long totalSize = inputStream.available();


            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
                length += count;
                Log.i("saveFile", "count：" + inputStream.available() + ", length: " + length);

                if (onCallBack != null) {
                    onCallBack.onProgress(length, totalSize);
                }
            }

            if (onCallBack != null) {
                onCallBack.onComplete();
            }
        } catch (IOException e) {
            e.printStackTrace();

            if (onCallBack != null) {
                onCallBack.onError(e);
            }
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeIO(ContentResolver resolver, Uri inUri, String outAbsolutePath, OnReceiveCallback onCallBack) {
        try {
            OutputStream outputStream = new FileOutputStream(outAbsolutePath);
            InputStream inputStream = resolver.openInputStream(inUri);
            writeIO(resolver, inputStream, outputStream, onCallBack);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface OnReceiveCallback {
        void onProgress(long receivedSize, long totalSize);

        void onComplete();

        void onError(Throwable throwable);
    }
} 