package com.haiming.paper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    private ImageView loginIv;
    private EditText accountNumberEt;
    private EditText passwordNumberEt;
    private LinearLayout loginLl;
    private Button userLoginInputBtn;
    private Button managerLoginInputBtn;
    private Button loginGotoRegister;
    private CheckBox mCheckBox;
    private Intent mIntent;
    private String number;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout_activity);
        ButterKnife.bind(this);
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

        close.setOnClickListener(v->finish());

        userLoginInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(initData()){
                    UIUtil.showToast(LoginActivity.this,"登陆成功");
                }

            }
        });

        managerLoginInputBtn.setOnClickListener(v->{
            if(initData()){
                UIUtil.showToast(this,"管理员登陆成功");
            }

        });

    }


    private boolean initData() {
        number = accountNumberEt.getText().toString();
        password = passwordNumberEt.getText().toString();
        if(number.isEmpty()){
            UIUtil.showToast(LoginActivity.this,"请输入账号");
            return false;
        }
        if(password.isEmpty()){
            UIUtil.showToast(LoginActivity.this,"请输入密码");
            return false;
        }
        if (!mCheckBox.isChecked()){
            UIUtil.showToast(LoginActivity.this,"请勾选同意登陆");
            return false;
        }
        return true;
    }



}
