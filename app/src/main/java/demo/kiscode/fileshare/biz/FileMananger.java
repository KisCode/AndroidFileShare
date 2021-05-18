package demo.kiscode.fileshare.biz;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import demo.kiscode.fileshare.R;
import demo.kiscode.fileshare.contants.FileMIME;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.util.FileUtil;

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
                return Environment.getExternalStorageDirectory();
        }
        return null;
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


    public static void writeIO(ContentResolver resolver, Uri inUri, Uri outUri, OnWriteCallback onCallBack) {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            outputStream = resolver.openOutputStream(outUri);
            inputStream = resolver.openInputStream(inUri);

            byte[] buffer = new byte[4096];
            int count = 0;
            long length = 0;
            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
                length += count;
                Log.i("saveFile", "count：" + count + ", lenght: " + length);

                if (onCallBack != null) {
                    onCallBack.onProgress(length, inputStream.available());
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

    public static void writeIO(ContentResolver resolver, Uri inUri, Uri outUri) {
        writeIO(resolver, inUri, outUri, null);
    }

    public interface OnWriteCallback {
        void onProgress(long progress, long totalSize);

        void onComplete();

        void onError(Throwable throwable);
    }
} 