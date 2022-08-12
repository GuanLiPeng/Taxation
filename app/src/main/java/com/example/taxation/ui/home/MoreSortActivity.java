package com.example.taxation.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.taxation.R;
import com.example.taxation.bean.StatisticsIconItem;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;

import static org.litepal.LitePalApplication.getContext;

import java.util.ArrayList;

/**
 * 更多分类 页面
 */
public class MoreSortActivity extends AppCompatActivity {

    ArrayList<StatisticsIconItem> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more_sort);

        //初始化数据
        initData();
        //返回按钮
        ImageView returnButton = findViewById(R.id.a_h_m_s_return_button);
        returnButton.setOnClickListener(v -> finish());

        //加载列表视图
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        RecyclerView recyclerView = findViewById(R.id.a_h_m_s_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        CommonBaseAdapter<StatisticsIconItem> adapter = new CommonBaseAdapter<>(list,
                new CommonBaseAdapter.OnBindDataInterface<StatisticsIconItem>() {
                    @Override
                    public int getItemLayoutId(int viewType) {
                        return R.layout.list_item_statistics_icon;
                    }

                    @Override
                    public void onBindData(StatisticsIconItem item, BaseViewHolder holder, int type) {
                        holder.setImage(R.id.l_i_s_i_image_button, item.getImgId());
                        holder.setText(R.id.l_i_s_i_text, item.getText());
                        holder.setOnClickListener(R.id.l_i_s_i_image_button, v -> {
                            Intent intent = new Intent(getContext(), BidListActivity.class);
                            intent.putExtra("title", item.getText());
                            startActivity(intent);
                        });
                    }
                });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    public void initData() {
        int[] imgIds = {
                R.drawable.button_construction, R.drawable.button_medical_hygiene, R.drawable.button_environmental_protection,
                R.drawable.button_information_construction, R.drawable.button_office, R.drawable.button_business_service,
                R.drawable.button_mechanical_equipment, R.drawable.button_clothing_appliances, R.drawable.button_farming,
                R.drawable.button_energy_and_chemical, R.drawable.button_ai_manufacture, R.drawable.button_resource_exchange,
                R.drawable.button_other_all
        };
        String[] names = {
                "工程建设", "医疗卫生", "环保绿化",
                "信息建设", "办公文教", "商业服务",
                "机械设备", "服装家电", "农林牧渔",
                "能源化工", "智能制造", "资源交易",
                "其他"
        };
        for (int i = 0, len = imgIds.length; i < len; ++i) {
            list.add(new StatisticsIconItem(imgIds[i], names[i]));
        }
    }
}