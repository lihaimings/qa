package com.haiming.paper.ui.fragment;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.Manager;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.Constante;
import com.haiming.paper.db.ManagerDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.db.UserData;
import com.haiming.paper.ui.activity.LookMyAnswerActivity;
import com.haiming.paper.ui.activity.LookMyQuestionActivity;
import com.haiming.paper.ui.activity.UserListActvity;
import com.haiming.paper.ui.activity.video.ModifyMyInformation;

/**
 * 我的Fragment
 */
public class MyAccountFragment extends BaseFragment {

    private View bgHomeStatusBar;
    private ImageView userImage;
    private TextView answerUserName;
    private TextView userMark;
    private RelativeLayout userRl;
    private Button modifyBasicInformation;
    private Button lookMyQuestions;
    private Button lookMyAnswers;
    private Button lookAnswerVideo;

    private UserDao mUserDao;
    private ManagerDao mManagerDao;
    private User mUser = new User();
    private Manager mManager = new Manager();
    private Intent mIntent;

    private boolean isManager = Constante.isManage;

    @Override
    protected int getLayout() {
        return R.layout.my_account_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        View view = getView();
        if (view == null) {
            return;
        }
        bgHomeStatusBar = view.findViewById(R.id.bg_home_status_bar);
        userImage = view.findViewById(R.id.user_image);
        answerUserName = view.findViewById(R.id.answer_user_name);
        userMark = view.findViewById(R.id.user_mark);
        modifyBasicInformation = view.findViewById(R.id.modify_basic_information);
        lookMyQuestions = view.findViewById(R.id.look_my_questions);
        lookMyAnswers = view.findViewById(R.id.look_my_answers);
        lookAnswerVideo = view.findViewById(R.id.look_answer_video);


        bgHomeStatusBar.getLayoutParams().height = UIUtil.getStatusBarHeight(getContext());
        if (Constante.isManage){
            lookMyQuestions.setVisibility(View.GONE);
            lookAnswerVideo.setVisibility(View.GONE);
            lookMyAnswers.setVisibility(View.GONE);
            lookMyQuestions.setText("查询用户");
            lookMyAnswers.setText("查看全部问题");
            if (mManager != null){
                userImage.post(() -> {
                    byte[] bytes = UIUtil.string2byte(mManager.getImagePath());
                    Glide.with(getContext()).load(bytes).circleCrop().into(userImage);
                });
                answerUserName.post(() -> answerUserName.setText(mManager.getName()));
                userMark.setVisibility(View.GONE);
            }

        }else {
            if (mUser != null) {
                userImage.post(() -> {
                    byte[] bytes = UIUtil.string2byte(mUser.getImagePath());
                    Glide.with(getContext()).load(bytes).circleCrop().into(userImage);
                });
                answerUserName.post(() -> answerUserName.setText(mUser.getName()));
                userMark.post(() -> userMark.setText(mUser.getSignature()));
            }

            lookAnswerVideo.setVisibility(View.GONE);
        }

        modifyBasicInformation.setOnClickListener(v -> {
            if (!Constante.isManage){
                mIntent = new Intent(getActivity(), ModifyMyInformation.class);
                mIntent.putExtra("userId",UserData.getUserId(getActivity()));
                getActivity().startActivity(mIntent);
            }else {
                mIntent= new Intent(getActivity(), UserListActvity.class);
                getActivity().startActivity(mIntent);
            }

        });

        lookMyQuestions.setOnClickListener(v -> {
            mIntent = new Intent(getActivity(), LookMyQuestionActivity.class);
            getActivity().startActivity(mIntent);
        });

        lookMyAnswers.setOnClickListener(v -> {
            mIntent = new Intent(getActivity(), LookMyAnswerActivity.class);
            getActivity().startActivity(mIntent);
        });

    }

    @Override
    protected void loadData() {
        mUserDao = new UserDao(getContext());
        mManagerDao = new ManagerDao(getContext());
        User user = new User();
//        user = mUserDao.queryUserById();
        mUser = user;
        int id = UserData.getUserId(getActivity());
        Log.d("主页的用户", "id= " + id);
        mUser = mUserDao.queryUserById(id);
        mManager = mManagerDao.queryManagerById(id);
//        if (){
//            isManager = false;
//            Log.d("管理员","false");
//        }
//        if (mManager != null){
//            isManager = true;
//            Log.d("管理员","true");
//        }
    }
}
