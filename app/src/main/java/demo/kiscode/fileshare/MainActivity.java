package demo.kiscode.fileshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initIntent();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null
                || intent.getExtras() == null
                || intent.getExtras().getParcelable(Intent.EXTRA_STREAM) == null
        ) {
            return;
        }
/*
        String intentAction = intent.getAction();
        String intentType = intent.getType();
        Bundle extraDatas = intent.getExtras();*/

        Uri receiveUri = intent.getExtras().getParcelable(Intent.EXTRA_STREAM);

        Log.i(TAG, "intentType=" + intent.getAction()
                + "\t intentAction=" + intent.getType()
                + "\t bundle=" + intent.getExtras());

//        FileReceiveDialog dialog = (FileReceiveDialog) getSupportFragmentManager().getFragmentFactory().instantiate(getClassLoader(),"FileReceiveDialog");
        FileReceiveDialog dialog = FileReceiveDialog.instantiate(receiveUri);
        dialog.show(getSupportFragmentManager(), "FileReceiveDialog");
    }
}