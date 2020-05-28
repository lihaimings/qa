package com.haiming.paper.ui.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.haiming.paper.R;
import com.haiming.paper.adapter.HomeRecycerViewAdapter;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.thread.ThreadManager;
import com.haiming.paper.ui.view.recyclerview.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * 问答Tag Fragment
 */
public class BaseQATavFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycerViewAdapter mAdapter;
    public List<Note> dataList = new ArrayList<>();
    public List<User> mUserList = new ArrayList<>();
    private NoteDao mNoteDao;
    private Context mContext = getActivity();
    private UserDao mUserDao;
    public User mUser = new User();
    public boolean isRefresh = false;
    private int id;


    @Override
    protected int getLayout() {
        return R.layout.fragment_home_list;
    }

    @Override
    protected void initView() {
        super.initView();
        View view = getView();
        if (view == null) {
            Log.e("加载时", "view是空的");
            return;
        }
        Log.e("加载时", "view不是空的");
        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh=true;
                loadData();
                if (mAdapter != null){
                    mAdapter.updateList(dataList);
                }else {
                    initRecycleView();
                    mAdapter.updateList(dataList);
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadDataEnd(){
        if (dataList.size()>0 && mUserList.size()>0){
            Log.d("初始化recycle","1");
            initRecycleView();
        }
    }

    public void initRecycleView(){
        recyclerView.post(() -> {
            mAdapter = new HomeRecycerViewAdapter(getContext(), dataList, mUserList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(mAdapter);

            int gapH = getResources().getDimensionPixelOffset(R.dimen.x8);
            recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(getContext())
                    .thickness(gapH)
                    .color(getContext(), R.color.transparent)
                    .create());

        });
    }

    @Override
    protected void loadData() {

        ThreadManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                if (getArguments() != null) {
                    id = getArguments().getInt("id", 1);
                }

                mNoteDao = new NoteDao(getContext());
                mUserDao = new UserDao(getContext());

                if (mNoteDao != null) {
                    List<Note> notes = new ArrayList<>();
                    notes = mNoteDao.queryNotesAll(id);
                    if (notes != null) {
                        dataList.addAll(notes);
                        Log.d("数据", "大小=" + dataList.size() + "");
                    }
                    if (dataList != null || dataList.size() > 0) {
                        for (Note note : dataList) {
                            mUserList.add(mUserDao.queryUserById(note.getUserId()));
                        }
                    }
                    if (!isRefresh){
                        loadDataEnd();
                    }

                } else {
                    Log.d("数据", "笔记未实例化");
                }

            }
        });
    }


    public void setDataList(List<Note> dataList) {
        this.dataList = dataList;
    }

    public List<Note> getDataList() {
        return dataList;
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
