package com.haiming.paper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginRegisterActivity extends BaseActivity {

    private Button loginBtn;
    private Button registerBtn;
    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.begin);

        initView();
    }



    private void initView() {
        loginBtn = findViewById(R.id.login_btn);
        registerBtn = findViewById(R.id.register_btn);

        registerBtn.post(new Runnable() {
            @Override
            public void run() {
                registerBtn.setSelected(true);
            }
        });
        loginBtn.setOnClickListener(v -> {
            clickView(loginBtn,1);
        });

        registerBtn.setOnClickListener(v -> {
            clickView(registerBtn,2);
        });
    }

    public void clickView(Button view,int tag){
        loginBtn.setSelected(false);
        registerBtn.setSelected(false);
        loginBtn.setTextColor(ContextCompat.getColor(this,R.color.black));
        registerBtn.setTextColor(ContextCompat.getColor(this,R.color.black));

        view.setSelected(true);
        view.setTextColor(ContextCompat.getColor(this,R.color.white));

        if(tag == 1){
            mIntent = new Intent(this,LoginActivity.class);
        }else {
            mIntent = new Intent(this,RegisterActivity.class);
        }
        startActivity(mIntent);
    }

}