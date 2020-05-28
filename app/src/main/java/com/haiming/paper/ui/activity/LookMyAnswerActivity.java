package com.haiming.paper.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.LookMyAnswerAdapter;
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.AnswerDao;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.db.UserData;
import com.haiming.paper.thread.ThreadManager;
import com.haiming.paper.ui.view.recyclerview.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LookMyAnswerActivity extends BaseActivity {

    private View viewTop;
    private TextView title;
    private ImageView back;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    public List<Answer> dataList = new ArrayList<>();
    public List<Note> mNoteList = new ArrayList<>();
    public List<User> mUserList = new ArrayList<>();
    public User mUser = new User();
    public AnswerDao mAnswerDao  = new AnswerDao(this);
    private UserDao mUserDao = new UserDao(this);
    private NoteDao mNoteDao = new NoteDao(this);
    private LookMyAnswerAdapter mAdapter;
    public boolean isRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_my_question);
        loadData();
        initView();
    }


    private void loadData() {
        ThreadManager.getLongPool().execute(() -> {
        mUser = mUserDao.queryUserById(UserData.getUserId(LookMyAnswerActivity.this));
        if (mAnswerDao != null) {
            List<Answer> answers = new ArrayList<>();
            answers = mAnswerDao.queryUserById(UserData.getUserId(LookMyAnswerActivity.this));
            if (answers != null) {
                dataList.addAll(answers);
            }
//
        }
        if (dataList != null) {
            for (Answer answer : dataList) {
                mNoteList.add(mNoteDao.queryNoteId(answer.getAnswerNoteId()));
                mUserList.add(mUserDao.queryUserById(answer.getAnswerUserId()));
            }
        }
        });
    }

    private void initView() {

        recyclerView = findViewById(R.id.recycler_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        viewTop = findViewById(R.id.view_top);
        title = findViewById(R.id.page_title);
        back = findViewById(R.id.iv_back);

        title.setText("我的回答");
        viewTop.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        back.setOnClickListener(v -> finish());
        if (dataList.size() > 0) {
            initRecycleView();
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("更新","开始");
                loadData();
                if (mAdapter != null) {
                    mAdapter.updateList(dataList);
                    Log.d("更新","data大小="+dataList.size());
                } else {
                    initRecycleView();
                    Log.d("更新","初始化设配器");
                    mAdapter.updateList(dataList);
                }
                refreshLayout.setRefreshing(false);
            }
        });

    }

    public void initRecycleView() {
        recyclerView.post(() -> {
            mAdapter = new LookMyAnswerAdapter(LookMyAnswerActivity.this, dataList, mNoteList, mUserList);
            recyclerView.setLayoutManager(new LinearLayoutManager(LookMyAnswerActivity.this));
            recyclerView.setAdapter(mAdapter);

            int gapH = getResources().getDimensionPixelOffset(R.dimen.x8);
            recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(LookMyAnswerActivity.this)
                    .thickness(gapH)
                    .color(LookMyAnswerActivity.this, R.color.transparent)
                    .create());

        });
    }

}
