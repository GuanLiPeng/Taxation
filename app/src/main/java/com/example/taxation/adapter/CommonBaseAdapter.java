package com.example.taxation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * 通用RecyclerView适配器，在新的类中直接实现其中的接口即可使用（implements）
 *
 * @param <T>
 */
public class CommonBaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> mData;
    private int selectPosition;
    private final OnBindDataInterface<T> mOnBindDataInterface;
    private OnMultiTypeBindDataInterface<T> mOnMultiTypeBindDataInterface;

    //单样式列表初始化方法
    public CommonBaseAdapter(List<T> data, OnBindDataInterface<T> bindInterface) {
        mData = data;
        mOnBindDataInterface = bindInterface;
        selectPosition = 0;
    }

    //多样式列表初始化方法
    public CommonBaseAdapter(List<T> data, OnMultiTypeBindDataInterface<T> bindInterface) {
        mData = data;
        mOnMultiTypeBindDataInterface = bindInterface;
        mOnBindDataInterface = bindInterface;
        selectPosition = 0;
    }

    /**
     * 修改mData中的数据，并返回总长度，用来刷新列表
     *
     * @param item
     * @return
     */
    public int removeData(T item) {
        mData.remove(item);
        return mData.size();
    }

    /**
     * 设置默认下标
     *
     * @param selectPosition
     */
    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    /**
     * 获得默认下标
     *
     * @return
     */
    public int getSelectPosition() {
        return selectPosition;
    }


    /* 绑定数据的接口 */
    public interface OnBindDataInterface<T> {
        /**
         * 绑定列表元素的item页面(R.layout.xxx)
         *
         * @param viewType
         * @return
         */
        int getItemLayoutId(int viewType);

        /**
         * 绑定数据(R.id.xxx)，最后一个type参数是多类型item使用的
         *
         * @param item   item.get()
         * @param holder holder.setText(R.id.xxx, "a string")
         * @param type   default 0
         */
        void onBindData(T item, BaseViewHolder holder, int type);
    }

    /**
     * 多类型item的支持，例如包含head的列表
     *
     * @param <T>
     */
    public interface OnMultiTypeBindDataInterface<T> extends OnBindDataInterface<T> {
        /**
         * 返回item类型，针对不同的item类型使用不同的布局元素
         *
         * @param position switch(case 0, case 1, case 2...)
         * @return
         */
        int getItemViewType(int position);
    }


    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(mOnBindDataInterface.getItemLayoutId(viewType), parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        mOnBindDataInterface.onBindData(mData.get(position), holder, getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mOnMultiTypeBindDataInterface != null) {
            return mOnMultiTypeBindDataInterface.getItemViewType(position);
        }
        return 0;
    }
}