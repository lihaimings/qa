package com.haiming.paper.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.LookMyQuestionAdapter;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.db.UserData;
import com.haiming.paper.ui.view.recyclerview.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class LookMyQuestionActivity extends BaseActivity {


    private View viewTop;
    private TextView title;
    private ImageView back;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    public List<Note> dataList = new ArrayList<>();
    public List<User> mUserList = new ArrayList<>();
    public User mUser = new User();
    public NoteDao mNoteDao= new NoteDao(this);;
    private UserDao mUserDao = new UserDao(this);
    private LookMyQuestionAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_my_question);
        loadData();
        initView();
    }

    private void loadData() {
        mUser = mUserDao.queryUserById(UserData.getUserId(LookMyQuestionActivity.this));
        if (mNoteDao != null) {
            List<Note> notes = new ArrayList<>();
            notes = mNoteDao.queryNotesByUserId(UserData.getUserId(LookMyQuestionActivity.this));
            if (notes != null) {
                dataList.clear();
                dataList.addAll(notes);
            }
//
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        viewTop = findViewById(R.id.view_top);
        title = findViewById(R.id.page_title);
        back = findViewById(R.id.iv_back);

        title.setText("我的提问");
        viewTop.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        back.setOnClickListener(v -> finish());
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter = new LookMyQuestionAdapter(LookMyQuestionActivity.this, dataList, mUser);
                recyclerView.setLayoutManager(new LinearLayoutManager(LookMyQuestionActivity.this));
                recyclerView.setAdapter(mAdapter);

                int gapH = getResources().getDimensionPixelOffset(R.dimen.x8);
                recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(LookMyQuestionActivity.this)
                        .thickness(gapH)
                        .color(LookMyQuestionActivity.this, R.color.transparent)
                        .create());

            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
//                if (mAdapter != null) {
                    mAdapter.updateList(dataList);
//                } else {
////                    initRecycleView();
////                    mAdapter.updateList(dataList);
//                }
                refreshLayout.setRefreshing(false);
            }
        });

    }

    public void loadDataEnd() {

    }

    private void initRecycleView() {

    }
}
