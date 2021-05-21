package demo.kiscode.fileshare;

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

import demo.kiscode.fileshare.adapter.CacheManangerAdapter;
import demo.kiscode.fileshare.biz.FileMananger;
import demo.kiscode.fileshare.contants.PathType;
import demo.kiscode.fileshare.pojo.CacheModel;
import demo.kiscode.fileshare.util.FileUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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

        mAdapter.setOnItemClickListener(new CacheManangerAdapter.OnItemClickListener() {
            @Override
            public void onClick(CacheModel cacheModel) {
                CacheFileListActivity.start(MainActivity.this, cacheModel.getPathType());
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
                File dir = FileMananger.getDirByCode(MainActivity.this, pathType);
                long totalSize = FileUtil.getDirTotalSize(dir);
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