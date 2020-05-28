package com.haiming.paper.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private boolean isLoadingResume = false;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayout(),container,false);
        return mView;
    }

    protected abstract int getLayout();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBroadcastReceiver();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    protected void initView(){
        loadData();
    }

    protected abstract void loadData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyBroadcastReceiver();
    }

    /**
     * 初始化Receiver
     */
    protected void initBroadcastReceiver() {
    }

    /**
     * 销毁Receiver
     */
    protected void destroyBroadcastReceiver() {
    }

    @Nullable
    @Override
    public View getView() {
        return mView;
    }
}
