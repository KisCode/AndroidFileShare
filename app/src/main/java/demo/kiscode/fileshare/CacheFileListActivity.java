package demo.kiscode.fileshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
        loadDatas();
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

    private void loadDatas() {
        final File dir = FileMananger.getDirByCode(this, mPathType);
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                List<FileModel> fileModelList = new ArrayList<>();
                List<File> allFile = FileUtil.getAllFile(dir);
                for (File file : allFile) {
                    FileModel fileModel = new FileModel(file.getName(), mPathType, file.length(), file.lastModified());
                    fileModelList.add(fileModel);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setNewData(fileModelList);
                    }
                });
            }
        });

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

    private void deleteFile(FileModel fileModel) {
        File file = new File(FileMananger.getDirByCode(this, fileModel.getPathType()), fileModel.getName());
        file.delete();

        loadDatas();
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
    }
}