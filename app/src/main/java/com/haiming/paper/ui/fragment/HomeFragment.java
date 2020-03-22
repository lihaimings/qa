package com.haiming.paper.ui.fragment;

import android.util.Log;
import android.view.View;



import com.haiming.paper.R;
import com.haiming.paper.adapter.HomeRecycerViewAdapter;
import com.haiming.paper.ui.view.recyclerview.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class HomeFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener,LoadMoreRecyclerView.OnLoadMoreListener {


    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private HomeRecycerViewAdapter mAdapter;

    private List<String> data = new ArrayList<>();


    @Override
    protected int getLayout() {
        return R.layout.fragment_home_list;
    }

    @Override
    public void initView() {
        for(int i=1;i<25;i++){
           data.add(""+i);
        }
        View view =getView();
        if(view == null){
            Log.e("加载时","view是空的");
            return;
        }else {
            Log.e("加载时","view不是空的");
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        mAdapter = new HomeRecycerViewAdapter(getContext(),data);
        recyclerView.post(() -> {
            recyclerView.setOnLoadMoreListener(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(mAdapter);
        });


    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
