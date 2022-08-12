package com.example.taxation.ui.statistics;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.bean.OneTextItem;
import com.example.taxation.bean.TwoTextItem;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;
import com.example.taxation.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 利润表、现金流表、资产规划 页面
 */
public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerMonthView, recyclerView;
    private final List<OneTextItem> monthList = new ArrayList<>();
    private final List<TwoTextItem> list = new ArrayList<>();
    private CommonBaseAdapter<OneTextItem> monthAdapter; //横向列表的适配器
    private CommonBaseAdapter<TwoTextItem> adapter; //竖向列表的适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_list);

        initView(); //初始化界面
        initData(); //初始化数据

        //横向月份列表的适配器
        LinearLayoutManager layoutMonthManager = new LinearLayoutManager(this);
        layoutMonthManager.setOrientation(LinearLayoutManager.HORIZONTAL); //将列表横过来
        recyclerMonthView.setLayoutManager(layoutMonthManager);
        monthAdapter = new CommonBaseAdapter<>(monthList,
                new CommonBaseAdapter.OnBindDataInterface<OneTextItem>() {
                    @Override
                    public int getItemLayoutId(int viewType) {
                        return R.layout.list_item_month;
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onBindData(OneTextItem item, BaseViewHolder holder, int type) {
                        TextView textView = holder.getSubView(R.id.l_i_m_text);
                        textView.setText(item.getTitle());
                        if (monthAdapter.getSelectPosition() == holder.getLayoutPosition()) {
                            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.myWhite));
                        } else {
                            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.myBlue));
                        }

                        //点击事件
                        holder.setOnClickListener(R.id.l_i_m_all_view, v -> {
                            if (monthAdapter.getSelectPosition() != holder.getLayoutPosition()) {
                                monthAdapter.setSelectPosition(holder.getLayoutPosition());
                                monthAdapter.notifyDataSetChanged(); //刷新月份
                                initData(); //重新加载列表的数据
                                adapter.notifyDataSetChanged(); //刷新列表
                            }
                        });
                    }
                });
        int endPosition = monthAdapter.getItemCount() - 1; //获取列表总长度
        monthAdapter.setSelectPosition(endPosition); //设置默认选中的是最后一个item
        recyclerMonthView.setAdapter(monthAdapter);
        recyclerMonthView.scrollToPosition(endPosition); //滚动到指定位置

        //竖向列表的适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonBaseAdapter<>(list, new CommonBaseAdapter.OnBindDataInterface<TwoTextItem>() {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.list_item_two_text;
            }

            @Override
            public void onBindData(TwoTextItem item, BaseViewHolder holder, int type) {
                holder.setText(R.id.l_i_t_t_title, item.getTitle());
                holder.setText(R.id.l_i_t_t_info, item.getInfo());
            }
        });
        recyclerView.setAdapter(adapter); //适配器
    }

    /**
     * 初始化
     */
    private void initView() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        //绑定控件
        recyclerMonthView = findViewById(R.id.a_s_l_month_list);
        recyclerView = findViewById(R.id.a_s_l_two_text_list);
        TextView titleText = findViewById(R.id.a_s_l_action_bar_title);
        ImageView returnButton = findViewById(R.id.a_s_l_return_button);

        titleText.setText(title);
        returnButton.setOnClickListener(v -> finish()); //点击事件

        //装载横向月份信息
        for (String s : ConstantUtil.MONTH) {
            monthList.add(new OneTextItem(s));
        }
    }

    /**
     * 初始化数据
     */
    public void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        if (title == null) title = "利润表";

        //装载列表需要的信息
        list.clear(); //检查竖向列表中是否有数据，有则清空重新存放
        Random random = new Random();
        switch (title) {
            case "利润表":
                String[] leftData1 = {"主营业务收入", "营业利润", "利润总额", "净利润总额", "基本每股收益", "稀释每股收益"};
                for (String s : leftData1) {
                    String rightData = "本期：" + (random.nextInt(10_000) + 500);
                    list.add(new TwoTextItem(s, rightData)); //营业利润  本期：1234
                }
                break;
            case "现金流表":
                String[] leftData2 = {"经营活动产生的现金流量", "投资活动产生的现金流量", "筹资活动产生的现金流量", "汇率变动对现金的影响额", "期末现金及现金等价余额"};
                for (String s : leftData2) {
                    String rightData = "本期：" + (random.nextInt(10_000) + 500);
                    list.add(new TwoTextItem(s, rightData)); //营业利润  本期：1234
                }
                break;
            case "资产规划":
                String[] leftData3 = {"期间费用", "增值税税负率", "净利润", "营业收入", "增值税", "企业所得税", "营业成本", "成本占比", "税金占比", "期间费用占比", "净利润占比"};
                int[] randomRange = {100_000, 40, 10_000, 100_000, 10_000, 10_000, 10_000, 25, 25, 40, 20};
                int[] offset = {1_000, 5, 1_000, 10_000, 1_000, 1_000, 1_000, 5, 5, 5, 50};
                String[] unit = {"", "%", "", "", "", "", "", "%", "%", "%", "%"};
                for (int i = 0, len = leftData3.length; i < len; ++i) {
                    String rightData = "本期：" + (random.nextInt(randomRange[i]) + offset[i]) + unit[i]; //"本期：" + 随机数 + 加最小值 + 单位("%") => 本期：12%
                    list.add(new TwoTextItem(leftData3[i], rightData)); //营业利润  本期：12%
                }
                break;
        }
    }
}