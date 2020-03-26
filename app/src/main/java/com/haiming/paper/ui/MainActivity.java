package com.haiming.paper.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.BaseFragmentAdapter;
import com.haiming.paper.bean.Note;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.ui.activity.BaseActivity;
import com.haiming.paper.ui.fragment.MeFragment;
import com.haiming.paper.ui.fragment.QATabFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private View stateView;
    private ViewPager viewpager;
    private TabLayout tabLayout;
    private BaseFragmentAdapter adapter;

    private String[] titles={"问答","我的"};

//    private final int[] tabIconsSelected = {R.raw.home_showup,R.raw.draw_showup,R.raw.shopping_showup,R.raw.moment_showup,R.raw.me_showup};
//    private final int[] tabIconsNotSelected = {R.raw.home_disappear,R.raw.draw_disappear,R.raw.shopping_disappear,R.raw.moment_disappear,R.raw.me_disappear};
    private final int[] tabIconsSelected = {R.raw.home_showup,R.raw.me_showup};
    private final int[] tabIconsNotSelected = {R.raw.home_disappear,R.raw.me_disappear};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        stateView = findViewById(R.id.state_view);
        viewpager = findViewById(R.id.main_pager);
        tabLayout = findViewById(R.id.main_tabs);

        stateView.post(() -> {
            stateView.getLayoutParams().height = UIUtil.getStatusBarHeight(this);
        });

        QATabFragment homeFragment = new QATabFragment(this);
        MeFragment meFragment = new MeFragment();


        adapter = new BaseFragmentAdapter(this,getSupportFragmentManager(),titles,tabIconsSelected,tabIconsNotSelected);
        adapter.addFragment(homeFragment);
        adapter.addFragment(meFragment);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(adapter.getTabView(i));
        }

        NoteDao noteDao = new NoteDao(this);
        List<Note> notes = new ArrayList<>();
        notes= noteDao.queryNotesAll(2);
       for (Note note:notes){
           Log.d("数据",note.getContent());
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
