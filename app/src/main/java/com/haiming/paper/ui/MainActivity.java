package com.haiming.paper.ui;

import android.os.Bundle;

import com.haiming.paper.R;
import com.haiming.paper.db.DaoMaster;

import org.greenrobot.greendao.AbstractDaoMaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list);


    }
}
