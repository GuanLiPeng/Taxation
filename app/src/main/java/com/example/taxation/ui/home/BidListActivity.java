package com.example.taxation.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.bean.BidInfoItem;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 单独的招标列表页面（分类按钮和搜索结果展示的页面）
 */
public class BidListActivity extends AppCompatActivity {

    private Intent intent;
    private TextView titleText; //标题栏文字

    //list和适配器
    private RecyclerView recyclerViewBid; //card样式列表
    private final List<BidInfoItem> bidList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_bid_list);

        intent = getIntent();
        initView(); //初始化界面
        initData(); //加载列表数据

        //装载招标列表的适配器
        LinearLayoutManager layoutBidManager = new LinearLayoutManager(this);
        recyclerViewBid.setLayoutManager(layoutBidManager);
        CommonBaseAdapter<BidInfoItem> infoAdapter = new CommonBaseAdapter<>(bidList,
                new CommonBaseAdapter.OnBindDataInterface<BidInfoItem>() {
                    @Override
                    public int getItemLayoutId(int viewType) {
                        return R.layout.list_item_bid;
                    }

                    @Override
                    public void onBindData(BidInfoItem item, BaseViewHolder holder, int type) {
                        holder.setText(R.id.l_i_b_classification, item.getClassification());
                        holder.setText(R.id.l_i_b_head, item.getInfo());
                        holder.setText(R.id.l_i_b_content, item.getDetailInfo());
                        holder.setText(R.id.l_i_b_address, item.getAddress());
                        holder.setText(R.id.l_i_b_price, Double.parseDouble(item.getPrice()) + "万元");
                        holder.setText(R.id.l_i_b_date, item.getDate());
                        holder.setOnClickListener(R.id.l_i_b_all_view, v -> {
                            Intent intent = new Intent(BidListActivity.this, BidDetailsActivity.class);
                            intent.putExtra("type", "首页");
                            intent.putExtra("serialNumber", item.getSerialNumber());
                            startActivity(intent);
                        });
                    }
                });
        recyclerViewBid.setAdapter(infoAdapter);
    }

    /**
     * 初始化
     */
    private void initView() {
        //绑定控件
        titleText = findViewById(R.id.a_h_b_l_title);
        recyclerViewBid = findViewById(R.id.a_h_b_l_recycler_view);
        //返回按钮点击事件
        ImageButton returnButton = findViewById(R.id.a_h_b_l_return_button);
        returnButton.setOnClickListener(v -> finish());
    }

    /**
     * 重新加载列表数据
     */
    public void initData() {
        String title = intent.getStringExtra("title");

        //设置标题栏应该显示的内容
        titleText.setText(title);

        //装载数据库中的数据
        List<BidInfoItem> queryResult;
        if ("搜索结果".equals(title)) {
            String query = intent.getStringExtra("query");
            queryResult = LitePal.where("info like ?", "%" + query + "%").find(BidInfoItem.class); //获取首页列表的信息
        } else {
            queryResult = LitePal.where("classification = ?", title).find(BidInfoItem.class);
        }
        if (bidList.size() > 0) { //检查List中是否有数据，有的话就先清空内容再存放
            bidList.clear();
        }
        bidList.addAll(queryResult);
    }
}