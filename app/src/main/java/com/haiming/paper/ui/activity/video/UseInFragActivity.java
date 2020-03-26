package com.haiming.paper.ui.activity.video;

import android.os.Bundle;

import com.haiming.paper.R;
import com.youcheyihou.videolib.NiceVideoPlayerManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UseInFragActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_in_frag);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container,new DemoFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) return;
        super.onBackPressed();
    }
}
