package com.example.taxation.ui.my;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.bean.InquireInfoItem;
import com.example.taxation.bean.OneTextItem;
import com.example.taxation.bean.UserInfo;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;
import com.example.taxation.util.JsonUtil;
import com.example.taxation.util.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 个人信息 碎片页面
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    private ImageView imageView;
    private TextView textView;

    private boolean isLogin = false; //是否登录标记，false为未登录

    private RecyclerView recyclerView;
    private final List<OneTextItem> listOneText = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        initView(view); //初始化视图
        initData(view); //初始化数据

        //加载列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        CommonBaseAdapter<OneTextItem> adapter = new CommonBaseAdapter<>(listOneText,
                new CommonBaseAdapter.OnBindDataInterface<OneTextItem>() {
                    @Override
                    public int getItemLayoutId(int viewType) {
                        return R.layout.list_item_text_and_arrow;
                    }

                    @Override
                    public void onBindData(OneTextItem item, BaseViewHolder holder, int type) {
                        holder.setText(R.id.l_i_t_a_a_title, item.getTitle());
                        holder.setOnClickListener(R.id.l_i_t_a_a_all_view, v -> {
                            if (isLogin) {
                                Intent intent = new Intent(view.getContext(), InquireActivity.class);
                                intent.putExtra("from", "MyFragment");
                                intent.putExtra("type", item.getTitle());
                                startActivity(intent);
                            } else {
                                loginDialog(view);
                            }
                        });
                    }
                });
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * 生命周期，当页面由不可见变为可见后调用
     */
    @Override
    public void onStart() {
        super.onStart();
        updateUserLoginStatus(); //检查前页面是 登录状态 还是 未登录状态
    }

    /**
     * 初始化视图
     */
    private void initView(View view) {
        //修改状态栏
        StatusBarUtil.setLightMode(Objects.requireNonNull(getActivity()).getWindow(),
                R.color.myBlueDark,
                false);

        //绑定控件
        imageView = view.findViewById(R.id.my_image_view);
        textView = view.findViewById(R.id.my_text_view);
        recyclerView = view.findViewById(R.id.my_recycler_view);

        //登录或退出的点击事件
        textView.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData(View view) {
        String[] array = {"招标查询", "中标查询", "流标查询", "资审查询"};
        for (String s : array) {
            listOneText.add(new OneTextItem(s));
        }

        //向数据库中存储数据
        InquireInfoItem info = LitePal.findFirst(InquireInfoItem.class);
        if (info == null) {
            String jsonData = JsonUtil.readLocalJson(view.getContext(), "InquireBidInfo.json");
            List<InquireInfoItem> list = new Gson().fromJson(jsonData, new TypeToken<List<InquireInfoItem>>() {
            }.getType());
            if (list.size() > 0) {
                LitePal.saveAll(list);
            }
        }
    }

    /**
     * 将当前页面设置成登录状态 或者 未登录状态
     */
    @SuppressLint("SetTextI18n")
    private void updateUserLoginStatus() {
        UserInfo loginUser = LitePal.where("login = ?", "true").findFirst(UserInfo.class); //获取用户信息
        isLogin = (loginUser != null);
        if (isLogin) {
            textView.setText(loginUser.getUsername() + " | 退出"); //更改用户名
            imageView.setImageResource(R.drawable.portrait_true); //更改头像
        } else {
            textView.setText("当前未登录 | 登录"); //更改用户名
            imageView.setImageResource(R.drawable.portrait_false); //更改头像
        }
    }

    /**
     * 用户名的点击事件
     *
     * @param v v.get
     */
    @Override
    public void onClick(View v) {
        if (isLogin) { //退出登录
            UserInfo userInfo = new UserInfo();
            userInfo.setLogin("false");
            userInfo.updateAll("login = ?", "true"); //将所有登录状态为true的改成false
            updateUserLoginStatus(); //修改界面
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }

    /**
     * 登录提示的弹窗
     */
    private void loginDialog(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
        dialog.setMessage("当前未登录，登录后可查看");
        //去登录按钮
        dialog.setPositiveButton("去登录", (dialog1, which) -> {
            startActivity(new Intent(v.getContext(), LoginActivity.class));
        });
        //取消按钮
        dialog.setNegativeButton("取消", (dialog2, which) -> {
        });
        dialog.show(); //显示消息提示框
    }
}
