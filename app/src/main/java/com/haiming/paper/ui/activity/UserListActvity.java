package com.haiming.paper.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.LookUserListAdapter;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.ui.view.recyclerview.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class UserListActvity extends BaseActivity {

    private View viewTop;
    private TextView title;
    private ImageView back;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private UserDao mUserDao = new UserDao(this);
    private List<User> mUserList = new ArrayList<>();
    private LookUserListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_my_question);
        loadData();
        initView();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        viewTop = findViewById(R.id.view_top);
        title = findViewById(R.id.page_title);
        back = findViewById(R.id.iv_back);

        title.setText("全部用户");
        viewTop.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        back.setOnClickListener(v -> finish());

        if (mUserList.size() > 0) {
            initRecyclerView();
        }


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                if (mAdapter != null) {
                    mAdapter.updateList(mUserList);
                } else {
                    initRecyclerView();
                    mAdapter.updateList(mUserList);
                }

                refreshLayout.setRefreshing(false);
            }
        });

    }

    public void initRecyclerView() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                mAdapter = new LookUserListAdapter(UserListActvity.this, mUserList);
                recyclerView.setLayoutManager(new LinearLayoutManager(UserListActvity.this));
                recyclerView.setAdapter(mAdapter);

                int gapH = getResources().getDimensionPixelOffset(R.dimen.x8);
                recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(UserListActvity.this)
                        .thickness(gapH)
                        .color(UserListActvity.this, R.color.transparent)
                        .create());

            }
        });
    }

    private void loadData() {
        List<User> users = new ArrayList<>();

        users = mUserDao.queryUserAll();
        if (users!= null){
            mUserList = users;
        }

        for (User user:users){
            Log.d("查询用户",user.toString());
        }
//        ThreadManager.getLongPool().execute(new Runnable() {
//            @Override
//            public void run() {
//            }
//        });
    }
}
