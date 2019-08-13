package com.zbj.skin;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingjia.zheng on 2019/8/12.
 */

public class SkinFactory implements LayoutInflater.Factory2 {
    //装载收集起来的需要换肤的控件的容器
    List<SkinView> viewList = new ArrayList<>();
    private static final String[] prxfixList = {
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //监听xml的生成过程，自己去创建控件
        View view = null;
        //区分这个控件是否是自定义的控件
        if (name.contains(".")) {
            view = onCreateView(name, context, attrs);
        } else {
            for (String s : prxfixList) {
                view = onCreateView(s + name, context, attrs);
                if (view != null) {
                    break;
                }
            }
        }
        //收集所有需要换肤的控件
        if (view != null) {
            //如果控件已经被实例化， 就去判断这个控件是否满足我们换肤的要求 然后收集起来
            peaseView(view, name, attrs);
        }
        return view;
    }

    public void apply() {
        for (SkinView skinView : viewList) {
            skinView.apply();
        }
    }

    private void peaseView(View view, String name, AttributeSet attrs) {
        List<SkinItem> itemList = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            //属性的名字
            String attributeName = attrs.getAttributeName(i);
            //获取属性的资源ID
            String attributeValue = attrs.getAttributeValue(i);
            //判断每条属性是否包含background textColor color
            if (attributeName.contains("background") || attributeName.contains("textColor")
                    || attributeName.contains("color") || attributeName.contains("src")) {
                //获取资源ID
                int resId = Integer.parseInt(attributeValue.substring(1));
                //获取属性的值的类型
                String typeName = view.getResources().getResourceTypeName(resId);
                //获取到属性的值的名字
                String entryName = view.getResources().getResourceEntryName(resId);
                SkinItem skinItem = new SkinItem(attributeName, resId, entryName, typeName);
                itemList.add(skinItem);
            }
        }
        //如果长度大于0，说明当前控件需要换肤
        if (itemList.size() > 0) {
            SkinView skinView = new SkinView(view, itemList);
            viewList.add(skinView);
            skinView.apply();
        }
    }

    /**
     * 将控件进行实例化的方法（创建）
     *
     * @param name
     * @param context
     * @param attrs
     * @return
     */
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            //这个name的class对象
            Class aClass = context.getClassLoader().loadClass(name);
            Constructor<? extends View> constructor = aClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
            view = constructor.newInstance(context, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //收集所有需要换肤的控件
        return view;
    }

    /**
     * 封装每一条属性的对象
     */
    class SkinItem {
        //属性的名字 background
        String name;
        //属性的值的ID
        int resId;
        //属性的值的名字
        String entryName;
        //属性的值的类型
        String typeName;

        public SkinItem(String name, int resId, String entryName, String typeName) {
            this.name = name;
            this.resId = resId;
            this.entryName = entryName;
            this.typeName = typeName;
        }

        public String getName() {
            return name;
        }

        public int getResId() {
            return resId;
        }

        public String getEntryName() {
            return entryName;
        }

        public String getTypeName() {
            return typeName;
        }
    }

    class SkinView {
        View view;
        List<SkinItem> list;

        public SkinView(View view, List<SkinItem> list) {
            this.view = view;
            this.list = list;
        }

        public View getView() {
            return view;
        }

        public List<SkinItem> getList() {
            return list;
        }

        public void apply() {
            for (SkinItem skinItem : list) {
                if (skinItem.getName().equals("background")) {
                    if (skinItem.getTypeName().equals("color")) {
                        view.setBackgroundColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (skinItem.getTypeName().equals("drawable") || skinItem.getTypeName().equals("mipmap")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        } else {
                            view.setBackgroundDrawable(SkinManager.getInstance().getDrawable(skinItem.getResId()));
                        }
                    }
                } else if (skinItem.getName().equals("textColor")) {
                    if (view instanceof TextView) {
                        ((TextView) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    } else if (view instanceof Button) {
                        ((Button) view).setTextColor(SkinManager.getInstance().getColor(skinItem.getResId()));
                    }
                }
            }
        }
    }
}
