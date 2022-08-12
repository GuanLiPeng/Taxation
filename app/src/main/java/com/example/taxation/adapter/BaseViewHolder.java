package com.example.taxation.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

/**
 * 通用的ViewHolder，与CommonBaseAdapter配套使用
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final SparseArray<View> mViews;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId R.id.xxx
     * @return
     */
    public <T extends View> T getSubView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 点击事件
     *
     * @param viewId   R.id.xxx
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getSubView(viewId);
        view.setOnClickListener(listener);
    }

    /**
     * 设置TextView内容
     *
     * @param viewId R.id.xxx
     * @param text   a string
     */
    public void setText(int viewId, String text) {
        TextView tv = getSubView(viewId);
        if (tv != null) {
            tv.setText(text);
        }
    }

    /**
     * 设置本地资源图片
     *
     * @param viewId R.id.xxx
     * @param resId  R.drawable.xxx
     */
    public void setImage(int viewId, int resId) {
        ImageView iv = getSubView(viewId);
        if (iv != null) {
            Glide.with(iv.getContext()).load(resId).centerCrop().into(iv);
        }
    }
}
