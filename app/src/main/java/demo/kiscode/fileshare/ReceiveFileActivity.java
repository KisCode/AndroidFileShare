package demo.kiscode.fileshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

import demo.kiscode.fileshare.adapter.ReceivePathAdapter;
import demo.kiscode.fileshare.biz.FileBiz;
import demo.kiscode.fileshare.contants.PathType;

public class ReceiveFileActivity extends AppCompatActivity {
    private final String TAG = "ReceiveFileActivity";
    private RecyclerView recyclerView;
    private Uri receiveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_file);

        initIntent();
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReceivePathAdapter adapter = new ReceivePathAdapter(Arrays.asList(PathType.values()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(pathType -> {
            File dir = FileBiz.getDirByCode(this, pathType);
            if (dir != null) {
                FileReceiveDialog dialog = FileReceiveDialog.instantiate(receiveUri, pathType);
                dialog.show(getSupportFragmentManager(), "FileReceiveDialog");
            }
        });
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null
                || intent.getExtras() == null
                || intent.getExtras().getParcelable(Intent.EXTRA_STREAM) == null
        ) {
            return;
        }
        receiveUri = intent.getExtras().getParcelable(Intent.EXTRA_STREAM);

        Log.i(TAG, "intentType=" + intent.getAction()
                + "\t intentAction=" + intent.getType()
                + "\t bundle=" + intent.getExtras());

        initRecyclerView();
    }

}