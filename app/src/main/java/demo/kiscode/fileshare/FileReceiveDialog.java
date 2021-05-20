package demo.kiscode.fileshare;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Objects;

import demo.kiscode.fileshare.biz.FileMananger;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.pojo.ShareFileInfo;
import demo.kiscode.fileshare.util.FileIcons;

/**
 * Description: 消息接收页面
 * Author: keno
 * Date : 2020/9/25 15:24
 **/
public class FileReceiveDialog extends DialogFragment implements View.OnClickListener, FileMananger.OnReceiveCallback {
    private static final String TAG = "ShareFileActivity";
    private static final String KEY_FILE_URI = "FILE_URI";
    private static final String KEY_FILE_PATH = "FILE_PATH";

    private Uri sourceUri;
    private PathType pathType;
    private ShareFileInfo shareFileInfo;

    private ImageView ivFileIcon;
    private TextView tvFileName, tvFileSize, tvReceivePath;
    private ContentLoadingProgressBar progressBar;

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
        return view;
    }

    private void initIntent() {
        sourceUri = getArguments().getParcelable(KEY_FILE_URI);
        pathType = (PathType) getArguments().getSerializable(KEY_FILE_PATH);
        if (sourceUri == null) {
            return;
        }

        String filePath;
        if (pathType == null) {
            //默认缓存路径
            pathType = PathType.CacheDir;
        }

        filePath = FileMananger.getDirByCode(getContext(), pathType).getAbsolutePath();
        tvReceivePath.setText(filePath);

        Log.i(TAG, sourceUri.toString());
        Log.i(TAG, "filePath:" + pathType);
        shareFileInfo = new ShareFileInfo();
        if (ContentResolver.SCHEME_FILE.equals(sourceUri.getScheme())) {
            File file = new File(sourceUri.getPath());
            shareFileInfo.setName(file.getName());
            shareFileInfo.setSize(file.length());
        } else if (ContentResolver.SCHEME_CONTENT.equals(sourceUri.getScheme())) {
            ContentResolver contentResolver = getContext().getContentResolver();
            Cursor cursor = contentResolver.query(sourceUri, null, null, null, null);
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
                saveFile();
                break;
        }
    }

    private void saveFile() {
        String saveFileName = shareFileInfo.getName();

        if (pathType == PathType.ExternalStorageDirectory
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Uri targetSaveUri = FileMananger.getExternalStorageDownloadFileUri(Objects.requireNonNull(getActivity()), shareFileInfo.getName());
            FileMananger.writeIO(Objects.requireNonNull(getActivity()).getContentResolver(), sourceUri, targetSaveUri, this);
        } else {
            //内部文件路径直接写入
            File targetFile = new File(FileMananger.getDirByCode(getContext(), pathType), saveFileName);
            FileMananger.writeIO(Objects.requireNonNull(getActivity()).getContentResolver(), sourceUri, targetFile.getAbsolutePath(), this);
        }
    }

    @Override
    public void onProgress(long receivedSize, long totalSize) {
        //更新下载进度
        if (totalSize <= 0) {
            return;
        }
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            int percent = (int) (receivedSize * 100 / totalSize);
            progressBar.setProgress(percent);
        });
    }

    @Override
    public void onComplete() {
        //下载完成
        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            Log.i(TAG, Thread.currentThread().getName() + "\tonComplete");
            Toast.makeText(getContext(), "接收完毕", Toast.LENGTH_LONG).show();
            dismiss();
        });
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e(TAG, "onError:\t" + throwable);

    }
}