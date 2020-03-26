package com.haiming.paper.ui.fragment;

import android.util.Log;
import android.view.View;

import com.haiming.paper.R;
import com.haiming.paper.adapter.HomeRecycerViewAdapter;
import com.haiming.paper.bean.Note;
import com.haiming.paper.ui.view.recyclerview.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public  class BaseQATavFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycerViewAdapter mAdapter;
    public List<Note> dataList = new ArrayList<>();


    @Override
    protected int getLayout() {
        return R.layout.fragment_home_list;
    }

    @Override
    protected void initView() {
        View view = getView();
        if (view == null) {
            Log.e("加载时", "view是空的");
            return;
        }
        Log.e("加载时", "view不是空的");
        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        mAdapter = new HomeRecycerViewAdapter(getContext(), dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);

        int gapH = getResources().getDimensionPixelOffset(R.dimen.x8);
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration.Builder(getContext())
                .thickness(gapH)
                .color(getContext(), R.color.transparent)
                .create());

    }

    @Override
    protected void loadData() {

    }


    @Override
    public void onRefresh() {
        loadData();
        mAdapter.updateList(dataList);
    }

    public void setDataList(List<Note> dataList) {
        this.dataList = dataList;
    }

    public List<Note> getDataList() {
        return dataList;
    }
}
