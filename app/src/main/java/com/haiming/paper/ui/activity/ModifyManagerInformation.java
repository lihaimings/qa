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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.Manager;
import com.haiming.paper.db.ManagerDao;
import com.haiming.paper.db.UserData;
import com.haiming.paper.thread.ThreadManager;
import com.haiming.paper.ui.view.YEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ModifyManagerInformation extends BaseActivity {


    @BindView(R.id.user_image)
    ImageView userImage;
    @BindView(R.id.account_number_et)
    YEditText accountNumberEt;
    @BindView(R.id.input_password)
    YEditText inputPassword;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.view_status_bar)
    View viewStatusBar;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private Manager mManager;
    private ManagerDao mManagerDao;

    private String imagePath;

    private String number;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_manager_information);
        loadData();
        initView();
    }

    private void loadData() {
        mManager = new Manager();
        mManagerDao = new ManagerDao(this);
        ThreadManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                mManager = mManagerDao.queryManagerById(UserData.getUserId(ModifyManagerInformation.this));
            }
        });

    }

    private void initView() {
        ivBack.setOnClickListener(v->finish());
        viewStatusBar.getLayoutParams().height = UIUtil.getStatusBarHeight(this);

        String imagePath = mManager.getImagePath();
        Glide.with(this).load(UIUtil.string2byte(imagePath)).circleCrop().into(userImage);
        userImage.setOnClickListener(v->{
            checkPremission();
        });
        accountNumberEt.setText(mManager.getName());
        inputPassword.setText(mManager.getPassword());

        registerBtn.setOnClickListener(v->{
            if (checkMessage()) {
                mManager.setImagePath(imagePath);
                mManager.setName(number);
                mManager.setPassword(password);
                mManagerDao.updateManager(mManager);
                UIUtil.showToast(ModifyManagerInformation.this, "修改成功");
                finish();
            }
        });
        }

    private boolean checkMessage() {
        number = accountNumberEt.getText().toString().trim();
        if (number.isEmpty()) {
            UIUtil.showToast(this, "请输入管理员账号");
            return false;
        }
        if (!inputPassword.getText().toString().trim().isEmpty()) {
            password = inputPassword.getText().toString().trim();
        } else {
            UIUtil.showToast(this, "请输入密码");
            return false;
        }

        return true;
    }


    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ButterKnife.bind(this);
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
                        UIUtil.showToast(ModifyManagerInformation.this, "设置头像失败");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String image) {
                        imagePath = image;
                        Glide.with(ModifyManagerInformation.this).load(UIUtil.string2byte(image)).circleCrop().into(userImage);
                        Log.d("数据", "开始生成图片view");
                    }
                });
    }
}
