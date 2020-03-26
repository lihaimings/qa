package com.haiming.paper.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class QuestionAndAnswerPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> mFragmentList = new ArrayList<>();

    public QuestionAndAnswerPagerAdapter(List<Fragment> list, @NonNull FragmentManager fm) {
        super(fm);
        this.mFragmentList = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
