package demo.kiscode.fileshare;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.DialogFragment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import demo.kiscode.fileshare.biz.FileBiz;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.pojo.ShareFileInfo;
import demo.kiscode.fileshare.util.FileIcons;

/**
 * Description: 消息接收页面
 * Author: keno
 * Date : 2020/9/25 15:24
 **/
public class FileReceiveDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "ShareFileActivity";
    private static final int CODE_COMPLETE = 279;
    private static final int CODE_PROGRESS = 389;
    private static final String KEY_FILE_URI = "FILE_URI";
    private static final String KEY_FILE_PATH = "FILE_PATH";

    private Uri receiveUri;
    private String filePath;
    private ShareFileInfo shareFileInfo;

    private ImageView ivFileIcon;
    private TextView tvFileName, tvFileSize, tvReceivePath;
    private ContentLoadingProgressBar progressBar;

    private ProgressHandler mHandler;

    public static FileReceiveDialog instantiate(Uri fileUri, PathType path) {
        FileReceiveDialog dialog = new FileReceiveDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_FILE_URI, fileUri);
        bundle.putSerializable(KEY_FILE_PATH, path);
        dialog.setArguments(bundle);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_share_file_receive, container, false);
        initView(view);
        initIntent();
        initHandler();
        return view;
    }

    private void initHandler() {
        mHandler = new ProgressHandler(getActivity());
    }

    private void initIntent() {
        receiveUri = getArguments().getParcelable(KEY_FILE_URI);
        PathType pathType = (PathType) getArguments().getSerializable(KEY_FILE_PATH);
        if (receiveUri == null) {
            return;
        }

        if (pathType != null) {
            filePath = FileBiz.getDirByCode(getContext(), pathType).getAbsolutePath();
        }
        tvReceivePath.setText(filePath);

        Log.i(TAG, receiveUri.toString());
        Log.i(TAG, "filePath:" + pathType);
        shareFileInfo = new ShareFileInfo();
        if (ContentResolver.SCHEME_FILE.equals(receiveUri.getScheme())) {
            File file = new File(receiveUri.getPath());
            shareFileInfo.setName(file.getName());
            shareFileInfo.setSize(file.length());
        } else if (ContentResolver.SCHEME_CONTENT.equals(receiveUri.getScheme())) {
            ContentResolver contentResolver = getContext().getContentResolver();
            Cursor cursor = contentResolver.query(receiveUri, null, null, null, null);
            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                long fileSize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                shareFileInfo.setName(fileName);
                shareFileInfo.setSize(fileSize);
            }
        }

        if (shareFileInfo != null) {
            tvFileName.setText(shareFileInfo.getName());
            tvFileSize.setText(Formatter.formatFileSize(getContext(), shareFileInfo.getSize()));
            ivFileIcon.setImageResource(FileIcons.getFileIconRes(shareFileInfo.getName()));
        }
    }

    private void initView(View rootView) {
        ivFileIcon = rootView.findViewById(R.id.iv_file_icon);
        tvFileName = rootView.findViewById(R.id.tv_file_name);
        tvFileSize = rootView.findViewById(R.id.tv_file_size);
        tvReceivePath = rootView.findViewById(R.id.tv_receive_path);
        progressBar = rootView.findViewById(R.id.progressbar);
        TextView tvCancel = rootView.findViewById(R.id.tv_cancel);
        TextView tvSure = rootView.findViewById(R.id.tv_sure);

        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                //接收
                agreeReceive();
                break;
        }
    }

    private void agreeReceive() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                File cache = new File(filePath, Math.round((Math.random() + 1) * 1000) + shareFileInfo.getName());
                long fileSize = shareFileInfo.getSize();
                try {
                    ParcelFileDescriptor descriptor = getActivity().getContentResolver().openFileDescriptor(receiveUri, "r");
                    FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                    InputStream is = new FileInputStream(fileDescriptor);
                    FileOutputStream fos = new FileOutputStream(cache);
                    // 定义缓冲数组
                    byte[] buffer = new byte[1024 * 8];
                    // 读出浏览器获得内容
                    long total = 0;
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        // 写入到内存流中
                        fos.write(buffer, 0, len);
                        total += len;

                        int percent = (int) (total * 100 / fileSize);
                        progressBar.setProgress(percent);

                        Message msg = mHandler.obtainMessage();
                        msg.obj = percent;
                        msg.what = CODE_PROGRESS;
                        mHandler.sendMessage(msg);
                    }
                    mHandler.sendEmptyMessage(CODE_COMPLETE);
                    fos.flush();
                    fos.close();
                    is.close();
                    dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class ProgressHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public ProgressHandler(Activity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_PROGRESS:
                    int percent = (int) msg.obj;
                    progressBar.setProgress(percent);
                    break;
                case CODE_COMPLETE:
                    Toast.makeText(mActivity.get(), "接收完毕", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}