package com.example.taxation.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.taxation.R;
import com.example.taxation.bean.BidNewsItem;

import org.litepal.LitePal;

import java.util.List;

/**
 * 新闻点进去的页面
 */
public class NewsDetailsActivity extends AppCompatActivity {

    private TextView titleText, mainText, fromText, dateText;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_news_details);

        initView(); //初始化界面
        initData(); //初始化数据
    }

    /**
     * 初始化
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void initView() {
        //绑定控件
        titleText = findViewById(R.id.a_h_n_d_title);
        mainText = findViewById(R.id.a_h_n_d_content);
        fromText = findViewById(R.id.a_h_n_d_from);
        dateText = findViewById(R.id.a_h_n_d_date);
        image = findViewById(R.id.a_h_n_d_image_view);

        //点击事件
        ImageButton returnButton = findViewById(R.id.a_h_n_d_return_button);
        returnButton.setOnClickListener(v -> finish());
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("newsId", 0);
        if (id > 0) {
            BidNewsItem item = LitePal.find(BidNewsItem.class, id);
            titleText.setText(item.getTitle());
            mainText.setText(item.getMainInfo());
            fromText.setText(item.getFrom());
            dateText.setText(item.getDate());
            Glide.with(getApplicationContext()).load(item.getImgId()).centerCrop().into(image);
        }
    }
}