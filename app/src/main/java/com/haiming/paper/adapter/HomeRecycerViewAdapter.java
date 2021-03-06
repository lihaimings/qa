package com.haiming.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.haiming.paper.Utils.PopupWindowUtil;
import com.haiming.paper.Utils.StringUtils;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.Constante;
import com.haiming.paper.ui.activity.QAAnswerQuestionActivity;
import com.haiming.paper.ui.activity.QADetailsListActivity;
import com.sendtion.xrichtext.RichTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * tab 设配器
 */
public class HomeRecycerViewAdapter extends RecyclerBaseAdapter<Note, HomeRecycerViewAdapter.ViewHolder> {

    private List<User> mUserList = new ArrayList<>();

    public HomeRecycerViewAdapter(Context mContext, List<Note> dataList, List<User> userList) {
        super(mContext, dataList);
        this.mUserList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(getContext()).inflate(R.layout.question_show_tab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getDataList().get(position), position, this,mUserList.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll;
        private ImageView answerUserPic;
        private TextView answerUserName;
        private TextView rvAnswerQuestionTime;
        private TextView tvProblemTitle;
        private TextView answerQuestionDescription;
        private ImageView ivDislikeBtn;
        private TextView answerNum;
        private RichTextView mRichTextView;

        private Intent mIntent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerUserPic = itemView.findViewById(R.id.answer_user_pic);
            answerUserName = itemView.findViewById(R.id.answer_user_name);
            tvProblemTitle = itemView.findViewById(R.id.tv_problem_title);
            answerQuestionDescription = itemView.findViewById(R.id.answer_question_description);
            answerNum = itemView.findViewById(R.id.answer_number);
            ivDislikeBtn = itemView.findViewById(R.id.iv_dislike_btn);
            ll = itemView.findViewById(R.id.ll_question_have_answer);
            mRichTextView = itemView.findViewById(R.id.question_description);

        }

        public void bind(Note note, int pos, HomeRecycerViewAdapter adapter,User user) {
            mRichTextView.isNoTouch = true;
            if (Constante.isManage) {
                ivDislikeBtn.setVisibility(View.VISIBLE);
            }else {
                ivDislikeBtn.setVisibility(View.GONE);
            }
            String path = user.getImagePath();
            if (path != null) {
                byte[] bytes = UIUtil.string2byte(path);
                Log.d("数据", "用户图片=" + path);
                Glide.with(getContext()).load(bytes).circleCrop().into(answerUserPic);
            }
            answerUserName.setText(user.getName());
            tvProblemTitle.setText(note.getTitle());
            int size = note.getAnswerSize();
            if (size <= 0) {
                mRichTextView.post(() -> dealWithContent("该提问暂时没有回答"));
                answerNum.setText("查看问题详情");

            } else {
                answerNum.setText("该提问共有" + size + "回答");
                mRichTextView.post(() -> dealWithContent(note.getAnswerDescription()));
            }

            if (ivDislikeBtn.getVisibility() == View.VISIBLE) {
                ivDislikeBtn.setOnClickListener(v -> {
                    //管理员删除提问
                    PopupWindowUtil.showQAndADislikeWindow(getContext(), ivDislikeBtn, note.getId(), adapter, pos);
                });
            }


            ll.setOnClickListener(v -> {
                if (size <= 0) {
                    mIntent = new Intent(getContext(), QADetailsListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("note", note);
                    mIntent.putExtra("type", 0);
                    mIntent.putExtra("data", bundle);
                    getContext().startActivity(mIntent);
                    return;
                }
                mIntent = new Intent(getContext(), QAAnswerQuestionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("note", note);
                mIntent.putExtra("data", bundle);
                getContext().startActivity(mIntent);
            });


        }

        private void dealWithContent(String description) {
            mRichTextView.clearAllLayout();
            showDataSync(description);
        }

        /**
         * 异步方式显示正文内容数据
         */
        private void showDataSync(final String html) {
            Observable.create(new ObservableOnSubscribe<String>() {
                @Override
                public void subscribe(ObservableEmitter<String> emitter) {
                    showEditData(emitter, html);
                }
            })
                    //.onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())//生产事件在io
                    .observeOn(AndroidSchedulers.mainThread())//消费事件在UI线程
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {

                            com.haiming.paper.Utils.UIUtil.showToast(getContext(), "解析错误");
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String text) {
                            try {
                                if (mRichTextView != null) {
                                    if (text.contains("img=")) {
                                        String imagePath = StringUtils.getImgString(text);
                                        //  et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                        mRichTextView.addImageViewAtIndex(mRichTextView.getLastIndex(), imagePath);
                                    } else if (text.contains("video=")) {
                                        String[] viedoPath = StringUtils.getVideoImage(text);
                                        if (viedoPath[1] != null) {
                                            mRichTextView.imagePath = viedoPath[1];
                                            Log.d("数据", "图片=" + viedoPath[1]);
                                        }
                                        if (viedoPath[0] != null) {
                                            mRichTextView.addVideoViewAtIndex(mRichTextView.getLastIndex(), viedoPath[0]);
                                            Log.d("数据", "视频=" + viedoPath[0]);
                                        }

                                    } else {
                                        if (text.length() > 50) {
                                            text = text.substring(0, 50);
                                        }
                                        mRichTextView.addTextViewAtIndex(mRichTextView.getLastIndex(), text);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        /**
         * 将正文的string进行切割数据，并发送给观察者
         */
        protected void showEditData(ObservableEmitter<String> emitter, String html) {
            try {
                //这个方法是把string切割成list，然后把发送给onnext
                List<String> textList = StringUtils.spliteString(html);
                String text = textList.get(0);
                emitter.onNext(text);
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }

    }

}
