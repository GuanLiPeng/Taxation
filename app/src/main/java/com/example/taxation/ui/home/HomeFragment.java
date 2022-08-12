package com.example.taxation.ui.home;

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
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taxation.R;
import com.example.taxation.bean.BidInfoItem;
import com.example.taxation.bean.BidNewsItem;
import com.example.taxation.adapter.BaseViewHolder;
import com.example.taxation.adapter.CommonBaseAdapter;
import com.example.taxation.util.ToastUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页 碎片页面
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    //界面控件
    private TextView recommendButton, newsButton; //资讯 和 推荐
    private View newsDot, bidDot;
    private RecyclerView recyclerViewNews, recyclerViewBid; //card样式列表 和 news样式列表
    //变量
    private int currentPosition = R.id.home_classification_news; //资讯 和 推荐的点击状态
    private final List<BidNewsItem> newsList = new ArrayList<>(); //新闻列表
    private final List<BidInfoItem> infoList = new ArrayList<>(); //招标项目列表


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view); //初始化界面
        initData(); //加载列表数据
        ChangeButtonState(view, currentPosition); //默认当前展示的是资讯列表


        //搜索的点击事件
        SearchView searchView = view.findViewById(R.id.home_search_view);
        searchView.setIconifiedByDefault(false); //设置搜索默认为展开状态
        searchView.setQueryHint("请输入项目名称");//设置默认无内容时的文字提示
        searchView.setSubmitButtonEnabled(true); //显示提交按钮
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<BidInfoItem> queryResult = LitePal
                        .where("info like ?", "%" + query + "%")
                        .find(BidInfoItem.class); //获取首页列表的信息
                if (queryResult.size() > 0) {
                    //提交按钮的点击事件
                    Intent intent = new Intent(getContext(), BidListActivity.class);
                    intent.putExtra("title", "搜索结果");
                    intent.putExtra("query", query);
                    startActivity(intent);
                } else {
                    ToastUtil.showShortToast(view.getContext(), "未查询到数据！");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //当前输入框内容改变时的回调
                return false;
            }
        });


        //装载新闻列表的适配器
        LinearLayoutManager layoutNewsManager = new LinearLayoutManager(getContext());
        recyclerViewNews.setLayoutManager(layoutNewsManager);
        CommonBaseAdapter<BidNewsItem> newsAdapter = new CommonBaseAdapter<>(newsList,
                new CommonBaseAdapter.OnBindDataInterface<BidNewsItem>() {
                    @Override
                    public int getItemLayoutId(int viewType) {
                        return R.layout.list_item_news;
                    }

                    @Override
                    public void onBindData(BidNewsItem item, BaseViewHolder holder, int type) {
                        holder.setText(R.id.l_i_n_title, item.getTitle());
                        holder.setText(R.id.l_i_n_from, item.getFrom());
                        holder.setText(R.id.l_i_n_date, item.getDate());
                        holder.setImage(R.id.l_i_n_mini_image_view, item.getImgId());
                        holder.setImage(R.id.l_i_n_image_radius, R.drawable.bg_image_radius_home);
                        holder.setOnClickListener(R.id.l_i_n_all_view, v -> {
                            Intent intent = new Intent(getContext(), NewsDetailsActivity.class);
                            intent.putExtra("newsId", item.getId());
                            startActivity(intent);
                        });
                    }
                });
        recyclerViewNews.setAdapter(newsAdapter);
        recyclerViewNews.setNestedScrollingEnabled(false); //隐藏滚动条


        //装载招标列表的适配器
        LinearLayoutManager layoutBidManager = new LinearLayoutManager(getContext());
        recyclerViewBid.setLayoutManager(layoutBidManager);
        CommonBaseAdapter<BidInfoItem> infoAdapter = new CommonBaseAdapter<>(infoList,
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
                            Intent intent = new Intent(getContext(), BidDetailsActivity.class);
                            intent.putExtra("type", "首页");
                            intent.putExtra("serialNumber", item.getSerialNumber());
                            startActivity(intent);
                        });
                    }
                });
        recyclerViewBid.setAdapter(infoAdapter);
        recyclerViewBid.setNestedScrollingEnabled(false); //隐藏滚动条

        return view;
    }

    /**
     * 初始化
     */
    private void initView(View view) {
        //绑定控件
        ImageView constructionButton = view.findViewById(R.id.home_construction_button);
        ImageView medicalButton = view.findViewById(R.id.home_medical_hygiene_button);
        ImageView environmentalButton = view.findViewById(R.id.home_environmental_protection_button);
        ImageView informationButton = view.findViewById(R.id.home_information_construction_button);
        ImageView moreButton = view.findViewById(R.id.home_more_button);
        newsButton = view.findViewById(R.id.home_classification_news); //资讯
        recommendButton = view.findViewById(R.id.home_classification_bid); //推荐
        bidDot = view.findViewById(R.id.home_classification_bid_dot_view);
        newsDot = view.findViewById(R.id.home_classification_news_dot_view);
        recyclerViewNews = view.findViewById(R.id.home_recycler_view_news); //新闻列表
        recyclerViewBid = view.findViewById(R.id.home_recycler_view_bid); //招标列表

        //其他按钮的点击事件
        constructionButton.setOnClickListener(this);
        medicalButton.setOnClickListener(this);
        environmentalButton.setOnClickListener(this);
        informationButton.setOnClickListener(this);
        moreButton.setOnClickListener(this);
        newsButton.setOnClickListener(this); //资讯的点击事件
        recommendButton.setOnClickListener(this); //推荐的点击事件
    }

    /**
     * 重新加载列表数据
     */
    public void initData() {
        //检查cardList中是否有数据，有的话就先清空内容再存放
        if (infoList.size() <= 0) {
            List<BidInfoItem> info = LitePal.findAll(BidInfoItem.class);
            infoList.addAll(info);
        }
        if (newsList.size() <= 0) {
            List<BidNewsItem> news = LitePal.findAll(BidNewsItem.class);
            newsList.addAll(news);
        }
    }

    /**
     * 主页面的点击事件
     *
     * @param v
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), BidListActivity.class);
        switch (v.getId()) {
            case R.id.home_construction_button:
                intent.putExtra("title", "工程建设");
                startActivity(intent);
                break;
            case R.id.home_medical_hygiene_button:
                intent.putExtra("title", "医疗卫生");
                startActivity(intent);
                break;
            case R.id.home_environmental_protection_button:
                intent.putExtra("title", "环保绿化");
                startActivity(intent);
                break;
            case R.id.home_information_construction_button:
                intent.putExtra("title", "信息建设");
                startActivity(intent);
                break;
            case R.id.home_more_button: //更多 的点击事件
                intent = new Intent(getContext(), MoreSortActivity.class);
                startActivity(intent);
                break;
            case R.id.home_classification_news: //资讯的点击事件
                if (currentPosition != R.id.home_classification_news) {
                    currentPosition = R.id.home_classification_news; //修改标志位
                    ChangeButtonState(v, currentPosition); //修改当前高亮的是 资讯按钮
                }
                break;
            case R.id.home_classification_bid: //推荐的点击事件
                if (currentPosition != R.id.home_classification_bid) {
                    currentPosition = R.id.home_classification_bid; //修改标志位
                    ChangeButtonState(v, currentPosition); //修改当前高亮的是 推荐按钮
                }
                break;
        }
    }

    /**
     * 改变按钮状态
     * 推荐 和 资讯 两个文字之间颜色变换
     *
     * @param v
     */
    public void ChangeButtonState(View v, int id) {
        if (id == R.id.home_classification_news) {
            //资讯按钮和它的下标 高亮
            newsButton.setTextColor(ContextCompat.getColor(v.getContext(), R.color.myBlueDark));
            newsDot.setVisibility(View.VISIBLE);
            //推荐按钮和它的下标 变灰
            recommendButton.setTextColor(ContextCompat.getColor(v.getContext(), R.color.myTextGray));
            bidDot.setVisibility(View.INVISIBLE);
            //调整要显示的列表
            recyclerViewNews.setVisibility(View.VISIBLE);
            recyclerViewBid.setVisibility(View.GONE);
        } else {
            //资讯按钮和它的下标 变灰
            newsButton.setTextColor(ContextCompat.getColor(v.getContext(), R.color.myTextGray));
            newsDot.setVisibility(View.INVISIBLE);
            //推荐按钮和它的下标 高亮
            recommendButton.setTextColor(ContextCompat.getColor(v.getContext(), R.color.myBlueDark));
            bidDot.setVisibility(View.VISIBLE);
            //调整要显示的列表
            recyclerViewNews.setVisibility(View.GONE);
            recyclerViewBid.setVisibility(View.VISIBLE);
        }
    }
}
