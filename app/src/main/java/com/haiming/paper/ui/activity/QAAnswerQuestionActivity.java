package com.haiming.paper.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingmouren.layoutmanagergroup.viewpager.ViewPagerLayoutManager;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.AnswerQuestionAdapter;
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.AnswerDao;
import com.haiming.paper.db.Constante;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.thread.ThreadManager;
import com.youcheyihou.videolib.NiceVideoPlayerManager;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.disposables.Disposable;

/**
 * 回答内容详情列表
 */
public class QAAnswerQuestionActivity extends BaseActivity implements AnswerQuestionAdapter.CallBack {

    private View mView;
    private TextView pageTitle;
    private ImageView ivBack;
    private RecyclerView answerItemRv;
    private ImageView nextAnswer;
    private ViewPagerLayoutManager mViewPagerLayoutManager;
    private AnswerQuestionAdapter mAdapter;

    //数据
    private List<Answer> mAnswerList = new ArrayList<>();
    private List<User> mUsers = new ArrayList<>();
    private List<Integer> mIds = new ArrayList<>();
    private AnswerDao mAnswerDao;
    private UserDao mUserDao;
    private Note note;
    private int noteId;

    private ProgressDialog loadingDialog;
    private Disposable mDisposable;

    private String myTitle;
    private String myContent;
    private String myGroupName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_question_activity);
        initView();
    }


    private int type = 0;

    private void initView() {
        mView = findViewById(R.id.state_view);
        pageTitle = findViewById(R.id.page_title);
        ivBack = findViewById(R.id.iv_back);
        answerItemRv = findViewById(R.id.answer_item_rv);
        //   nextAnswer = findViewById(R.id.next_answer);

        mView.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        ivBack.setOnClickListener(v -> {
            finish();
        });

        loadIntent();
        mViewPagerLayoutManager = new ViewPagerLayoutManager(QAAnswerQuestionActivity.this, OrientationHelper.VERTICAL);
        // answerItemRv.setLayoutManager(new LinearLayoutManager(QAAnswerQuestionActivity.this));
        answerItemRv.setLayoutManager(mViewPagerLayoutManager);
        mAdapter = new AnswerQuestionAdapter(this, mAnswerList, mUsers, note, answerItemRv, Constante.isManage, type);
        mAdapter.setCallBack(this);
        answerItemRv.setAdapter(mAdapter);
    }

    private void loadIntent() {
        try {
            Intent intent = getIntent();
            type = intent.getIntExtra("type", 0);
            if (type == 0) {
                Log.d("类型", "0");
                Bundle bundle = intent.getBundleExtra("data");
                note = (Note) bundle.getSerializable("note");
                if (note != null) {
                    myTitle = note.getTitle();
                    pageTitle.setText(myTitle);
                    noteId = note.getId();
                    loadData();
                } else {
                    return;
                }
            } else if (type == 1 || type == 2) {
                Log.d("类型", "1");
                Bundle bundle = intent.getBundleExtra("data");
                note = (Note) bundle.getSerializable("note");
                Answer answer = (Answer) bundle.getSerializable("answer");
                if (answer != null) {
                    mAnswerList.clear();
                    mAnswerList.add(answer);
                }
                User user = (User) bundle.getSerializable("user");
                if (user != null) {
                    mUsers.clear();
                    mUsers.add(user);
                }
                if (note != null) {
                    myTitle = note.getTitle();
                    pageTitle.setText(myTitle);
                    noteId = note.getId();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadData() {
        ThreadManager.getLongPool().execute(() -> {
            mAnswerDao = new AnswerDao(QAAnswerQuestionActivity.this);
            mUserDao = new UserDao(QAAnswerQuestionActivity.this);
            List<Answer> answers = new ArrayList<>();
            answers = mAnswerDao.queryNoteById(note.getId());
            Log.e("数据", "查询到的Note的大小=" + answers.size() + " 传来的note大小=" + note.getAnswerSize());
            mAnswerList.addAll(answers);
            for (Answer answer : mAnswerList) {
                mUsers.add(mUserDao.queryUserById(answer.getAnswerUserId()));
            }

        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        NiceVideoPlayerManager.instance().releaseNiceVideoPlayer();
    }

    private Intent mIntent;

    @Override
    public void itemClick(Note note) {
        mIntent = new Intent(this, QADetailsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        mIntent.putExtra("data", bundle);
        mIntent.putExtra("type", 1);
        startActivity(mIntent);
        finish();
    }
}
