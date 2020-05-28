package com.haiming.paper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.StringUtils;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.QADetailsListAdapter;
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.AnswerDao;
import com.haiming.paper.db.Constante;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.thread.ThreadManager;
import com.haiming.paper.ui.view.recyclerview.RecyclerViewItemDecoration;
import com.hjq.toast.ToastUtils;
import com.sendtion.xrichtext.RichTextView;
import com.youcheyihou.videolib.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 问答详情展示列表
 */
public class QADetailsListActivity  extends BaseActivity implements QADetailsListAdapter.CallBack{

    private View bgHomeStatusBar;
    private TextView pageTitle;
    private ImageView ivBack;
    private TextView tvProblemTitle;
    private RichTextView tvProblemDescription;
    private TextView tvQuestionAnswerNum;
    private RecyclerView rvAnswerQuestionList;
    private FrameLayout haveAnswer;
    private ImageView ivNoAnswerPic;
    private FrameLayout noAnswer;
    private Button btnInvitationAnswer;
    private Button btnMeAnswer;

    private QADetailsListAdapter mAdapter;

    private int flag;
    private Note note = new Note();
    //数据
    private List<Answer> mAnswerList = new ArrayList<>();
    private List<User> mUsers = new ArrayList<>();
    private Intent mIntent;

    private AnswerDao mAnswerDao;
    private UserDao mUserDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_details_list);
        initView();

    }


    private void initView() {
        bgHomeStatusBar = findViewById(R.id.bg_home_status_bar);
        pageTitle = findViewById(R.id.page_title);
        ivBack = findViewById(R.id.iv_back);
        tvProblemTitle = findViewById(R.id.tv_problem_title);
        tvProblemDescription = findViewById(R.id.tv_problem_description);
        tvQuestionAnswerNum = findViewById(R.id.tv_question_answer_num);
        rvAnswerQuestionList = findViewById(R.id.rv_answer_question_list);
        haveAnswer = findViewById(R.id.have_answer);
        ivNoAnswerPic = findViewById(R.id.iv_no_answer_pic);
        noAnswer = findViewById(R.id.no_answer);
        btnInvitationAnswer = findViewById(R.id.btn_invitation_answer);
        btnMeAnswer = findViewById(R.id.btn_me_answer);
        ivBack.setOnClickListener(v -> finish());
        bgHomeStatusBar.getLayoutParams().height = UIUtil.getStatusBarHeight(QADetailsListActivity.this);

        if (Constante.isManage){
            btnMeAnswer.setText("删除提问");
        }
        try {
            if (getIntent() != null) {
                Bundle bundle = getIntent().getBundleExtra("data");
                note = (Note) bundle.getSerializable("note");
                flag = getIntent().getIntExtra("type", 0);
                if (flag == 1) {
                    loadData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (flag == 0) {
            haveAnswer.setVisibility(View.GONE);
            noAnswer.setVisibility(View.VISIBLE);
            tvQuestionAnswerNum.setText("暂无回答");
            pageTitle.setText(note.getTitle());
            tvProblemTitle.setText(note.getTitle());
            tvProblemDescription.post(() -> dealWithContent());
        } else if (flag == 1) {
            pageTitle.setText(note.getTitle());
            tvProblemTitle.setText(note.getTitle());
            tvProblemDescription.post(() -> dealWithContent());
            rvAnswerQuestionList.post(() -> {
                rvAnswerQuestionList.setLayoutManager(new LinearLayoutManager(QADetailsListActivity.this));
                int gapH = getResources().getDimensionPixelOffset(R.dimen.x8);
                rvAnswerQuestionList.addItemDecoration(new RecyclerViewItemDecoration.Builder(QADetailsListActivity.this)
                        .thickness(gapH)
                        .color(QADetailsListActivity.this, R.color.transparent)
                        .create());
                mAdapter = new QADetailsListAdapter(QADetailsListActivity.this, note, mAnswerList, mUsers);
                mAdapter.setCallBack(this);
                rvAnswerQuestionList.setAdapter(mAdapter);
            });
            tvQuestionAnswerNum.post(new Runnable() {
                @Override
                public void run() {
                    tvQuestionAnswerNum.setText(mAnswerList.size()+" 回答");
                }
            });
        }

        btnMeAnswer.post(new Runnable() {
            @Override
            public void run() {
                btnMeAnswer.setOnClickListener(v -> {
                    if (Constante.isManage){
                        NoteDao noteDao = new NoteDao(QADetailsListActivity.this);
                        noteDao.deleteNote(note.getId());
                        ToastUtils.show("删除提问成功");
                        finish();
                    }else {
                        mIntent = new Intent(QADetailsListActivity.this, QAInputAnswerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", note);
                        mIntent.putExtra("data", bundle);
                        mIntent.putExtra("flag", 0);
                        startActivity(mIntent);
                    }

                });

            }
        });

    }

    private void loadData() {
        ThreadManager.getLongPool().execute(() -> {
            mAnswerDao = new AnswerDao(QADetailsListActivity.this);
            mUserDao = new UserDao(QADetailsListActivity.this);
            List<Answer> answers = new ArrayList<>();
            answers = mAnswerDao.queryNoteById(note.getId());
            mAnswerList.addAll(answers);
            for (Answer answer : mAnswerList) {
                mUsers.add(mUserDao.queryUserById(answer.getAnswerUserId()));
            }
        });
    }

    private void dealWithContent() {
        tvProblemDescription.clearAllLayout();
        showDataSync(note.getContent());

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
                        if (tvProblemDescription != null) {
                            //在图片全部插入完毕后，再插入一个EditText，防止最后一张图片后无法插入文字
                            //  etInputQuestionDescription.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        com.haiming.paper.Utils.UIUtil.showToast(QADetailsListActivity.this, "解析错误");
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String text) {
                        try {
                            if (tvProblemDescription != null) {
                                if (text.contains("img=")) {
                                    String imagePath = StringUtils.getImgString(text);
                                    //  et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                    tvProblemDescription.addImageViewAtIndex(tvProblemDescription.getLastIndex(), imagePath);
                                } else if (text.contains("video=")) {
                                    String[] viedoPath = StringUtils.getVideoImage(text);
                                    if (viedoPath[1] != null) {
                                        tvProblemDescription.imagePath = viedoPath[1];
                                        Log.d("数据", "图片=" + viedoPath[1]);
                                    }
                                    if (viedoPath[0] != null) {
                                        tvProblemDescription.addVideoViewAtIndex(tvProblemDescription.getLastIndex(), viedoPath[0]);
                                        Log.d("数据", "视频=" + viedoPath[0]);
                                    }

                                } else {
                                    tvProblemDescription.addTextViewAtIndex(tvProblemDescription.getLastIndex(), text);
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


    @Override
    public void itemClick(int type, Answer answer, Note note,User user) {
        mIntent = new Intent(this, QAAnswerQuestionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        bundle.putSerializable("answer", answer);
        bundle.putSerializable("user", user);
        mIntent.putExtra("data", bundle);
        mIntent.putExtra("type", type);
        startActivity(mIntent);

    }

    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }
}
