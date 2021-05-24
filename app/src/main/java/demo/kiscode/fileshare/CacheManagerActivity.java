package demo.kiscode.fileshare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import demo.kiscode.fileshare.adapter.CacheManangerAdapter;
import demo.kiscode.fileshare.adapter.comman.CommanAdapter;
import demo.kiscode.fileshare.biz.FileMananger;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.pojo.CacheModel;
import demo.kiscode.fileshare.util.FileUtil;

/**
 * Description:  首页文件管理列表
 **/
public class CacheManagerActivity extends AppCompatActivity {
    private static final String TAG = "CacheManagerActivity";
    private RecyclerView recyclerView;
    private CacheManangerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerview_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CacheManangerAdapter(Collections.emptyList());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new CommanAdapter.OnItemClickListener<CacheModel>() {
            @Override
            public void onItemClick(CommanAdapter<CacheModel> adapter, int position) {
                CacheModel cacheModel = adapter.getItem(position);
                CacheFileListActivity.start(CacheManagerActivity.this, cacheModel.getPathType());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(() -> {
            ArrayList<CacheModel> cacheList = new ArrayList<>();
            for (PathType pathType : PathType.values()) {
                File dir = FileMananger.getDirByCode(CacheManagerActivity.this, pathType);
                long totalSize = 0;
                if (PathType.ExternalStorageDirectory == pathType
                        && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    totalSize = FileMananger.getAllExternalStorageDownloadTotalSize(this);
                } else {
                    totalSize = FileUtil.getDirTotalSize(dir);
                }

                String absolutePath = dir.getAbsolutePath();
                CacheModel cacheModel = new CacheModel(pathType, absolutePath, totalSize);
                cacheList.add(cacheModel);
            }

            runOnUiThread(() -> {
                mAdapter.setNewData(cacheList);
            });
        });


    }

}