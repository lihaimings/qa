package com.haiming.paper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.haiming.paper.Utils.PopupWindowUtil;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.User;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LookUserListAdapter extends RecyclerBaseAdapter<User, LookUserListAdapter.ViewHolder> {


    public LookUserListAdapter(Context context, List<User> dataList) {
        super(context, dataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getContext(),getDataList().get(position),this,position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView answerUserPic;
        TextView userName;
        TextView userNumber;
        TextView userPassward;
        TextView userSignle;
        TextView userSex;
        ImageView ivDislikeBtn;
        LinearLayout llQuestionHaveAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerUserPic = itemView.findViewById(R.id.answer_user_pic);
            userName = itemView.findViewById(R.id.user_name);
            userNumber = itemView.findViewById(R.id.user_number);
            userPassward = itemView.findViewById(R.id.user_passward);
            userSignle = itemView.findViewById(R.id.user_signle);
            userSex = itemView.findViewById(R.id.user_sex);
            ivDislikeBtn = itemView.findViewById(R.id.iv_dislike_btn);

        }
        public void bind(Context context,User user,LookUserListAdapter adapter,int pos){

            String path = user.getImagePath();
            if (path != null) {
                byte[] bytes = UIUtil.string2byte(path);
                Glide.with(getContext()).load(bytes).circleCrop().into(answerUserPic);
            }
            userName.setText("用户名:"+user.getName());
            userNumber.setText("账号:"+user.getNumber());
            userPassward.setText("密码:"+user.getPassword());
            userSignle.setText("签名:"+user.getSignature());
            userSex.setText("性别:"+user.getSex());
            ivDislikeBtn.setOnClickListener(v->{
                PopupWindowUtil.showQAndADeleteUserWindow(context, ivDislikeBtn, user.getId(), adapter, pos);
            });
        }
    }
}
