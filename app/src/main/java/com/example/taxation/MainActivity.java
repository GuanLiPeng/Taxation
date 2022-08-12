package com.example.taxation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taxation.bean.BidInfoItem;
import com.example.taxation.bean.BidNewsItem;
import com.example.taxation.ui.home.HomeFragment;
import com.example.taxation.ui.my.MyFragment;
import com.example.taxation.ui.statistics.FinanceFragment;
import com.example.taxation.util.JsonUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.LitePal;

import java.util.List;

/**
 * 注意：如果向json文件中手动添加了数据但app中没有显示，请卸载重装app，因为代码中没有设置新数据的更新
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private int currentItem; //保存当前页面的id，用来防止在当前页面点击其对应的底部按钮时会重新加载的情况

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        initData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        //创建底部导航栏
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        //设置首页为默认页面
        currentItem = R.id.navigation_home;
        replaceFragment(new HomeFragment());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //如果数据库中没 招标 数据，就获取本地json数据并保存到数据库中
        BidInfoItem info = LitePal.findFirst(BidInfoItem.class);
        if (info == null) { //如果数据库中没有数据，就获取本地json数据并保存到数据库中
            String cardJsonData = JsonUtil.readLocalJson(getApplicationContext(), "BidInfo.json");
            List<BidInfoItem> infoJson = new Gson().fromJson(cardJsonData, new TypeToken<List<BidInfoItem>>() {
            }.getType());
            if (infoJson.size() > 0) {
                LitePal.saveAll(infoJson); //当json中有获取到数据时，才会向数据库存放
            }
        }

        //如果数据库中没 新闻 数据，就获取本地json数据并保存到数据库中
        BidNewsItem news = LitePal.findFirst(BidNewsItem.class);
        if (news == null) {
            int[] images = {
                    R.drawable.img_news_1, R.drawable.img_news_2, R.drawable.img_news_3, R.drawable.img_news_4,
                    R.drawable.img_news_5, R.drawable.img_news_6, R.drawable.img_news_7, R.drawable.img_news_8,
                    R.drawable.img_news_9, R.drawable.img_news_10
            };
            int imgLen = images.length - 1;
            String newsJsonData = JsonUtil.readLocalJson(getApplicationContext(), "BidNews.json");
            List<BidNewsItem> newsJson = new Gson().fromJson(newsJsonData, new TypeToken<List<BidNewsItem>>() {
            }.getType());
            int imgIndex = 0;
            for (BidNewsItem item : newsJson) {
                imgIndex++;
                item.setImgId(images[imgIndex % imgLen]);
            }
            if (newsJson.size() > 0) {
                LitePal.saveAll(newsJson);
            }
        }
    }

    /**
     * 动态添加碎片
     *
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout, fragment);
        transaction.commit();
    }

    /**
     * 底部导航栏的点击事件
     *
     * @param item R.id.XXX
     * @return 跳转成功返回true，否则返回false
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                if (currentItem != R.id.navigation_home) {
                    replaceFragment(new HomeFragment());
                    currentItem = R.id.navigation_home;
                }
                return true;
            case R.id.navigation_statistics:
                if (currentItem != R.id.navigation_statistics) {
                    replaceFragment(new FinanceFragment());
                    currentItem = R.id.navigation_statistics;
                }
                return true;
            case R.id.navigation_my:
                if (currentItem != R.id.navigation_my) {
                    replaceFragment(new MyFragment());
                    currentItem = R.id.navigation_my;
                }
                return true;
        }
        return false;
    }

}