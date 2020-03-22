package com.haiming.paper.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.adapter.TextWatcherAdapter;
import com.hjq.toast.ToastUtils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class QAAskQuestionActivity extends BaseActivity {

    @BindView(R.id.bg_home_status_bar)
    View bgHomeStatusBar;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.page_back)
    LinearLayout pageBack;
    @BindView(R.id.page_title)
    TextView pageTitle;
    @BindView(R.id.page_title_arrow)
    ImageView pageTitleArrow;
    @BindView(R.id.ll_page_title)
    LinearLayout llPageTitle;
    @BindView(R.id.page_extension_ll)
    LinearLayout pageExtensionLl;
    @BindView(R.id.page_title_rl)
    RelativeLayout pageTitleRl;
    @BindView(R.id.ll_top_bar)
    LinearLayout llTopBar;
    @BindView(R.id.et_input_question)
    EditText etInputQuestion;
    @BindView(R.id.et_input_question_description)
    EditText etInputQuestionDescription;
    @BindView(R.id.iv_question_add_video)
    ImageView ivQuestionAddVideo;
    @BindView(R.id.iv_question_add_picture)
    ImageView ivQuestionAddPicture;
    @BindView(R.id.tv_how_ask_question)
    TextView tvHowAskQuestion;
    @BindView(R.id.rl_reward_coin_bar)
    LinearLayout rlRewardCoinBar;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        mUnbinder = ButterKnife.bind(this);
        //initView();
    }

    private void initView() {
        pageTitleRl.setPadding(0,UIUtil.getStatusBarHeight(this),0,0);
        ivBack.setOnClickListener(v->finish());
        pageTitle.setText("增加问答");
        etInputQuestion.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                int length = s.toString().trim().length();
                if (length >= 50) {
                    ToastUtils.show("字数达到最大值啦！");
                }
            }
        });
        etInputQuestion.setOnFocusChangeListener(UIUtil.onFocusAutoClearHintListener());
        etInputQuestionDescription.setOnFocusChangeListener(UIUtil.onFocusAutoClearHintListener());
        etInputQuestionDescription.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                int length = s.toString().trim().length();
                if (length >= 500) {
                    ToastUtils.show("字数达到最大值啦！");
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
