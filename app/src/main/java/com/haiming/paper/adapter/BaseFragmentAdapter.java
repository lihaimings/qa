package com.haiming.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.ui.view.MaterialBadgeTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class BaseFragmentAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private  String[] mTabTitleList;
    private int[] tabsSelected;
    private int[] tabsNotSelected;
    private Context mContext;
    private MaterialBadgeTextView personInfoBadgeButton;
    private int previousSelectedPos = 0;

    public BaseFragmentAdapter(Context context, @NonNull FragmentManager fm,String[] titles,
                               int[] tabsSelected,int[] tabsNotSelected) {
        super(fm);
        this.mContext = context;
        this.mTabTitleList = titles;
        this.tabsSelected = tabsSelected;
        this.tabsNotSelected = tabsNotSelected;
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position < 0 || position >= mFragmentList.size()) {
            return null;
        }
        return mTabTitleList[position];
    }

    //配合TabLayout使用实现tab的gif动效
    public View getTabView(int pos) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_tab_item, null);
        view.setBackgroundResource(R.color.bg_white);

        TextView tv = view.findViewById(R.id.main_tab_title);
        tv.setText(mTabTitleList[pos]);
        LottieAnimationView icon = view.findViewById(R.id.main_tab_gif_icon);
        icon.setAnimation(tabsSelected[pos]);
        if (pos == 0) {
            icon.playAnimation();
            tv.setTextColor(UIUtil.getColor(mContext,R.color.black_33));
            tv.getPaint().setFakeBoldText(true);
        } else {
            tv.setTextColor(UIUtil.getColor(mContext,R.color.gray_99));
            tv.getPaint().setFakeBoldText(false);
        }
        if (pos == mTabTitleList.length - 1) {
            personInfoBadgeButton = view.findViewById(R.id.main_tab_badge);
        }
        return view;
    }

//    public void updateBadge(){
//        if(personInfoBadgeButton!=null){
//            BadgeNumberTreeManager.PosInTree posInTree = BadgeUtils.posMap.get(BadgeUtils.BADGE_ROOT);
//            BadgeNumber.BadgeNumberData data = BadgeNumberTreeManager.getInstance().getBadgeNum(posInTree.getDepth(),posInTree.getIndex());
//            BadgeUtils.setBadgeButton(personInfoBadgeButton, data);
//        }
//    }

    public void onTabSelectedChange(TabLayout tabLayout, int selectedPos){
        try{
            LottieAnimationView preIcon = tabLayout.getTabAt(previousSelectedPos).getCustomView().findViewById(R.id.main_tab_gif_icon);
            preIcon.setAnimation(tabsNotSelected[previousSelectedPos]);
            //preIcon.setRepeatCount(1);
            preIcon.playAnimation();
            TextView preTabTitle = tabLayout.getTabAt(previousSelectedPos).getCustomView().findViewById(R.id.main_tab_title);
            preTabTitle.setTextColor(UIUtil.getColor(mContext,R.color.tv_gray_77));
            preTabTitle.getPaint().setFakeBoldText(false);

            LottieAnimationView icon = tabLayout.getTabAt(selectedPos).getCustomView().findViewById(R.id.main_tab_gif_icon);
            icon.setAnimation(tabsSelected[selectedPos]);
            //icon.setRepeatCount(1);
            icon.playAnimation();
            TextView tabTitle = tabLayout.getTabAt(selectedPos).getCustomView().findViewById(R.id.main_tab_title);
            tabTitle.setTextColor(UIUtil.getColor(mContext,R.color.black_33));
            tabTitle.getPaint().setFakeBoldText(true);
            previousSelectedPos = selectedPos;
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
