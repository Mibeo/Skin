package com.zbj.skin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;
import java.sql.Driver;

/**
 * Created by bingjia.zheng on 2019/8/12.
 */

public class SkinManager {
    private static SkinManager skinManager = new SkinManager();
    //皮肤插件APK的资源对象
    private Resources resources;
    private Context context;
    //插件apk的包名
    private String skinPackageName;

    public void setContext(Context context) {
        this.context = context;
    }

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        return skinManager;
    }

    /**
     * 根据皮肤apk的路径去获取到它的资源对象
     *
     * @param path
     */
    public void loadSkinApk(String path) {
        //获取包管理器
        PackageManager packageManager = context.getPackageManager();
        //获取到皮肤apk的包信息类
        PackageInfo packageArchiveInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        //获取皮肤apk的包名
        skinPackageName = packageArchiveInfo.packageName;
        try {
            //反射调用AssetManager
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, path);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(),
                    context.getResources().getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据传进来的ID 或匹配皮肤插件APK资源对象 如果有类型和名字一样的就返回
     *
     * @param id
     * @return
     */
    public int getColor(int id) {
        if (resources == null) {
            return id;
        }
        //获取属性值的名字colorPrimary
        String resourceEntryName = context.getResources().getResourceEntryName(id);
        //获取到的属性值的类型 colorPrimary
        String typeName = context.getResources().getResourceTypeName(id);
        //就是名字和类型匹配的资源对象的ID
        int identifier = resources.getIdentifier(resourceEntryName, typeName, skinPackageName);
        if (identifier == 0) {
            return id;
        }
        return resources.getColor(identifier);
    }

    /**
     * 从外置APK中拿到drawable的资源id
     */
    public Drawable getDrawable(int id) {
        if (resources == null) {
            return ContextCompat.getDrawable(context, id);
        }
        //获取到资源id的类型
        String resourceTypeName = context.getResources().getResourceTypeName(id);
        //获取到的就是资源id的名字
        String resourceEntryName = context.getResources().getResourceEntryName(id);
        //就是colorAccent这个资源在外置apk中的id
        int identifier = resources.getIdentifier(resourceEntryName, resourceTypeName, skinPackageName);
        if (identifier == 0) {
            return ContextCompat.getDrawable(context, id);
        }
        return resources.getDrawable(identifier);
    }
}
