package com.haiming.paper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.UserDao;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    private ImageView userImager;
    private EditText accountNumberEt;
    private EditText numberEt;
    private EditText inputPassword;
    private EditText determinePassword;
    private EditText inputEmail;
    private CheckBox cbMan;
    private CheckBox cbGirl;
    private Button registerBtn;
    private ImageView ivBack;
    private CheckBox cbAgree;
    private EditText mSignature;
    private UserDao mUserDao;
    private User mUser;
    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout_activity);
        initView();
    }


    private void initView() {
        userImager = findViewById(R.id.user_image);
        accountNumberEt = findViewById(R.id.account_number_et);
        numberEt = findViewById(R.id.number_et);
        inputPassword = findViewById(R.id.input_password);
        determinePassword = findViewById(R.id.determine_password);
        mSignature = findViewById(R.id.input_signature);
        inputEmail = findViewById(R.id.input_email);
        cbMan = findViewById(R.id.cb_man);
        cbGirl = findViewById(R.id.cb_girl);
        registerBtn = findViewById(R.id.register_btn);
        ivBack = findViewById(R.id.iv_back);
        cbAgree = findViewById(R.id.cb_agree);
        mUserDao = new UserDao(this);
        mUser = new User();

        mIntent = new Intent(this,LoginActivity.class);

        userImager.setOnClickListener(v->{
            checkPremission();
        });

        ivBack.setOnClickListener(v -> finish());

        cbMan.setOnClickListener(v -> {
            setCheckedFalse();
            cbMan.setChecked(true);
        });

        cbGirl.setOnClickListener(v -> {
            setCheckedFalse();
            cbGirl.setChecked(true);
        });

        registerBtn.setOnClickListener(v -> {
            if (checkMessage()) {
                mUser.setName(name);
                mUser.setNumber(number);
                mUser.setPassword(password);
                mUser.setIsManager(0);
                mUser.setSex(sex);
                mUser.setSignature(signature);
                mUser.setEmail(email);
                mUser.setImagePath(imagePath);
                mUserDao.insertUser(mUser);
                UIUtil.showToast(RegisterActivity.this, "注册成功");
                startActivity(mIntent);
                finish();
            }
        });

    }

    public void setCheckedFalse() {
        cbMan.setChecked(false);
        cbGirl.setChecked(false);
    }

    private String name;
    private String number;
    private String password;
    private String email="";
    private String sex = "男";
    private String signature = null;
    private String imagePath;

    /**
     * 檢查信息是否完善
     * @return
     */
    public boolean checkMessage() {
        name = accountNumberEt.getText().toString().trim();
        if (name.isEmpty()) {
            UIUtil.showToast(this, "请输入用户名");
            return false;
        }
        number = numberEt.getText().toString().trim();
        if (number.isEmpty()){
            UIUtil.showToast(this, "请输入账号");
            return false;
        }
        if (mUserDao != null){
            if(mUserDao.haveUserNum(number)){
                UIUtil.showToast(this, "账号已被注册");
                return false;
            }
        }
        if (inputPassword.getText().toString().equals(determinePassword.getText().toString())) {
            password = inputPassword.getText().toString().trim();
        } else {
            UIUtil.showToast(this, "两次输入密码不相等");
            return false;
        }

        if (!cbAgree.isChecked()) {
            UIUtil.showToast(this, "请同意注册");
            return false;
        }

        email = inputEmail.getText().toString();
        if (!email.isEmpty()) {
            if (!isEmail(email)) {
                UIUtil.showToast(this, "邮箱格式错误");
                return false;
            }
        }else {
            UIUtil.showToast(this, "邮箱不能为空");
            return false;
        }

        if (cbMan.isChecked()) {
            sex = "男";
        }
        if (cbGirl.isChecked()) {
            sex = "女";
        }

        signature = mSignature.getText().toString().trim();
        if (signature.isEmpty()){
            UIUtil.showToast(this, "个性签名不能为空");
            return false;
        }

        return true;
    }

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 检查权限
     */
    public void checkPremission() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                            addPicture();
                    } else {
                        UIUtil.showToast(this, "你拒绝了权限，无法加载图片");
                    }
                });
    }

    /**
     * /调用相册
     */
    public void addPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            insertImagesSync(data);
        }
    }

    /**
     * 根据选择的图片，把其转换了string，并传给控件，让其在applition的接口实现显示
     */
    private void insertImagesSync(final Intent data) {

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    String picString = UIUtil.bitmap2String(bm);
                    imagePath = picString;
                    emitter.onNext(imagePath);
                    c.close();
                    emitter.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }
            }
        })
                //.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        UIUtil.showToast(RegisterActivity.this, "设置头像失败");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String image) {
                        imagePath = image;
                        Glide.with(RegisterActivity.this).load(UIUtil.string2byte(image)).circleCrop().into(userImager);
                        Log.d("数据", "开始生成图片view");
                    }
                });
    }

}
