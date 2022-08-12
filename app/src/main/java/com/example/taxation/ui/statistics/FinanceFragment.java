package com.example.taxation.ui.statistics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.bean.OneTextItem;
import com.example.taxation.bean.TwoTextItem;
import com.example.taxation.bean.UserInfo;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;
import com.example.taxation.ui.my.LoginActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 财务 碎片页面
 */
public class FinanceFragment extends Fragment {

    private boolean isLogin = false; //是否登录标记，false为未登录

    private final List<TwoTextItem> list1 = new ArrayList<>(); //列表样式1的信息内容
    private final List<OneTextItem> list2 = new ArrayList<>(); //列表样式2的信息内容
    private CommonBaseAdapter<TwoTextItem> adapter1;
    private CommonBaseAdapter<OneTextItem> adapter2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        //加载样式1的list
        RecyclerView recyclerView1 = view.findViewById(R.id.statistics_recycler_view);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        recyclerView1.setLayoutManager(layoutManager1);
        adapter1 = new CommonBaseAdapter<>(list1,
                new CommonBaseAdapter.OnBindDataInterface<TwoTextItem>() {
                    @Override
                    public int getItemLayoutId(int viewType) {
                        return R.layout.list_item_two_text;
                    }

                    @Override
                    public void onBindData(TwoTextItem item, BaseViewHolder holder, int type) {
                        holder.setText(R.id.l_i_t_t_title, item.getTitle());
                        holder.setText(R.id.l_i_t_t_info, item.getInfo());
                        holder.setOnClickListener(R.id.l_i_t_t_all_view, v -> {
                            if (!isLogin) loginDialog(view);
                        });
                    }
                });
        recyclerView1.setAdapter(adapter1);

        //加载样式2的list
        RecyclerView recyclerView2 = view.findViewById(R.id.statistics_recycler_view2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerView2.setLayoutManager(layoutManager2);
        adapter2 = new CommonBaseAdapter<>(list2,
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
                                Intent intent;
                                if ("资产负债表".equals(item.getTitle())) {
                                    intent = new Intent(view.getContext(), ExpandableListActivity.class);
                                } else {
                                    intent = new Intent(view.getContext(), ListActivity.class);
                                }
                                intent.putExtra("title", item.getTitle());
                                startActivity(intent);
                            } else {
                                loginDialog(view);
                            }
                        });
                    }
                });
        recyclerView2.setAdapter(adapter2);

        return view;
    }

    /**
     * 生命周期：在系统准备去启动或者恢复另一个活动的时候调用
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onStart() {
        super.onStart();
        list1.clear();
        list2.clear();
        initData(); //初始化数据
        adapter1.notifyDataSetChanged(); //刷新适配器
        adapter2.notifyDataSetChanged();
    }

    /**
     * 初始化列表信息
     */
    private void initData() {
        UserInfo loginUser = LitePal.where("login = ?", "true").findFirst(UserInfo.class); //获取用户信息
        isLogin = (loginUser != null);

        //添加列表1的数据
        Random random = new Random();
        String[] list1LeftData = {"纳税情况", "应收账款", "应付账款", "销项金额", "进项金额", "资产总额", "利润情况"};
        String[] list1RightData = {"本月应缴：", "累计应收账款：", "累计应付账款：", "", "", "", "本月净利润："};
        for (int i = 0, len = list1LeftData.length; i < len; ++i) {
            String rightData;
            if (isLogin) {
                rightData = list1RightData[i].concat(String.valueOf(random.nextInt(10_000) + 1000)); //本月应缴：1234
            } else {
                rightData = list1RightData[i].concat("***"); //本月应缴：***
            }
            list1.add(new TwoTextItem(list1LeftData[i], rightData));
        }

        //添加列表2的数据
        String[] list2Data = {"资产负债表", "利润表", "现金流表", "资产规划"};
        for (String s : list2Data) {
            list2.add(new OneTextItem(s));
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
