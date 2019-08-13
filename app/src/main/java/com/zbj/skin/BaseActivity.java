package com.zbj.skin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;

/**
 * Created by bingjia.zheng on 2019/8/12.
 */

public class BaseActivity extends Activity {
    private SkinFactory skinFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().setContext(getApplicationContext());
        skinFactory = new SkinFactory();
        //监听XMl的生成过程
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), skinFactory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        skinFactory.apply();
    }

    public void apply() {
        skinFactory.apply();
    }
}
