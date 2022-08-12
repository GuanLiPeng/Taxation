package com.example.taxation.ui.my;

import static org.litepal.LitePalApplication.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.bean.InquireInfoItem;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;
import com.example.taxation.ui.home.BidDetailsActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 招标查询、中标查询、流标查询、资审查询共同使用的页面
 */
public class InquireActivity extends AppCompatActivity {

    //界面控件
    private TextView titleText;
    private ImageView noContentImage;
    private TextView noContentText;

    //列表的 list和适配器
    private RecyclerView recyclerView;
    private List<InquireInfoItem> list = new ArrayList<>();
    private CommonBaseAdapter<InquireInfoItem> adapter;

    private Intent mIntent;
    private String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inquire);

        mIntent = getIntent();
        type = mIntent.getStringExtra("type");

        initView(); //初始化界面
        initData(); //初始化数据

        if (list.size() > 0) {
            setListIsVisible(true); //显示列表
            LinearLayoutManager layoutCardManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutCardManager);
            adapter = new CommonBaseAdapter<>(list,
                    new CommonBaseAdapter.OnBindDataInterface<InquireInfoItem>() {
                        @Override
                        public int getItemLayoutId(int viewType) {
                            return R.layout.list_item_bid_state_inquire;
                        }

                        @Override
                        public void onBindData(InquireInfoItem item, BaseViewHolder holder, int type) {
                            holder.setText(R.id.l_i_b_s_i_title, item.getInfo());
                            holder.setText(R.id.l_i_b_s_i_content, item.getDetailInfo());
                            //item点击查看详情
                            holder.setOnClickListener(R.id.l_i_b_s_i_all_view, v -> {
                                Intent intent = new Intent(getContext(), BidDetailsActivity.class);
                                intent.putExtra("type", item.getType());
                                intent.putExtra("serialNumber", item.getSerialNumber());
                                String from = mIntent.getStringExtra("from");
                                if ("BidDetailsActivity".equals(from)) {
                                    setResult(200, intent);
                                    finish();
                                } else {
                                    startActivity(intent);
                                }
                            });
                            //设置按钮是否显示 和 审核提示的文字颜色
                            TextView button = holder.getSubView(R.id.l_i_b_s_i_button);
                            TextView stateText = holder.getSubView(R.id.l_i_b_s_i_state);
                            if ("招标查询".equals(item.getType())) {
                                button.setVisibility(View.VISIBLE); //显示按钮
                                button.setOnClickListener(v -> { //点击事件
                                    cancelApplicationDialog(item, holder);
                                });
                                stateText.setTextColor(getResources().getColor(R.color.myPink, getContext().getTheme())); //设置字体颜色为粉红色
                                stateText.setText("等待审核");
                            } else {
                                button.setVisibility(View.INVISIBLE); //隐藏按钮
                                if (item.getState()) {
                                    stateText.setTextColor(getResources().getColor(R.color.myOrange, getContext().getTheme())); //设置字体颜色为橘黄色
                                    stateText.setText("已通过");
                                } else {
                                    stateText.setTextColor(getResources().getColor(R.color.myPink, getContext().getTheme()));
                                    stateText.setText("未通过");
                                }
                            }
                        }
                    });

            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);
        } else {
            setListIsVisible(false); //不显示列表
        }
    }

    /**
     * 生命周期：当页面由不可见变为可见后被调用
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        super.onResume();
        //刷新数据
        list = LitePal.where("type = ?", type).find(InquireInfoItem.class); //从数据库获取数据
        if (list.size() > 0) {
            setListIsVisible(true); //更新UI
            adapter.notifyDataSetChanged(); //刷新数据
        } else {
            setListIsVisible(false); //不显示列表
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //绑定控件
        titleText = findViewById(R.id.a_m_i_action_bar_title);
        noContentImage = findViewById(R.id.a_m_i_no_info_image);
        noContentText = findViewById(R.id.a_m_i_no_info_text);
        recyclerView = findViewById(R.id.a_m_i_recycler_view);
        ImageButton returnButton = findViewById(R.id.a_m_i_return_button);
        returnButton.setOnClickListener(v -> finish());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        titleText.setText(type); //设置标题文字
        if (list.size() > 0) {
            list.clear();
        }
        list = LitePal.where("type = ?", type).find(InquireInfoItem.class); //从数据库获取数据
    }

    /**
     * 设置列表是否显示
     */
    private void setListIsVisible(boolean isVisible) {
        if (isVisible) { //当数据库获取时，显示列表视图
            noContentImage.setVisibility(View.INVISIBLE); //隐藏无内容的图片
            noContentText.setVisibility(View.INVISIBLE); //隐藏无内容的文字提示
            recyclerView.setVisibility(View.VISIBLE); //显示列表
        } else {
            noContentImage.setVisibility(View.VISIBLE); //隐藏无内容的图片
            noContentText.setVisibility(View.VISIBLE); //隐藏无内容的文字提示
            recyclerView.setVisibility(View.INVISIBLE); //隐藏列表
        }
    }

    /**
     * 取消申请的弹窗
     */
    private void cancelApplicationDialog(InquireInfoItem item, BaseViewHolder holder) {
        TextView button = holder.getSubView(R.id.l_i_b_s_i_button);
        AlertDialog.Builder dialog = new AlertDialog.Builder(button.getContext());
        dialog.setMessage("是否撤销申请？");
        dialog.setPositiveButton("确认撤销", (dialog1, which) -> {
            LitePal.deleteAll(InquireInfoItem.class, "serialNumber = ?", item.getSerialNumber()); //从数据库中删除数据
            int len = adapter.removeData(item); //从list中删除数据并返回总长度
            int position = holder.getLayoutPosition();
            adapter.notifyItemRemoved(position); //删除数据（注意下标不会自动纠正）
            adapter.notifyItemRangeChanged(position, len - position); //对指定范围内数据进行重新绘制（纠正下标问题）
            if (len == 0) {//如果列表种无数据，显示无内容的消息提示
                setListIsVisible(false);
            }
        });
        //取消按钮
        dialog.setNegativeButton("取消", (dialog2, which) -> {
        });
        //显示消息提示框
        dialog.show();
    }
}