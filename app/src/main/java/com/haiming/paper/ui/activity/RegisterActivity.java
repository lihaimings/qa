package com.haiming.paper.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class RegisterActivity extends BaseActivity {

    private EditText accountNumberEt;
    private EditText inputPassword;
    private EditText determinePassword;
    private EditText inputEmail;
    private CheckBox cbMan;
    private CheckBox cbGirl;
    private Button registerBtn;
    private ImageView ivBack;
    private CheckBox cbAgree;
    private EditText mSignature;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout_activity);
        initView();
    }




    private void initView() {
        accountNumberEt = findViewById(R.id.account_number_et);
        inputPassword = findViewById(R.id.input_password);
        determinePassword = findViewById(R.id.determine_password);
        mSignature = findViewById(R.id.input_signature);
        inputEmail = findViewById(R.id.input_email);
        cbMan = findViewById(R.id.cb_man);
        cbGirl = findViewById(R.id.cb_girl);
        registerBtn = findViewById(R.id.register_btn);
        ivBack = findViewById(R.id.iv_back);
        cbAgree = findViewById(R.id.cb_agree);




        ivBack.setOnClickListener(v->finish());

        cbMan.setOnClickListener(v->{
            setCheckedFalse();
            cbMan.setChecked(true);
        });

        cbGirl.setOnClickListener(v->{
            setCheckedFalse();
            cbGirl.setChecked(true);
        });

        registerBtn.setOnClickListener(v->{
            if(checkMessage()){
                UIUtil.showToast(this,"注册成功");
            }

        });

    }

    public void setCheckedFalse(){
        cbMan.setChecked(false);
        cbGirl.setChecked(false);
    }

    private String number;
    private String password;
    private String email;
    private String sex = "";
    private String signature ="";
    public boolean checkMessage(){
        number = accountNumberEt.getText().toString().trim();
        if(number.isEmpty()){
            UIUtil.showToast(this,"请输入用户名");
            return false;
        }
        if(inputPassword.getText().toString().equals(determinePassword.getText().toString())){
            password = inputPassword.getText().toString().trim();
        }else {
            UIUtil.showToast(this,"两次输入密码不相等");
            return false;
        }

        if(!cbAgree.isChecked()){
            UIUtil.showToast(this,"请同意注册");
            return false;
        }

        email = inputEmail.getText().toString();
        if(!email.isEmpty()){
            if(!isEmail(email)){
                UIUtil.showToast(this,"邮箱格式错误");
                return false;
            }
        }

        if (cbMan.isChecked()){
            sex = "男";
        }
        if(cbGirl.isChecked()){
            sex = "女";
        }

        signature = mSignature.getText().toString().trim();

        return true;
    }

    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
