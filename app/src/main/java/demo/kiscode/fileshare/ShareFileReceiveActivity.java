package demo.kiscode.fileshare;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import demo.kiscode.fileshare.pojo.ShareFileInfo;
import demo.kiscode.fileshare.util.FileIcons;

public class ShareFileReceiveActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ShareFileActivity";

    private Uri receiveUri;
    private ShareFileInfo shareFileInfo;

    private ImageView ivFileIcon;
    private TextView tvFileName;
    private TextView tvFileSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share_file_receive);
        initView();

        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String intentAction = intent.getAction();
        String intentType = intent.getType();
        Bundle extraDatas = intent.getExtras();
        Log.i(TAG, "intentType=" + intentType
                + "\t intentAction=" + intentAction
                + "\t bundle=" + extraDatas);

        if (extraDatas == null) {
            return;
        }

        receiveUri = extraDatas.getParcelable(Intent.EXTRA_STREAM);
        if (receiveUri == null) {
            return;
        }

        Log.i(TAG, receiveUri.toString());
        shareFileInfo = new ShareFileInfo();
        if (ContentResolver.SCHEME_FILE.equals(receiveUri.getScheme())) {
            File file = new File(receiveUri.getPath());
            shareFileInfo.setName(file.getName());
            shareFileInfo.setSize(file.length());
        } else if (ContentResolver.SCHEME_CONTENT.equals(receiveUri.getScheme())) {
            ContentResolver contentResolver = getContentResolver();
            Cursor cursor = contentResolver.query(receiveUri, null, null, null, null);
            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                long fileSize = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                shareFileInfo.setName(fileName);
                shareFileInfo.setSize(fileSize);
            }
        }

//        Log.i(TAG, "receive new file: " + shareFileInfo.toString());
        tvFileName.setText(shareFileInfo.getName());
        tvFileSize.setText(Formatter.formatFileSize(this, shareFileInfo.getSize()));
        ivFileIcon.setImageResource(FileIcons.getFileIconRes(shareFileInfo.getName()));
    }

    private void initView() {
        ivFileIcon = findViewById(R.id.iv_file_icon);
        tvFileName = findViewById(R.id.tv_file_name);
        tvFileSize = findViewById(R.id.tv_file_size);
        TextView tvCancel = findViewById(R.id.tv_cancel);
        TextView tvSure = findViewById(R.id.tv_sure);

        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_sure:
                //接收
                agrreReceive();
                break;
        }
    }

    private void agrreReceive() {
        File cache = new File(getCacheDir(), Math.round((Math.random() + 1) * 1000) + shareFileInfo.getName());
        try {
            ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(receiveUri, "r");
            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
            InputStream is = new FileInputStream(fileDescriptor);
            FileOutputStream fos = new FileOutputStream(cache);
            // 定义缓冲数组
            byte[] buffer = new byte[1024];
            // 读出浏览器获得内容
            int len;
            while ((len = is.read(buffer)) != -1) {
                // 写入到内存流中
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            is.close();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}