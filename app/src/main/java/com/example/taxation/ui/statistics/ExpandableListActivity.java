package com.example.taxation.ui.statistics;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.adapter.GroupListAdapter;
import com.example.taxation.bean.GroupChildListItem;
import com.example.taxation.bean.OneTextItem;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;
import com.example.taxation.util.ConstantUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 资产负债表页面（可扩展列表样式）
 */
public class ExpandableListActivity extends AppCompatActivity {

    //横向列表的适配器
    private RecyclerView recyclerMonthView;
    private final List<OneTextItem> monthList = new ArrayList<>();
    private CommonBaseAdapter<OneTextItem> monthAdapter;

    //竖向列表的适配器
    private ExpandableListView expandableList;
    private final List<OneTextItem> groupList = new ArrayList<>();
    private final List<List<GroupChildListItem>> childList = new ArrayList<>();
    private GroupListAdapter groupAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_expandable_list);

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
                                groupAdapter.notifyDataSetChanged(); //刷新列表
                            }
                        });
                    }
                });
        int endPosition = monthAdapter.getItemCount() - 1; //获取列表总长度
        monthAdapter.setSelectPosition(endPosition); //设置默认选中的是最后一个item
        recyclerMonthView.setAdapter(monthAdapter);
        recyclerMonthView.scrollToPosition(endPosition); //滚动到指定位置

        //竖向列表的适配器
        groupAdapter = new GroupListAdapter(groupList, childList, this);
        expandableList.setAdapter(groupAdapter); //适配器
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //绑定控件
        recyclerMonthView = findViewById(R.id.a_s_e_l_month_list);
        expandableList = findViewById(R.id.a_s_expandable_list);
        ImageView returnButton = findViewById(R.id.a_s_e_l_return_button);
        returnButton.setOnClickListener(v -> finish()); //返回的点击事件

        //装载横向月份信息
        for (String s : ConstantUtil.MONTH) {
            monthList.add(new OneTextItem(s));
        }
    }

    /**
     * 初始化数据
     */
    public void initData() {
        String[] groupTitle = {"流动资产", "非流动资产", "流动负债", "非流动负债"}; //分组 的信息
        String[][] childTitle = {
                {"货币资金", "交易性金融资产", "应收票据", "应收账款", "预付款项", "应收利息", "应收股利", "其他应收款", "存货", "一年内到期的非流动资产", "其他流动资产", "流动资产合计"},
                {"可供出售的金融资产", "持有至到期投资", "长期应收款", "长期股权投资", "投资性房地产", "固定资产", "在建工程", "工程物资", "固定资产清理", "生产性生物资产", "油气资产", "无形资产", "开发支出", "商誉", "长期待摊费用", "递延所得税资产", "其他非流动性资产", "非流动资产合计"},
                {"短期借款", "交易性金融负债", "应付票据", "应付账款", "预收款项", "应付职工薪酬", "应交税费", "应付利息", "应付股利", "其他应付款", "一年内到期的非流动负债", "其他流动负债", "流动负债合计"},
                {"长期借款", "应付债券", "长期应付款", "专项应付款", "预计负债", "递延所得税负债", "其他非流动负债", "非流动负债合计", "负债合计", "股东权益", "股本", "资本公积", "减：库存股", "盈余公积", "未分配利润", "股东权益合计"}
        }; //分组展开 的信息

        //装载数据
        groupList.clear();
        childList.clear();
        Random random = new Random();
        for (int i = 0, len1 = groupTitle.length; i < len1; ++i) {
            groupList.add(new OneTextItem(groupTitle[i])); //流动资产...

            List<GroupChildListItem> child = new ArrayList<>();
            for (int j = 0, len2 = childTitle[i].length; j < len2; ++j) {
                child.add(new GroupChildListItem(childTitle[i][j], //货币资金...
                        "初：" + (random.nextInt(4_000) + 100), //初：123
                        "末：" + (random.nextInt(10_000) + 100) //末：1234
                ));
            }
            childList.add(child);
        }
    }
}