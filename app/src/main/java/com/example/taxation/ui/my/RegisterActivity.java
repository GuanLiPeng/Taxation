package com.example.taxation.ui.my;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taxation.R;
import com.example.taxation.bean.UserInfo;
import com.example.taxation.util.ConstantUtil;
import com.example.taxation.util.RegexUtil;
import com.example.taxation.util.StatusBarUtil;
import com.example.taxation.util.ToastUtil;

import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 注册页面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //界面控件
    EditText usernameView; //用户名
    TextView usernameError; //用户名错误提示
    EditText passwordView; //密码
    TextView passwordError; //密码错误提示
    EditText confirmPasswordView; //二次密码
    TextView confirmPasswordError; //二次密码错误提示
    Button registerButton;  //注册按钮

    //错误标志
    private boolean usernameErrorSign = false; //用户名错误标志
    private boolean passwordErrorSign = false; //密码错误标志
    private boolean confirmPasswordErrorSign = false; //二次密码的错误标志

    private final RegexUtil regex = new RegexUtil(); //正则工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_register);

        //初始化
        init();

        //输入框的实时判断功能
        usernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //重置错误内容
                usernameErrorSign = false;
                usernameError.setText(null);
                String usernameValues = usernameView.getText().toString();

                if (usernameValues.length() == ConstantUtil.USERNAME_LENGTH && !regex.isPhoneNumber(usernameValues)) {
                    usernameError.setText(getString(R.string.username_format_error));
                    usernameErrorSign = true;
                } else if (usernameValues.length() > ConstantUtil.USERNAME_LENGTH) {
                    usernameError.setText(getString(R.string.username_is_too_long_or_too_short));
                    usernameErrorSign = true;
                }
            }
        });
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //重置错误内容
                passwordErrorSign = false;
                passwordError.setText(null);
            }
        });
        confirmPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //重置错误内容
                confirmPasswordErrorSign = false;
                confirmPasswordError.setText(null);
            }
        });

        //按钮的点击事件
        registerButton.setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void init() {
        //改变状态栏
        StatusBarUtil.setLightMode(getWindow(), R.color.myWhite, true);

        //绑定控件
        usernameView = findViewById(R.id.register_username);
        usernameError = findViewById(R.id.username_error);
        passwordView = findViewById(R.id.register_password);
        passwordError = findViewById(R.id.password_error);
        confirmPasswordView = findViewById(R.id.confirm_password);
        confirmPasswordError = findViewById(R.id.confirm_password_error);
        registerButton = findViewById(R.id.register);
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (verifyInput()) { //判断输入项的错误，如果没有错误将为true
            String mUsername = usernameView.getText().toString();
            String mPassword = passwordView.getText().toString();
            new UserRegisterTask(RegisterActivity.this).execute(mUsername, mPassword);
        }
    }

    /**
     * 判断输入是否正确
     * 如果没有错误将返回true
     */
    private boolean verifyInput() {
        String usernameValues = usernameView.getText().toString();
        String passwordValues = passwordView.getText().toString();
        String confirmPasswordValues = confirmPasswordView.getText().toString();

        //部分错误提示不适合在输入过程中实时显示，在这里额外处理
        //用户名输入的检测
        if (!usernameErrorSign) { //先检测用户名输入框是否已经存在错误提示，如果存在则不再检查错误，以免防误提示的覆盖
            if (TextUtils.isEmpty(usernameValues)) {
                usernameError.setText(getString(R.string.username_is_empty));
                usernameErrorSign = true;
            } else if (usernameValues.length() != ConstantUtil.USERNAME_LENGTH) {
                usernameError.setText(getString(R.string.username_is_too_long_or_too_short));
                usernameErrorSign = true;
            }
        }

        //密码的输入检测
        if (TextUtils.isEmpty(passwordValues)) {
            passwordError.setText(getString(R.string.password_is_empty));
            passwordErrorSign = true;
        } else if (passwordValues.length() < ConstantUtil.PASSWORD_LENGTH) {
            passwordError.setText(getString(R.string.password_is_too_short));
            passwordErrorSign = true;
        }

        //再次输入密码的检测
        if (TextUtils.isEmpty(confirmPasswordValues)) {
            confirmPasswordError.setText(getString(R.string.password_is_empty));
            confirmPasswordErrorSign = true;
        } else if (!passwordValues.equals(confirmPasswordValues)) {
            confirmPasswordError.setText(getString(R.string.password_is_different));
            confirmPasswordErrorSign = true;
        }

        //将输入光标聚焦到第一个错误处
        errorViewFocus();

        //如果错误标记的值为true，说明输入框存在错误，这个函数就返回false
        return !(usernameErrorSign || passwordErrorSign || confirmPasswordErrorSign);
    }

    /**
     * 聚焦错误输入框
     * 当有错误时，将输入光标跳转到第一次出现错误的输入框中
     */
    private void errorViewFocus() {
        if (usernameErrorSign) {
            usernameView.requestFocus();
        } else if (passwordErrorSign) {
            passwordView.requestFocus();
        } else if (confirmPasswordErrorSign) {
            confirmPasswordView.requestFocus();
        }
    }

    /**
     * 开启登陆线程
     */
    private static class UserRegisterTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog; //加载框控件
        AlertDialog.Builder alertDialog; //消息提示控件

        private final WeakReference<RegisterActivity> activityWeakReference; // 使用弱引用来避免AsyncTask的溢出

        UserRegisterTask(RegisterActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RegisterActivity activity = activityWeakReference.get();
            //设置加载框的样式
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("注册中..."); //设置消息内容
            progressDialog.setCancelable(false); //设置是否能通过返回键关闭
            progressDialog.setCanceledOnTouchOutside(false); //设置触摸其他地方是否能关闭
            progressDialog.show(); //设置是否显示
            //设置消息提示框的样式
            alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setCancelable(true); //设置是否能通过返回键关闭
        }

        @Override
        protected String doInBackground(String... params) {
            try { //睡眠1.5秒再操作
                Thread.sleep(650L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String mUsername = params[0];
            String mPassword = params[1];
            List<UserInfo> users = LitePal
                    .where("username = ?", mUsername)
                    .limit(1)
                    .find(UserInfo.class);
            if (users.size() <= 0) {//如果查询结果不存在此数据，创建用户
                UserInfo user = new UserInfo();
                user.setUsername(mUsername);
                user.setPassword(mPassword);
                user.setLogin("false");
                user.save();
                return ConstantUtil.DB_OK;
            } else {//存在此数据，则不能创建用户
                return ConstantUtil.DB_FORBIDDEN;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String code) {
            super.onPostExecute(code);
            RegisterActivity activity = activityWeakReference.get();
            progressDialog.dismiss(); //关闭加载框

            switch (code) {
                case ConstantUtil.DB_OK:
                    //更新数据库信息
                    UserInfo user = new UserInfo();
                    user.setLogin("true");
                    user.updateAll("username = ?", code);

                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    activity.finish();
                    ToastUtil.showShortToast(activity.getApplicationContext(), "注册成功");
                    break;
                case ConstantUtil.DB_FORBIDDEN:
                    alertDialog.setMessage(activity.getString(R.string.register_failed)); //设置消息提示
                    alertDialog.setPositiveButton("确定", (dialogInterface, i) -> {
                    }).show();
                    break;
                default:
                    alertDialog.setMessage("未知错误").show(); //设置消息提示并显示
            }
        }
    }

}