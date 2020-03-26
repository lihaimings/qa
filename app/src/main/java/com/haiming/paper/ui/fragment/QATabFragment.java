package com.haiming.paper.ui.fragment;

import android.content.Context;
import android.view.View;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.QuestionAndAnswerNavigatorAdapter;
import com.haiming.paper.adapter.QuestionAndAnswerPagerAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class QATabFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private String[] mTabs = {"汽车", "文学"};
    private MagicIndicator questionMiTab;
    private ViewPager viewPager;
    private View view;
    private List<Fragment> mList;
    private Context mContext;

    public QATabFragment(Context context){
        this.mContext =context;
    }
    @Override
    protected int getLayout() {
        return R.layout.qa_tab_layout;
    }

    @Override
    protected void initView() {
        View view = getView();
        if (view == null) {
            return;
        }
        questionMiTab = view.findViewById(R.id.question_mi_tab);
        viewPager = view.findViewById(R.id.view_pager);
        view = view.findViewById(R.id.state_view);
        mList = new ArrayList<>();
        View finalView = view;
        view.post(new Runnable() {
            @Override
            public void run() {
                finalView.getLayoutParams().height = UIUtil.getStatusBarHeight(getContext());
            }
        });
        HomeFragment homeFragment = new HomeFragment();
        CarFragment carFragment = new CarFragment(mContext);
        mList.add(homeFragment);
        mList.add(carFragment);
        QuestionAndAnswerPagerAdapter pagerAdapter=new QuestionAndAnswerPagerAdapter(mList,getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        initTab();


    }

    @Override
    protected void loadData() {

    }

    private void initTab() {
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        QuestionAndAnswerNavigatorAdapter navigatorAdapter = new QuestionAndAnswerNavigatorAdapter(getContext(),mTabs,viewPager);
        commonNavigator.setAdapter(navigatorAdapter);
        commonNavigator.setAdjustMode(true);
        questionMiTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(questionMiTab,viewPager);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
