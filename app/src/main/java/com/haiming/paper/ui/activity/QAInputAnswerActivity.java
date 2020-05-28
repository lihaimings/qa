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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.StringUtils;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.db.AnswerDao;
import com.haiming.paper.db.UserData;
import com.hjq.toast.ToastUtils;
import com.sendtion.xrichtext.RichTextEditor;
import com.sendtion.xrichtext.RichTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youcheyihou.videolib.NiceUtil;
import com.youcheyihou.videolib.NiceVideoPlayerManager;

import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QAInputAnswerActivity extends BaseActivity {
    private View bgHomeStatusBar;
    private ImageView ivBack;
    private TextView pageTitle;
    private EditText etInputQuestion;
    private RichTextEditor etInputQuestionDescription;
    private ImageView ivQuestionAddVideo;
    private ImageView ivQuestionAddPicture;
    private Button rlInputAsk;
    private RelativeLayout pageTitleRl;
    private TextView questionTitle;
    private RichTextView questionDescription;

    private ProgressDialog loadingDialog;
    private ProgressDialog insertDialog;
    private Note mNote = new Note();
    private int flag = 0;

    private AnswerDao mAnswerDao = new AnswerDao(this);

    private Answer mAnswer =new Answer();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_answer_activity);
        initView();
    }

    private void initView() {
        bgHomeStatusBar = findViewById(R.id.bg_home_status_bar);
        pageTitle = findViewById(R.id.page_title);
        ivBack = findViewById(R.id.iv_back);
        questionTitle = findViewById(R.id.tv_problem_title);
        questionDescription = findViewById(R.id.tv_problem_description);
        etInputQuestionDescription = findViewById(R.id.et_input_question_description);
        ivQuestionAddVideo = findViewById(R.id.iv_question_add_video);
        ivQuestionAddPicture = findViewById(R.id.iv_question_add_picture);
        rlInputAsk = findViewById(R.id.rl_reward_coin_bar);

        try {
            if (getIntent() != null) {
                Bundle bundle = getIntent().getBundleExtra("data");
                mNote = (Note) bundle.getSerializable("note");
                flag = getIntent().getIntExtra("flag",0);
                if (flag == 1){
                    mAnswer = (Answer) bundle.getSerializable("answer");
                    dealWithEditorContent(mAnswer.getAnswerContent(),etInputQuestionDescription);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        insertDialog = new ProgressDialog(this);
        insertDialog.setMessage("正在插入图片...");
        insertDialog.setCanceledOnTouchOutside(false);

        bgHomeStatusBar.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        ivBack.setOnClickListener(v -> finish());

        pageTitle.post(() -> pageTitle.setText("写回答"));

        questionTitle.post(() -> questionTitle.setText(mNote.getTitle()));

        questionDescription.post(() -> dealWithTextContent(mNote.getContent(),questionDescription));

        ivQuestionAddPicture.setOnClickListener(v -> {
            closeSoftKeyInput();//关闭软键盘
            checkPremission(1);
        });

        ivQuestionAddVideo.setOnClickListener(v -> {
            closeSoftKeyInput();//关闭软键盘
            checkPremission(2);
        });

        rlInputAsk.setOnClickListener(v -> {
            saveNoteData(false);
            Log.d("数据", "点击了按钮");
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
     * 检查权限
     */
    public void checkPremission(int type) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (type == 1) {
                            addPicture();
                        } else if (type == 2) {
                            addVideo();
                        }

                    } else {
                        UIUtil.showToast(QAInputAnswerActivity.this, "你拒绝了权限，无法加载图片");
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

    /**
     * 调用视频
     */
    public void addVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
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
                        UIUtil.showToast(QAInputAnswerActivity.this, "图片插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (insertDialog != null && insertDialog.isShowing()) {
                            insertDialog.dismiss();
                        }
                        UIUtil.showToast(QAInputAnswerActivity.this, "图片插入失败");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String imagePath) {
                        //在applition接口里实现
                        etInputQuestionDescription.insertImage(imagePath);
                        Log.d("数据", "开始生成图片view");
                    }
                });
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
                            Bitmap bitmap = ThumbnailUtils.extractThumbnail(bitmap1, NiceUtil.getScreenWidth(QAInputAnswerActivity.this), (int) (NiceUtil.getScreenWidth(QAInputAnswerActivity.this) * 9f / 16f), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                            emitter.onNext("thumss" + imagePath);
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
                        UIUtil.showToast(QAInputAnswerActivity.this, "视频插入成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (insertDialog != null && insertDialog.isShowing()) {
                            insertDialog.dismiss();
                        }
                        UIUtil.showToast(QAInputAnswerActivity.this, "视频插入失败");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String imagePath) {
                        //在applition接口里实现
                        if (imagePath.contains("thumss")) {
                            imagePath = StringUtils.getVideoString(imagePath);
                            etInputQuestionDescription.imagePath = imagePath;
                            Log.d("视频的截图", imagePath);
                        } else {
                            etInputQuestionDescription.insertVideo(imagePath);
                            Log.d("视频的播放", imagePath);
                        }

                    }
                });

    }


    private void dealWithTextContent(String description, RichTextView view) {
        view.clearAllLayout();
        showTextDataSync(description,view);
    }

    private void dealWithEditorContent(String description, RichTextEditor view) {
        view.clearAllLayout();
        showEditorDataSync(description,view);
    }
    /**
     * 异步方式显示正文内容数据
     */
    private void showTextDataSync(final String html,View view) {
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
                    }

                    @Override
                    public void onError(Throwable e) {

                        com.haiming.paper.Utils.UIUtil.showToast(QAInputAnswerActivity.this, "解析错误");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String text) {
                        try {
                            if (view != null) {
                                if (text.contains("img=")) {
                                    String imagePath = StringUtils.getImgString(text);
                                    //  et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                    questionDescription.addImageViewAtIndex(questionDescription.getLastIndex(), imagePath);
                                } else if (text.contains("video=")) {
                                    String[] viedoPath = StringUtils.getVideoImage(text);
                                    if (viedoPath[1] != null) {
                                        questionDescription.imagePath = viedoPath[1];
                                        Log.d("数据", "图片=" + viedoPath[1]);
                                    }
                                    if (viedoPath[0] != null) {
                                        questionDescription.addVideoViewAtIndex(questionDescription.getLastIndex(), viedoPath[0]);
                                        Log.d("数据", "视频=" + viedoPath[0]);
                                    }

                                } else {
                                    questionDescription.addTextViewAtIndex(questionDescription.getLastIndex(), text);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void showEditorDataSync(final String html,View view) {
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
                    }

                    @Override
                    public void onError(Throwable e) {

                        com.haiming.paper.Utils.UIUtil.showToast(QAInputAnswerActivity.this, "解析错误");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String text) {
                        try {
                            if (view != null) {
                                if (text.contains("img=")) {
                                    String imagePath = StringUtils.getImgString(text);
                                    //  et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                    etInputQuestionDescription.addImageViewAtIndex(etInputQuestionDescription.getLastIndex(), imagePath);
                                } else if (text.contains("video=")) {
                                    String[] viedoPath = StringUtils.getVideoImage(text);
                                    if (viedoPath[1] != null) {
                                        etInputQuestionDescription.imagePath = viedoPath[1];
                                        Log.d("数据", "图片=" + viedoPath[1]);
                                    }
                                    if (viedoPath[0] != null) {
                                        etInputQuestionDescription.addVideoViewAtIndex(etInputQuestionDescription.getLastIndex(), viedoPath[0]);
                                        Log.d("数据", "视频=" + viedoPath[0]);
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
            for (String string : textList){
                emitter.onNext(string);
            }
            emitter.onComplete();
        } catch (Exception e) {
            e.printStackTrace();
            emitter.onError(e);
        }
    }

    /**
     * 保存数据,=0销毁当前界面，=1不销毁界面，为了防止在后台时保存笔记并销毁，应该只保存笔记
     * 拿到全部数据，并提交给数据库
     */
    private void saveNoteData(boolean isBackground) {
        if (!checkContent()) {
            ToastUtils.show("回答内容不能为空");
            return;
        }
        int userId = UserData.getUserId(this);
        int noteId = mNote.getId();

        String answerContent = getEditData();

        List<String> content =  StringUtils.spliteString(answerContent);
        if ((content.size() == 0 || (content.size() == 1 && content.get(0).trim().isEmpty()))){
            ToastUtils.show("请输入内容");
            return;
        }

        try {
            Answer answer = new Answer();
            answer.setAnswerContent(answerContent);
            answer.setAnswerUserId(userId);
            answer.setAnswerNoteId(noteId);
            if (flag == 0) {
                mAnswerDao.insertAnswer(this,answer);
                ToastUtils.show("已回答");
                finish();
            } else if (flag == 1) {
                if (!mAnswer.getAnswerContent().equals(answerContent)){
                    answer.setAnswerId(mAnswer.getAnswerId());
                    mAnswerDao.updateAnswer(answer);
                    ToastUtils.show("已更新");
                    finish();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkContent() {
        if (etInputQuestionDescription == null) {
            return false;
        }
        return true;
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


    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
}
