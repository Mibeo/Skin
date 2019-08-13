package com.zbj.skin;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void open(View view) {
        Intent intent = new Intent(this, TwoActivity.class);
        startActivity(intent);
    }
}
