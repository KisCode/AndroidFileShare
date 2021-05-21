package demo.kiscode.fileshare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
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

    public static void start(Context context, PathType pathType) {
        Intent starter = new Intent(context, CacheFileListActivity.class);
        starter.putExtra(KEY_PATH_TYPE, pathType);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_file_list);

        initViews();
        loadDatas();
    }

    private void loadDatas() {
        PathType pathType = (PathType) getIntent().getSerializableExtra(KEY_PATH_TYPE);
        final File dir = FileMananger.getDirByCode(this, pathType);
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                List<FileModel> fileModelList = new ArrayList<>();
                List<File> allFile = FileUtil.getAllFile(dir);
                for (File file : allFile) {
                    FileModel fileModel = new FileModel(file.getName(), pathType, file.length(), file.lastModified());
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
//        mAdapter.
    }

    private void initViews() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview_file_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new FileAdapter(Collections.emptyList());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }
}