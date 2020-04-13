package com.land.soft.hotfix.app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import com.land.soft.hotfix.R;
import com.land.soft.hotfix.app.util.FixDexUtil;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_REQ_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {

        req();

        TextView textView = (TextView) findViewById(R.id.textview);

        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        // 遍历所有的修复dex , 因为可能是多个dex修复包
        File fileDir = externalStorageDirectory != null ?
                new File(externalStorageDirectory, "007") :
                new File(getFilesDir(), FixDexUtil.DEX_DIR);// data/user/0/包名/files/odex（这个可以任意位置）
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (FixDexUtil.isGoingToFix(this)) {

            FixDexUtil.loadFixedDex(this, Environment.getExternalStorageDirectory());
            textView.setText("fixing...");

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }

    private void req() {

        int permission_write = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_read = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission_write != PermissionChecker.PERMISSION_GRANTED || permission_read != PermissionChecker.PERMISSION_GRANTED) {
            reqestPermission();
        } else {

        }

    }

    private void reqestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_REQ_CODE);
    }
}
