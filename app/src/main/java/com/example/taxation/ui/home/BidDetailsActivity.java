package com.example.taxation.ui.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.taxation.R;
import com.example.taxation.bean.BidInfoItem;
import com.example.taxation.bean.InquireInfoItem;
import com.example.taxation.bean.UserInfo;
import com.example.taxation.ui.my.InquireActivity;
import com.example.taxation.ui.my.LoginActivity;

import org.litepal.LitePal;

/**
 * 招标项目的详情页面（申请的招标项目、流标中标项目等都使用此页面）
 */
public class BidDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView titleText, fromText, dateText, contentText;
    private AppCompatButton applyBidButton;

    private boolean isApply = false; //是否提交过信息，false为未提交，true为已提交
    private boolean isLogin = false; //是否登录标记，false为未登录

    private String type, serialNumber; //intent传递过来的类型 和 编号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_bid_list_details);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        serialNumber = intent.getStringExtra("serialNumber");

        initView(); //初始化界面
        initData(); //初始化数据
    }

    /**
     * 生命周期：在活动由停止状态变为运行状态之前调用(活动重启)
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        InquireInfoItem inquireInfo = LitePal
                .where("serialNumber = ?", serialNumber)
                .findFirst(InquireInfoItem.class);
        isApply = (inquireInfo != null); //当前是否是申请过的状态？
        updateUIStatus();  //更新按钮状态
    }

    /**
     * 从A页面使用startActivityForResult()跳转到B页面，B页面点击按钮后将值传回到A页面并加载
     *
     * @param requestCode 请求代码
     * @param resultCode  返回代码
     * @param data        Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            if (data != null) {
                type = data.getStringExtra("type");
                serialNumber = data.getStringExtra("serialNumber");
                initData(); //初始化数据
                isApply = isLogin = true; //如果得到200的返回码，那必然是 登录+已申请 的状态
                updateUIStatus();  //更新UI状态
            }
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //绑定控件
        titleText = findViewById(R.id.a_h_b_l_d_title);
        fromText = findViewById(R.id.a_h_b_l_d_address);
        dateText = findViewById(R.id.a_h_b_l_d_date);
        contentText = findViewById(R.id.a_h_b_l_d_content);
        applyBidButton = findViewById(R.id.a_h_b_l_d_apply_bid_button);
        AppCompatButton myBidButton = findViewById(R.id.a_h_b_l_d_my_bid_button);
        ImageButton returnButton = findViewById(R.id.a_h_b_l_d_return_button);
        returnButton.setOnClickListener(v -> finish()); //返回

        //是否显示按钮
        if ("首页".equals(type) || "招标查询".equals(type)) { //type值是首页或招标查询时，显示底部的按钮
            myBidButton.setVisibility(View.VISIBLE); //显示底部按钮
            applyBidButton.setVisibility(View.VISIBLE);
            myBidButton.setOnClickListener(this);
            applyBidButton.setOnClickListener(this);
            //检查申请招标按钮的样式
            UserInfo loginUser = LitePal.where("login = ?", "true").findFirst(UserInfo.class); //获取用户信息
            isLogin = (loginUser != null); //当前是否是登录状态？
            if (isLogin) { //如果登录，再查询列表信息
                InquireInfoItem inquireInfo = LitePal
                        .where("serialNumber = ?", serialNumber)
                        .findFirst(InquireInfoItem.class);
                isApply = (inquireInfo != null); //当前是否是申请过的状态？
            }
            updateUIStatus();  //更新按钮状态
        } else { //否则就是中标、流标、资审页面，不显示底部按钮
            myBidButton.setVisibility(View.GONE);
            applyBidButton.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //加载招标信息
        if ("首页".equals(type)) {
            BidInfoItem item = LitePal.where("serialNumber = ?", serialNumber).findFirst(BidInfoItem.class);
            if (item != null) {
                titleText.setText(item.getInfo());
                fromText.setText(item.getAddress());
                dateText.setText(item.getDate());
                contentText.setText(item.getDetailInfo());
            }
        } else {
            InquireInfoItem item = LitePal.where("serialNumber = ?", serialNumber).findFirst(InquireInfoItem.class);
            if (item != null) {
                titleText.setText(item.getInfo());
                fromText.setText(item.getAddress());
                dateText.setText(item.getDate());
                contentText.setText(item.getDetailInfo());
            }
        }
    }

    /**
     * 更新当前界面状态：是否登录？ 是否已提交申请？
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateUIStatus() {
        if (isLogin && isApply) { //只有在登录+已申请状态，界面才会显示取消申请按钮，否则统一显示"申请此项目"样式
            applyBidButton.setText("取消申请");
            applyBidButton.setBackground(getDrawable(R.drawable.bg_apply_bid_button_gray_gradient));
        } else {
            applyBidButton.setText("申请此项目");
            applyBidButton.setBackground(getDrawable(R.drawable.bg_apply_bid_button_blue_gradient));
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        //如果未登录，弹出登录提醒
        if (!isLogin) {
            loginDialog();
            return;
        }

        switch (v.getId()) {
            //我的招标 点击事件
            case R.id.a_h_b_l_d_my_bid_button:
                Intent intent = new Intent(v.getContext(), InquireActivity.class);
                intent.putExtra("from", "BidDetailsActivity");
                intent.putExtra("type", "招标查询");
                startActivityForResult(intent, 100);
                break;
            //申请招标 点击事件
            case R.id.a_h_b_l_d_apply_bid_button:
                if (isApply) { //若是true，表示已申请，点击后反转状态
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setMessage("是否撤销申请？");
                    //确认撤销 按钮
                    dialog.setPositiveButton("确认撤销", (dialog1, which) -> {
                        isApply = false;
                        updateUIStatus(); //更新底部按钮UI
                        LitePal.deleteAll(InquireInfoItem.class, "serialNumber = ?", serialNumber); //删除数据
                    });
                    //取消按钮
                    dialog.setNegativeButton("取消", (dialog2, which) -> {
                    });
                    //显示消息提示框
                    dialog.show();
                } else {
                    //提交申请数据
                    isApply = true;
                    BidInfoItem item = LitePal.where("serialNumber = ?", serialNumber).findFirst(BidInfoItem.class);
                    InquireInfoItem inquireInfoItem = new InquireInfoItem("招标查询", false, item);
                    inquireInfoItem.save();
                    updateUIStatus(); //更新底部按钮UI
                }
                break;
        }
    }

    /**
     * 登录提示的弹窗
     */
    private void loginDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("当前未登录，登录后可查看");
        //去登录按钮
        dialog.setPositiveButton("去登录", (dialog1, which) -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        //取消按钮
        dialog.setNegativeButton("取消", (dialog2, which) -> {
        });
        dialog.show(); //显示消息提示框
    }
}