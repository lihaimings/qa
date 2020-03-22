package com.haiming.paper.ui;

import android.os.Bundle;
import android.view.View;

import com.haiming.paper.R;
import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.BaseFragmentAdapter;
import com.haiming.paper.ui.activity.BaseActivity;
import com.haiming.paper.ui.fragment.HomeFragment;
import com.haiming.paper.ui.view.SlidingTabLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity {

    private View stateView;
    private ViewPager viewpager;
    private SlidingTabLayout tabLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        stateView = findViewById(R.id.state_view);
        viewpager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

        stateView.post(() -> {
            stateView.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        });

        HomeFragment homeFragment = new HomeFragment();

        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager());

        adapter.addFragment(homeFragment,"主页");

      //  viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(adapter);
        tabLayout.setViewPager(viewpager);

    }
}
