package com.example.taxation.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 防止Toast被多次点击
 * <p>
 * 调用方式：
 * ToastUtil.showShortToast( Context , String );
 */
public class ToastUtil {

    private static Toast toast;

    /**
     * 显示短时间的Toast
     *
     * @param context this
     * @param content 消息提醒的内容
     */
    public static void showShortToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 显示长时间的Toast
     *
     * @param context this
     * @param content 消息提醒的内容
     */
    public static void showLongToast(Context context, String content) {
        if (toast == null) {
            toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
