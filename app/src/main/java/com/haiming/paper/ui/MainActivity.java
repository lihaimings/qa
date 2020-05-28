package com.haiming.paper.ui;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.BaseFragmentAdapter;
import com.haiming.paper.db.Constante;
import com.haiming.paper.db.ManagerDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.db.UserData;
import com.haiming.paper.ui.activity.BaseActivity;
import com.haiming.paper.ui.fragment.MyAccountFragment;
import com.haiming.paper.ui.fragment.QATabFragment;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private View stateView;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    private BaseFragmentAdapter adapter;

    private String[] titles={"问答","我的"};

    private final int[] tabIconsSelected = {R.raw.home_showup,R.raw.me_showup};
    private final int[] tabIconsNotSelected = {R.raw.home_disappear,R.raw.me_disappear};
    private UserDao mUserDao;
    private ManagerDao mManagerDao;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        Constante.isManage = UserData.getIsManager(this);
    }

    private void initView() {
        stateView = findViewById(R.id.state_view);
        viewpager = findViewById(R.id.main_pager);
        tabLayout = findViewById(R.id.main_tabs);

        stateView.post(() -> {
            stateView.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        });

        QATabFragment homeFragment = new QATabFragment(this);
        MyAccountFragment meFragment = new MyAccountFragment();

        adapter = new BaseFragmentAdapter(this,getSupportFragmentManager(),titles,tabIconsSelected,tabIconsNotSelected);
        adapter.addFragment(homeFragment);
        adapter.addFragment(meFragment);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(adapter.getTabView(i));
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        adapter.onTabSelectedChange(tabLayout,position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
