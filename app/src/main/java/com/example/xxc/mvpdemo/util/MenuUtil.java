package com.example.xxc.mvpdemo.util;

import android.view.Menu;
import android.view.Window;

import java.lang.reflect.Method;

public class MenuUtil {

    public static void setOverflowIconVisible(int featureId, Menu menu) {
        // ActionBar的featureId == 8，Toolbar的featureId == 108
        if (featureId % 100 == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                // setOptionalIconsVisible是个隐藏方法，需要通过反射机制调用
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
