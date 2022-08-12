package com.example.taxation.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 获取assets文件中的数据信息
 * <p>
 * 调用方式：
 * JsonUtil.readLocalJson(getContext(), "xxx.json");
 */
public class JsonUtil {

    /**
     * @param context  getContext()
     * @param fileName "xxx.json"
     * @return String
     */
    public static String readLocalJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
