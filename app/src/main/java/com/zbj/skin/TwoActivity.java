package com.zbj.skin;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TwoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }

    public void change(View view) {
        SkinManager.getInstance().loadSkinApk(Environment.getExternalStorageDirectory() + "/skin.apk");
        apply();
    }
}
