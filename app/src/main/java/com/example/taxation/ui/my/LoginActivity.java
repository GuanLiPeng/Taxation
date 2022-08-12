package com.example.taxation.ui.my;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taxation.MainActivity;
import com.example.taxation.R;
import com.example.taxation.bean.UserInfo;
import com.example.taxation.util.ConstantUtil;
import com.example.taxation.util.RegexUtil;
import com.example.taxation.util.StatusBarUtil;
import com.example.taxation.util.ToastUtil;

import org.litepal.LitePal;

import java.lang.ref.WeakReference;
import java.util.List;

import static org.litepal.crud.DataSupport.findFirst;

/**
 * 登录页面
 */
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, TextView.OnEditorActionListener {

    //界面控件
    EditText usernameView; //用户名输入框
    TextView usernameError; //密码输入框
    EditText passwordView; //用户名错误提示文本
    TextView passwordError; //密码错误提示文本
    Button loginButton; //跳转到注册界面的文字提示
    TextView toRegister; //登录按钮

    private boolean usernameErrorSign = false; //用户名错误标志
    private boolean passwordErrorSign = false; //密码错误标志

    private final RegexUtil regex = new RegexUtil(); //正则工具类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_login);

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

                if ((usernameValues.length() == ConstantUtil.USERNAME_LENGTH) && !regex.isPhoneNumber(usernameValues)) {
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

        passwordView.setOnEditorActionListener(this); //密码输入框的键盘监听事件

        loginButton.setOnClickListener(this); //登录的点击事件
        toRegister.setOnClickListener(this); //注册的点击事件
    }

    /**
     * 初始化
     */
    private void init() {
        //改变状态栏
        StatusBarUtil.setLightMode(getWindow(), R.color.myWhite, true);

        //绑定控件
        usernameView = findViewById(R.id.login_username);
        usernameError = findViewById(R.id.username_error);
        passwordView = findViewById(R.id.login_password);
        passwordError = findViewById(R.id.password_error);
        loginButton = findViewById(R.id.login);
        toRegister = findViewById(R.id.text_register);
    }

    /**
     * 软键盘的监听事件
     *
     * @param textView findViewById(R.id.XXX)
     * @param id
     * @param keyEvent
     * @return 返回true表示方法执行了，false表示方法未执行
     */
    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    /**
     * 点击事件
     *
     * @param view findViewById(R.id.XXX)
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                attemptLogin();
                break;
            case R.id.text_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
        }

    }

    /**
     * 尝试登陆
     * 对用户的输入进行检测，当没有错误后才会开启登陆线程
     */
    private void attemptLogin() {
        String usernameValues = usernameView.getText().toString();
        String passwordValues = passwordView.getText().toString();

        //解决输入的错误提示，并将输入框聚焦到第一个错误处
        errorViewFocus();

        //部分错误提示不适合在输入过程中显示，在这里额外处理
        if (TextUtils.isEmpty(usernameValues)) {
            usernameError.setText(getString(R.string.username_is_empty));
            usernameErrorSign = true;
        } else if (usernameValues.length() < ConstantUtil.USERNAME_LENGTH) {
            usernameError.setText(getString(R.string.username_is_too_long_or_too_short));
            usernameErrorSign = true;
        }
        if (TextUtils.isEmpty(passwordValues)) {
            passwordError.setText(getString(R.string.password_is_empty));
            passwordErrorSign = true;
        } else if (passwordValues.length() < ConstantUtil.PASSWORD_LENGTH) {
            passwordError.setText(getString(R.string.password_is_too_short));
            passwordErrorSign = true;
        }
        errorViewFocus();

        //如果没有错误，登陆账号
        if (!usernameErrorSign && !passwordErrorSign) {
            new UserLoginTask(LoginActivity.this).execute(usernameValues, passwordValues);
        }
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
        }
    }


    /**
     * 开启登陆线程
     */
    private static class UserLoginTask extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog; //加载框控件
        AlertDialog.Builder alertDialog; //消息提示控件

        private final WeakReference<LoginActivity> activityWeakReference; // 使用弱引用来避免AsyncTask的溢出

        UserLoginTask(LoginActivity context) {
            activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoginActivity activity = activityWeakReference.get();
            //设置加载框的样式
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("登录中..."); //设置消息内容
            progressDialog.setCancelable(false); //设置是否能通过返回键关闭
            progressDialog.setCanceledOnTouchOutside(false); //设置触摸其他地方是否能关闭
            progressDialog.show(); //设置是否显示
            //设置消息提示框的样式
            alertDialog = new AlertDialog.Builder(activity);
            alertDialog.setCancelable(true); //设置是否能通过返回键关闭
        }

        @Override
        protected String doInBackground(String... params) {
            try { //睡眠650ms
                Thread.sleep(650L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String mUsername = params[0];
            String mPassword = params[1];
            List<UserInfo> users = LitePal.where("username = ?", mUsername)
                    .limit(1)
                    .find(UserInfo.class);
            if (users.size() > 0 && mPassword.equals(users.get(0).getPassword())) {
                //存在此账户且密码正确的处理操作
                return ConstantUtil.DB_OK + "," + users.get(0).getUsername();
            } else {
                //账户或密码其中之一匹配不上或没有这个账户，统一返回未找到
                return ConstantUtil.DB_NOT_FOUND;
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LoginActivity activity = activityWeakReference.get();
            progressDialog.dismiss(); //关闭加载框

            String[] code = s.split(",");
            switch (code[0]) {
                case ConstantUtil.DB_OK:
                    //更新数据库信息
                    UserInfo user = new UserInfo();
                    user.setLogin("true");
                    user.updateAll("username = ?", code[1]);

                    activity.startActivity(new Intent(activity, MainActivity.class));
                    ToastUtil.showShortToast(activity.getApplicationContext(), "登录成功");
                    break;
                case ConstantUtil.DB_NOT_FOUND:
                    alertDialog.setMessage(activity.getString(R.string.login_failed)); //设置消息提示
                    alertDialog.setPositiveButton("确定", (dialogInterface, i) -> {
                    }).show();
                    break;
                default:
                    alertDialog.setMessage("未知错误").show(); //设置消息提示并显示
            }

        }
    }
}
