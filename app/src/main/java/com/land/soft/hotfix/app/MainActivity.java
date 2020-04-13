package com.land.soft.hotfix.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.land.soft.hotfix.R;
import com.land.soft.hotfix.hotfix.BugClass;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        Button btn = (Button) findViewById(R.id.fix);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BugClass.BugClass(MainActivity.this);
            }
        });

    }


}
