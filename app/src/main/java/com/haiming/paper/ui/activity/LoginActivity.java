package com.haiming.paper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.db.ManagerDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.db.UserData;
import com.haiming.paper.ui.MainActivity;

import androidx.annotation.Nullable;

public class LoginActivity extends BaseActivity {

    private ImageView loginIv;
    private EditText accountNumberEt;
    private EditText passwordNumberEt;
    private Button userLoginInputBtn;
    private Button managerLoginInputBtn;
    private CheckBox mCheckBox;
    private Intent mIntent;
    private String number;
    private String password;
    private UserDao mUserDao;
    private ManagerDao mManagerDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout_activity);
        initView();

    }


    private void initView() {
        loginIv = findViewById(R.id.login_iv);
        accountNumberEt = findViewById(R.id.account_number_et);
        passwordNumberEt = findViewById(R.id.password_number_et);
        userLoginInputBtn = findViewById(R.id.user_login_input_btn);
        managerLoginInputBtn = findViewById(R.id.manager_login_input_btn);
        mCheckBox = findViewById(R.id.cb_agree);
        ImageView close = findViewById(R.id.iv_back);

        mUserDao = new UserDao(this);
        mManagerDao = new ManagerDao(this);
        mIntent = new Intent(this, MainActivity.class);

        close.setOnClickListener(v -> finish());
        userLoginInputBtn.setOnClickListener(v -> {
            if (initData()) {
                int id = mUserDao.userLogin(number, password);
                Log.d("数据","用户登陆的id="+id);
                if(id != 0){
                    UserData.saveUserId(this,id);
                    UserData.saveIsManager(this,false);
                    Log.d("数据","保存了用户Id");
                    UIUtil.showToast(this, "登陆成功");
                    startActivity(mIntent);
                    finish();
                }else {
                    UIUtil.showToast(this, "账号或密码不正确");
                }

            }

        });

        managerLoginInputBtn.setOnClickListener(v -> {
            if (initData()) {
                int id = mManagerDao.managerLogin(number, password);
                if(id > 0){
                    UserData.saveUserId(this,id);
                    UserData.saveIsManager(this,true);
                    UIUtil.showToast(this, "管理员登陆成功");
                    startActivity(mIntent);
                }else {
                    UIUtil.showToast(this, "账号或密码不正确");
                }
            }

        });

    }


    private boolean initData() {
        number = accountNumberEt.getText().toString();
        password = passwordNumberEt.getText().toString();
        if (number.isEmpty()) {
            UIUtil.showToast(LoginActivity.this, "请输入账号");
            return false;
        }
        if (password.isEmpty()) {
            UIUtil.showToast(LoginActivity.this, "请输入密码");
            return false;
        }
        if (!mCheckBox.isChecked()) {
            UIUtil.showToast(LoginActivity.this, "请勾选同意登陆");
            return false;
        }
        return true;
    }


}
