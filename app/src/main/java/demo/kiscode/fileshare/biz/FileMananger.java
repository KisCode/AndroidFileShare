package demo.kiscode.fileshare.biz;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import demo.kiscode.fileshare.BuildConfig;
import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.contants.FileMIME;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.pojo.FileModel;
import demo.kiscode.fileshare.util.FileUtil;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Description: 文件管理类
 * Author: keno
 * Date : 2021/5/14 16:09
 **/
public class FileMananger {
    private static final String DIR_NAME = "FileShare";

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
                return Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
        }
        return null;
    }

    private static String getExternalStorageDownloadDir(Context context) {
        return DIRECTORY_DOWNLOADS + File.separator + context.getString(R.string.app_name);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static Uri getExternalStorageDownloadUri(Context context) {
        String relativePath =  getExternalStorageDownloadDir(context);
        ContentResolver resolver = context.getContentResolver();
        //设置文件参数到ContentValues
        ContentValues values = new ContentValues();
        //设置文件相对路径
        values.put(MediaStore.Downloads.RELATIVE_PATH, relativePath);
        Uri external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        return resolver.insert(external, values);
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


    public static void shareFile(Activity activity, File file) {
        Intent fileIntent = getFileIntent(activity, file, getMIME(activity, file.getName()).getValue());
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileIntent.getData());
        shareIntent.setType(fileIntent.getType());
        activity.startActivity(Intent.createChooser(shareIntent, activity.getResources().getText(R.string.share)));
    }

    public static Intent getFileIntent(Context context, File file, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//			Android 7.0文件权限管理管理 net.rlair.efb.provider
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
            intent.setDataAndType(uri, mimeType);
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimeType);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static List<FileModel> queryAllExternalStorageDownloadList(Context context) {
        ContentResolver resolver = context.getContentResolver();
        List<FileModel> fileModelList = new ArrayList<>();
//        Cursor cursor = resolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, null, null, null);
        Cursor cursor = resolver.query(getExternalStorageDownloadUri(context), null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Downloads.DISPLAY_NAME));
                long fileSize = cursor.getLong(cursor.getColumnIndex(MediaStore.Downloads.SIZE));
                long lastModifyDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Downloads.DATE_MODIFIED));
                FileModel fileModel = new FileModel(fileName, PathType.ExternalStorageDirectory, fileSize, lastModifyDate);
                fileModelList.add(fileModel);
            }
        }
        return fileModelList;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static long getAllExternalStorageDownloadTotalSize(Context context) {
        long totalSize = 0;
        ContentResolver resolver = context.getContentResolver();
//        Cursor cursor = resolver.query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, null, null, null);
        Cursor cursor = resolver.query(getExternalStorageDownloadUri(context), null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String relativePath = cursor.getString(cursor.getColumnIndex(MediaStore.Downloads.RELATIVE_PATH));
                Log.i("relativePath", relativePath);
                totalSize += cursor.getLong(cursor.getColumnIndex(MediaStore.Downloads.SIZE));
            }
        }
        return totalSize;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static int deleteExternalStorageDownloadFile(Context context, FileModel fileModel) {
        ContentResolver resolver = context.getContentResolver();
        return resolver.delete(MediaStore.Downloads.EXTERNAL_CONTENT_URI, MediaStore.Downloads.DISPLAY_NAME + " = ?", new String[]{fileModel.getName()});
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