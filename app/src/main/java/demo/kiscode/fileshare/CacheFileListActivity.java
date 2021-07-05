package demo.kiscode.fileshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demo.kiscode.fileshare.adapter.FileAdapter;
import demo.kiscode.fileshare.biz.FileMananger;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.pojo.FileModel;
import demo.kiscode.fileshare.util.FileUtil;

/**
 * Description: 缓存文件列表
 * Author: keno
 * Date : 2021/5/21 13:04
 **/
public class CacheFileListActivity extends AppCompatActivity {
    private static final int CODE_REQUEST_EXTENAL_STORAGE = 192;
    private static final String KEY_PATH_TYPE = "PATH_TYPE";
    private FileAdapter mAdapter;
    private PathType mPathType;

    public static void start(Context context, PathType pathType) {
        Intent starter = new Intent(context, CacheFileListActivity.class);
        starter.putExtra(KEY_PATH_TYPE, pathType);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_file_list);
        initIntentData();
        initViews();
        requestPermission();
    }

    @SuppressLint("WrongConstant")
    private void requestPermission() {
        if (mPathType != PathType.ExternalStorageDirectory) {
            //非外部存储无需 申请权限
            loadData();
            return;
        }

        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadData();
        } else {
            ActivityCompat.requestPermissions(this
                    , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , CODE_REQUEST_EXTENAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CODE_REQUEST_EXTENAL_STORAGE == requestCode
                && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            loadData();
        }
    }

    private void initIntentData() {
        mPathType = (PathType) getIntent().getSerializableExtra(KEY_PATH_TYPE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        final File dir = FileMananger.getDirByCode(this, mPathType);
        Log.i("FilePath", dir.getAbsolutePath());
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                final List<FileModel> fileModelList = new ArrayList<>();
                if (mPathType == PathType.ExternalStorageDirectory
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    List<FileModel> list = FileMananger.queryAllExternalStorageDownloadList(CacheFileListActivity.this);
                    fileModelList.addAll(list);
                } else {
                    List<File> allFile = FileUtil.getAllFile(dir);
                    for (File file : allFile) {
                        FileModel fileModel = new FileModel(file.getName(), mPathType, file.length(), file.lastModified());
                        fileModelList.add(fileModel);
                    }
                }
                runOnUiThread(() -> mAdapter.setNewData(fileModelList));
            }
        });

    }

    /***
     * 删除文件
     * @param fileModel
     */
    private void deleteFile(FileModel fileModel) {
        if (mPathType == PathType.ExternalStorageDirectory
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            FileMananger.deleteExternalStorageDownloadFile(this, fileModel);
        } else {
            File file = new File(FileMananger.getDirByCode(this, fileModel.getPathType()), fileModel.getName());
            file.delete();
        }

        loadData();
    }

    private void openFile(FileModel fileModel) {
        File file = new File(FileMananger.getDirByCode(this, fileModel.getPathType()), fileModel.getName());
        String type = FileMananger.getMIME(this, fileModel.getName()).getValue();
        Intent intent = FileMananger.getFileIntent(this, file, type);
        startActivity(intent);
    }

    private void shareFile(FileModel fileModel) {
        File file = new File(FileMananger.getDirByCode(this, fileModel.getPathType()), fileModel.getName());
        FileMananger.shareFile(this, file);
    }

    private void initViews() {
        final File dir = FileMananger.getDirByCode(this, mPathType);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mPathType.name());
        toolbar.setSubtitle(dir.getAbsolutePath());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);      //显示向上返回
        getSupportActionBar().setDisplayShowHomeEnabled(true);      //显示返回按钮

        RecyclerView recyclerView = findViewById(R.id.recyclerview_file_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileAdapter(Collections.emptyList());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mAdapter.setOnItemClickListener((adapter, position) -> openFile(adapter.getItem(position)));

        mAdapter.setOnItemLongClickListener((adapter, position) -> {
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(R.array.file_operator, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            FileModel item = adapter.getItem(position);
                            switch (which) {
                                case 0:
                                    //打开文件
                                    openFile(item);
                                    break;
                                case 1:
                                    //分享文件
                                    shareFile(item);
                                    break;
                                case 2:
                                    //删除文件
                                    deleteFile(item);
                                    break;
                            }
                        }
                    }).create();
            alertDialog.show();
        });
    }
}