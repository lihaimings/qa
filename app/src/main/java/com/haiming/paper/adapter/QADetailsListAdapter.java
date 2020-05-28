package com.haiming.paper.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.Constante;
import com.sendtion.xrichtext.RichTextView;

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

public class QADetailsListAdapter extends RecyclerView.Adapter<QADetailsListAdapter.ViewHolder> {

    private Note mNote;
    private List<Answer> mAnswerList;
    private List<User> mUserList;
    private Context mContext;
    private CallBack mCallBack;

   public interface CallBack{
        void itemClick(int type,Answer answer,Note note,User user);
    }
    public CallBack getCallBack() {
        return mCallBack;
    }

    public void setCallBack(CallBack callBack) {
        mCallBack = callBack;
    }

    public List<Answer> getAnswerList() {
        return mAnswerList;
    }

    public List<User> getUserList() {
        return mUserList;
    }

    public QADetailsListAdapter(Context context, Note note, List<Answer> answers, List<User> users) {
        this.mNote = note;
        this.mContext = context;
        this.mAnswerList = answers;
        this.mUserList = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.question_answer_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mAnswerList.get(position) != null && mUserList.get(position) != null) {
            holder.bind(mAnswerList.get(position), mUserList.get(position),this,position);
        } else {
            Log.e("回答", "为空");
        }

    }

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView answerUserPic;
        private TextView answerUserName;
        private RichTextView mRichTextView;
        private ImageView ivDislikeBtn;
        private LinearLayout llQuestionHaveAnswer;

        private Intent mIntent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            answerUserPic = itemView.findViewById(R.id.answer_user_pic);
            answerUserName = itemView.findViewById(R.id.answer_user_name);
            mRichTextView = itemView.findViewById(R.id.question_description);
            ivDislikeBtn = itemView.findViewById(R.id.iv_dislike_btn);
            llQuestionHaveAnswer = itemView.findViewById(R.id.ll_question_have_answer);
        }


        public void bind(Answer mAnswer, User mUser,QADetailsListAdapter adapter,int pos) {
            if (Constante.isManage){
                ivDislikeBtn.setVisibility(View.VISIBLE);
            }else {
                ivDislikeBtn.setVisibility(View.GONE);
            }
            String path = mUser.getImagePath();
            if (path != null) {
                byte[] bytes = UIUtil.string2byte(path);
                Glide.with(mContext).load(bytes).circleCrop().into(answerUserPic);
            }

            answerUserName.setText(mUser.getName());
            mRichTextView.noTouch = true;
            mRichTextView.post(new Runnable() {
                @Override
                public void run() {
                    dealWithContent(mAnswer.getAnswerContent());
                }
            });

            ivDislikeBtn.setOnClickListener(v -> {
                PopupWindowUtil.showQAndADeleteContentWindow(mContext,ivDislikeBtn,mAnswer.getAnswerId(),adapter,pos);
            });

            llQuestionHaveAnswer.setOnClickListener(v -> {
                Log.d("点击","点击了");
                if (mCallBack != null){
                    mCallBack.itemClick(2,mAnswer,mNote,mUser);
                }

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

                            com.haiming.paper.Utils.UIUtil.showToast(mContext, "解析错误");
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
