package com.haiming.paper.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.StringUtils;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.TextWatcherAdapter;
import com.haiming.paper.bean.Group;
import com.haiming.paper.bean.Note;
import com.haiming.paper.db.GroupDao;
import com.haiming.paper.db.NoteDao;
import com.sendtion.xrichtext.RichTextEditor;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youcheyihou.videolib.NiceUtil;
import com.youcheyihou.videolib.NiceVideoPlayerManager;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class QAAskQuestionActivity extends BaseActivity {

    private View bgHomeStatusBar;
    private ImageView ivBack;
    private TextView pageTitle;
    private EditText etInputQuestion;
    private RichTextEditor etInputQuestionDescription;
    private ImageView ivQuestionAddVideo;
    private ImageView ivQuestionAddPicture;
    private TextView tvHowAskQuestion;
    private Button rlInputAsk;
    private RelativeLayout pageTitleRl;

    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private int screenWidth;
    private int screenHeight;

    private String myTitle;
    private String myContent;
    private String myGroupName;
    private String myNoteTime;

    private Disposable subsLoading;
    private Disposable subsInsert;
    private static final int cutTitleLength = 20;//截取的标题长度

    // 分组
    private GroupDao groupDao;
    // 笔记的查删
    private NoteDao noteDao;
    // 笔记对象
    private Note note;
    // 区分是新建笔记还是编辑笔记
    private int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        initView();
    }

    private void initView() {
        bgHomeStatusBar = findViewById(R.id.bg_home_status_bar);
        pageTitle = findViewById(R.id.page_title);
        ivBack = findViewById(R.id.iv_back);
        etInputQuestion = findViewById(R.id.et_input_question);
        etInputQuestionDescription = findViewById(R.id.et_input_question_description);
        ivQuestionAddVideo = findViewById(R.id.iv_question_add_video);
        ivQuestionAddPicture = findViewById(R.id.iv_question_add_picture);
        tvHowAskQuestion = findViewById(R.id.tv_how_ask_question);
        rlInputAsk = findViewById(R.id.rl_reward_coin_bar);
        pageTitleRl = findViewById(R.id.page_title_rl);

        bgHomeStatusBar.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        ivBack.setOnClickListener(v -> finish());
        //点击后Hint自动消失
        etInputQuestion.setOnFocusChangeListener(UIUtil.onFocusAutoClearHintListener());
        etInputQuestionDescription.setOnFocusChangeListener(UIUtil.onFocusAutoClearHintListener());
        etInputQuestion.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                int length = s.toString().trim().length();
                if (length >= 50) {
                    UIUtil.showToast(QAAskQuestionActivity.this, "字数达到最大值啦");
                }
            }
        });

        ivQuestionAddPicture.setOnClickListener(v -> {
            closeSoftKeyInput();//关闭软键盘
            checkPremission(1);
        });

        ivQuestionAddVideo.setOnClickListener(v -> {
            closeSoftKeyInput();//关闭软键盘
            checkPremission(2);
        });

        groupDao = new GroupDao(this);
        noteDao = new NoteDao(this);
        note = new Note();


        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        try {
//            Intent intent = getIntent();
//            flag = intent.getIntExtra("flag", 0);//0新建，1编辑
            flag=1;
            if (flag == 1) {//编辑
                pageTitle.setText("编辑提问");
                // 拿到之前的提问内容
//                Bundle bundle = intent.getBundleExtra("data");
//                note = (Note) bundle.getSerializable("note");
                note=noteDao.queryNotes(21);

                if (note != null) {
                    myTitle = note.getTitle();
                    myContent = note.getContent();
                    myNoteTime = note.getCreateTime();
                    Group group = groupDao.queryGroupById(note.getGroupId());
//                    if (group != null) {
//                        myGroupName = group.getName();
//                        tv_new_group.setText(myGroupName);
//                    }

                    loadingDialog = new ProgressDialog(this);
                    loadingDialog.setMessage("数据加载中...");
                    loadingDialog.setCanceledOnTouchOutside(false);
                    loadingDialog.show();

//                    tv_new_time.setText(note.getCreateTime());
                    etInputQuestion.setText(note.getTitle());
                    etInputQuestionDescription.post(new Runnable() {
                        @Override
                        public void run() {
                            dealWithContent();
                        }
                    });
                }
            } else {
                pageTitle.setText("新增提问");
//                if (myGroupName == null || "全部笔记".equals(myGroupName)) {
////                    myGroupName = "默认笔记";
////                }
//                tv_new_group.setText(myGroupName); //分类名
                myNoteTime = CommonUtil.date2string(new Date());
//                tv_new_time.setText(myNoteTime);  //创建时间
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        rlInputAsk.setOnClickListener(v -> {
            saveNoteData(false);
            Log.d("数据", "点击了按钮");
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

    public void addVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    /**
     * 检查权限
     */
    public void checkPremission(int type) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (type == 1){
                            addPicture();
                        }else if (type == 2){
                            addVideo();
                        }

                    } else {
                        UIUtil.showToast(QAAskQuestionActivity.this, "你拒绝了权限，无法加载图片");
                    }
                });
    }

    /**
     * 异步展示正文内容
     */
    private void dealWithContent() {
        etInputQuestionDescription.clearAllLayout();
        showDataSync(note.getContent());

        // 图片删除事件
        etInputQuestionDescription.setOnRtImageDeleteListener(new RichTextEditor.OnRtImageDeleteListener() {

            @Override
            public void onRtImageDelete(String imagePath) {
                if (!TextUtils.isEmpty(imagePath)) {

                }
            }
        });
    }

    /**
     * 关闭软键盘
     */
    private void closeSoftKeyInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && imm.isActive() && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开软键盘
     */
    private void openSoftKeyInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && !imm.isActive() && etInputQuestionDescription != null) {
            etInputQuestionDescription.requestFocus();
            //第二个参数可设置为0
            //imm.showSoftInput(et_content, InputMethodManager.SHOW_FORCED);//强制显示
            imm.showSoftInputFromInputMethod(etInputQuestionDescription.getWindowToken(),
                    InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 异步方式显示正文内容数据
     */
    private void showDataSync(final String html) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                showEditData(emitter, html);
            }
        })
                //.onBackpressureBuffer()
                .subscribeOn(Schedulers.io())//生产事件在io
                .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        if (etInputQuestionDescription != null) {
                            //在图片全部插入完毕后，再插入一个EditText，防止最后一张图片后无法插入文字
                            //  etInputQuestionDescription.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (loadingDialog != null) {
                            loadingDialog.dismiss();
                        }
                        UIUtil.showToast(QAAskQuestionActivity.this, "解析错误：图片不存在或已损坏");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        subsLoading = d;
                    }

                    @Override
                    public void onNext(String text) {
                        try {
                            if (etInputQuestionDescription != null) {
                                if (text.contains("img=")) {
                                    String imagePath = StringUtils.getImgString(text);
                                    //  et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                    etInputQuestionDescription.addImageViewAtIndex(etInputQuestionDescription.getLastIndex(), imagePath);
                                } else if (text.contains("video=")) {
                                    String[] viedoPath = StringUtils.getVideoImage(text);
                                    if (viedoPath[1] != null){
                                        etInputQuestionDescription.imagePath=viedoPath[1];
                                        Log.d("数据","图片="+viedoPath[1]);
                                    }
                                    if (viedoPath[0] != null){
                                        etInputQuestionDescription.addVideoViewAtIndex(etInputQuestionDescription.getLastIndex(), viedoPath[0]);
                                        Log.d("数据","视频="+viedoPath[0]);
                                    }

                                } else {
                                    etInputQuestionDescription.addEditTextAtIndex(etInputQuestionDescription.getLastIndex(), text);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 将正文的string进行切割数据，并发送给观察者
     */
    protected void showEditData(ObservableEmitter<String> emitter, String html) {
        try {
            //这个方法是把string切割成list，然后把发送给onnext
            List<String> textList = StringUtils.spliteString(html);
            for (int i = 0; i < textList.size(); i++) {
                String text = textList.get(i);
                emitter.onNext(text);
            }
            emitter.onComplete();
        } catch (Exception e) {
            e.printStackTrace();
            emitter.onError(e);
        }
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     * 将正文内容生成一个string
     */
    private String getEditData() {
        StringBuilder content = new StringBuilder();
        try {
            List<RichTextEditor.EditData> editList = etInputQuestionDescription.buildEditData();
            for (RichTextEditor.EditData itemData : editList) {
                if (itemData.inputStr != null) {
                    content.append(itemData.inputStr).append("rofjy8427");
                } else if (itemData.imagePath != null) {
                    //为图片拼接
                    content.append("img=").append(itemData.imagePath).append("rofjy8427");
                } else if (itemData.videoPath != null) {
                    // 视频路径拼接
                    content.append("video=").append(itemData.videoPath).append("ry98jhf01").append(itemData.videoPath).append("rofjy8427");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     * 拿到全部数据，并提交给数据库
     */
    private void saveNoteData(boolean isBackground) {
        String noteTitle = etInputQuestion.getText().toString();
        String noteContent = getEditData();
        //分组名
        String groupName = "首页";
        //创建时间
//        String noteTime = tv_new_time.getText().toString();

        try {
            Group group = groupDao.queryGroupByName(groupName);
            if (group != null) {
                Log.d("数据", "group不为空");
                if (noteTitle.length() == 0) {//如果标题为空，则截取内容为标题
                    if (noteContent.length() > cutTitleLength) {
                        noteTitle = noteContent.substring(0, cutTitleLength);
                    } else if (noteContent.length() > 0) {
                        noteTitle = noteContent;
                    }
                }
                int groupId = group.getId();
                note.setTitle(noteTitle);
                note.setContent(noteContent);
                note.setGroupId(groupId);
                note.setGroupName(groupName);
                note.setType(2);
                //todo 不是白色
                note.setBgColor("#FFFFFF");
                note.setIsEncrypt(0);
                note.setCreateTime(CommonUtil.date2string(new Date()));
                note.setUpdateTime(CommonUtil.date2string(new Date()));
                //todo 还有用户id
                note.setUserId(1);
                note.setAnswerSize(0);
                note.setAnswerId(null);
                if (flag == 0) {//新建笔记
                    if (noteTitle.length() == 0 || noteContent.length() == 0) {
                        if (!isBackground) {
                            UIUtil.showToast(this, "请输入内容");
                        }
                    } else {
                        //提交到数据库
//                        long noteId = noteDao.insertNote(note);
//                        Log.i("数据", "笔记noteId: "+noteId);
                        //查询新建笔记id，防止重复插入
//                        note.setId((int) noteId);
                        noteDao.insertUser(note);
                        Log.i("数据", "已执行增加");
                        flag = 1;//插入以后只能是编辑
                        if (!isBackground) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            // todo finish();
                        }
                        UIUtil.showToast(this, "提交成功");
                    }
                } else if (flag == 1) {//编辑笔记
                    if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
//                            || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)
                    ) {
                        noteDao.updateNote(note);
                    }
                    if (!isBackground) {
                        finish();
                    }
                }
            } else {
                Log.d("数据", "group为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("数据", "开始获取图片");
            insertImagesSync(data);
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            insertVideo(data);
        }
    }

    private void insertVideo(Intent data) {
        insertDialog.setMessage("视频正在插入");
        insertDialog.show();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    etInputQuestionDescription.measure(0, 0);

                    Uri uri = data.getData();
                    ContentResolver cr = getContentResolver();
                    /** 数据库查询操作。
                     * 第一个参数 uri：为要查询的数据库+表的名称。
                     * 第二个参数 projection ： 要查询的列。
                     * 第三个参数 selection ： 查询的条件，相当于SQL where。
                     * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
                     * 第四个参数 sortOrder ： 结果排序。
                     */
                    Cursor cursor = cr.query(uri, null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            // 视频ID:MediaStore.Audio.Media._ID
                            int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                            // 视频名称：MediaStore.Audio.Media.TITLE
                            String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                            // 视频路径：MediaStore.Audio.Media.DATA
                            String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            // 视频时长：MediaStore.Audio.Media.DURATION
                            int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                            // 视频大小：MediaStore.Audio.Media.SIZE
                            long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                            // 视频缩略图路径：MediaStore.Images.Media.DATA
                            String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            // 缩略图ID:MediaStore.Audio.Media._ID
                            int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                            // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                            // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
                            Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

                            // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                            // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
                          //  Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                            // 如果追求更好的话可以利用 ThumbnailUtils.extractThumbnail 把缩略图转化为的制定大小
                            Bitmap bitmap= ThumbnailUtils.extractThumbnail(bitmap1, NiceUtil.getScreenWidth(QAAskQuestionActivity.this),(int) (NiceUtil.getScreenWidth(QAAskQuestionActivity.this)  * 9f / 16f) ,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                            emitter.onNext("thumss"+imagePath);
                            emitter.onNext(videoPath);
                            cursor.close();
                        }
                    }
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
                        if (insertDialog != null && insertDialog.isShowing()) {
                            insertDialog.dismiss();
                        }
                        UIUtil.showToast(QAAskQuestionActivity.this, "视频插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (insertDialog != null && insertDialog.isShowing()) {
                            insertDialog.dismiss();
                        }
                        UIUtil.showToast(QAAskQuestionActivity.this, "视频插入失败");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        subsInsert = d;
                    }

                    @Override
                    public void onNext(String imagePath) {
                        //在applition接口里实现
                        if (imagePath.contains("thumss")){
                            imagePath = StringUtils.getVideoString(imagePath);
                            etInputQuestionDescription.imagePath=imagePath;
                        }else {
                            etInputQuestionDescription.insertVideo(imagePath);
                        }

                    }
                });

    }

    /**
     * 根据选择的图片，把其转换了string，并传给控件，让其在applition的接口实现显示
     */
    private void insertImagesSync(final Intent data) {
        insertDialog.show();

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) {
                try {
                    etInputQuestionDescription.measure(0, 0);
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    Bitmap bm = BitmapFactory.decodeFile(imagePath);
                    String picString = UIUtil.bitmap2String(bm);
                    emitter.onNext(picString);
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
                        if (insertDialog != null && insertDialog.isShowing()) {
                            insertDialog.dismiss();
                        }
                        UIUtil.showToast(QAAskQuestionActivity.this, "图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (insertDialog != null && insertDialog.isShowing()) {
                            insertDialog.dismiss();
                        }
                        UIUtil.showToast(QAAskQuestionActivity.this, "图片插入失败");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        subsInsert = d;
                    }

                    @Override
                    public void onNext(String imagePath) {
                        //在applition接口里实现
                        etInputQuestionDescription.insertImage(imagePath);
                        Log.d("数据", "开始生成图片view");
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            //如果APP处于后台，或者手机锁屏，则保存数据
            if (CommonUtil.isAppOnBackground(getApplicationContext()) ||
                    CommonUtil.isLockScreeen(getApplicationContext())) {
              //  saveNoteData(true);//处于后台时保存数据
            }

            if (subsLoading != null && subsLoading.isDisposed()) {
                subsLoading.dispose();
            }
            if (subsInsert != null && subsInsert.isDisposed()) {
                subsInsert.dispose();
            }

            NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dealwithExit() {
        try {
            String noteTitle = etInputQuestion.getText().toString();
            String noteContent = getEditData();
//            String groupName = tv_new_group.getText().toString();
//            String noteTime = tv_new_time.getText().toString();
            if (flag == 0) {//新建笔记
                if (noteTitle.length() > 0 || noteContent.length() > 0) {
                    //saveNoteData(false);
                }
            } else if (flag == 1) {//编辑笔记
                if (!noteTitle.equals(myTitle) || !noteContent.equals(myContent)
                    //   || !groupName.equals(myGroupName) || !noteTime.equals(myNoteTime)
                ) {
                   // saveNoteData(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dealwithExit();
    }
}
