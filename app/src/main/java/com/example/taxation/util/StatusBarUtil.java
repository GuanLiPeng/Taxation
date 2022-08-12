package com.example.taxation.util;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;

/**
 * 改变状态栏颜色
 * 此效果只对Android 6.0以上有效果
 * <p>
 * 调用方式：
 * StatusBarUtil.setLightMode(getWindow(), R.color.xxx, true);
 */
public class StatusBarUtil {

    /**
     * 将状态栏设置成自定义文字和背景
     *
     * @param win      当前页面窗口
     * @param colorId  颜色id
     * @param darkText 是否使用深色图标和字体
     */
    public static void setLightMode(Window win, int colorId, boolean darkText) {
        // 设置状态栏底色
        win.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        win.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        win.setStatusBarColor(ContextCompat.getColor(win.getContext(), colorId));

        // 设置状态栏字体是否为黑色
        if (darkText) {
            win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            win.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }
}
