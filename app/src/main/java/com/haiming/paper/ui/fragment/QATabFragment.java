package com.haiming.paper.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.QuestionAndAnswerNavigatorAdapter;
import com.haiming.paper.adapter.QuestionAndAnswerPagerAdapter;
import com.haiming.paper.bean.Group;
import com.haiming.paper.db.Constante;
import com.haiming.paper.db.GroupDao;
import com.haiming.paper.ui.activity.QAAskQuestionActivity;
import com.haiming.paper.ui.view.InsertAskEvent;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

/**
 * 问答Fragment
 */
public class QATabFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private String[] mTabs;
    private List<String> mStringList = new ArrayList<>();
    private List<Integer> mIdList = new ArrayList<>();
    private int i = 0;
    private MagicIndicator questionMiTab;
    private ViewPager viewPager;
    private View view;
    private List<Fragment> mList;
    private Context mContext;
    private TextView addAsk;
    private Intent mIntent;

    public QATabFragment(Context context) {
        mContext = context;
    }

    @Override
    protected int getLayout() {
        return R.layout.qa_tab_layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InsertAskEvent event){

    }

    @Override
    protected void initView() {
        super.initView();
        View view = getView();
        if (view == null) {
            return;
        }
        questionMiTab = view.findViewById(R.id.question_mi_tab);
        viewPager = view.findViewById(R.id.view_pager);
        addAsk = view.findViewById(R.id.add_ask_iv);

        view = view.findViewById(R.id.state_view);
        mList = new ArrayList<>();

        if (Constante.isManage){
            addAsk.setVisibility(View.GONE);
        }

        View finalView = view;
        view.post(() -> finalView.getLayoutParams().height = UIUtil.getStatusBarHeight(getContext()));
        addAsk.setOnClickListener(v -> {
            mIntent = new Intent(getContext(), QAAskQuestionActivity.class);
            mIntent.putExtra("flag", 0);
            getContext().startActivity(mIntent);
        });

        for (Integer id : mIdList){
            BaseQATavFragment fragment = new BaseQATavFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            fragment.setArguments(bundle);
            mList.add(fragment);
        }
        QuestionAndAnswerPagerAdapter pagerAdapter = new QuestionAndAnswerPagerAdapter(mList, getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        initTab();

    }

    @Override
    protected void loadData() {
        GroupDao groupDao = new GroupDao(mContext);
        List<Group> groups = groupDao.queryGroupAll();
        for (Group group : groups) {
            mStringList.add(group.getName());
            mIdList.add(group.getId());
        }

    }


    private void initTab() {
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        QuestionAndAnswerNavigatorAdapter navigatorAdapter = new QuestionAndAnswerNavigatorAdapter(getContext(), viewPager, mStringList);
        commonNavigator.setAdapter(navigatorAdapter);
        commonNavigator.setAdjustMode(true);
        questionMiTab.setNavigator(commonNavigator);
        ViewPagerHelper.bind(questionMiTab, viewPager);
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
