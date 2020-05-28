package com.haiming.paper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haiming.paper.R;
import com.haiming.paper.Utils.StringUtils;
import com.haiming.paper.Utils.UIUtil;
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.bean.User;
import com.haiming.paper.db.AnswerDao;
import com.haiming.paper.db.Constante;
import com.haiming.paper.ui.activity.QAInputAnswerActivity;
import com.hjq.toast.ToastUtils;
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

public class AnswerQuestionAdapter extends RecyclerView.Adapter<AnswerQuestionAdapter.ViewHolder> {

    private List<Answer> mAnswerList = new ArrayList<>();
    private Note mNote;
    private Context mContext;
    private List<User> mUsers = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private boolean isManager;
    public int type = 1;
    private CallBack callBack;

    public interface CallBack{
        void itemClick(Note note);
    }
    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public AnswerQuestionAdapter(Context context, List<Answer> answers, List<User> users, Note note, RecyclerView recyclerView, boolean manager, int type) {
        this.mContext = context;
        this.mAnswerList = answers;
        this.mUsers = users;
        this.mNote = note;
        this.mRecyclerView = recyclerView;
        this.isManager = manager;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isManager) {
            holder.iAnswerLl.setText("删除内容");
        }
        holder.bind(mUsers.get(position), mAnswerList.get(position), this, position);
    }

    @Override
    public int getItemCount() {
        Log.d("回答", "" + mAnswerList.size());
        return mAnswerList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTitle;
        private RichTextView questionDescription;
        private Button lookAllAnswer;
        private Button iAnswerLl;
        private TextView answerUserName;
        private RichTextView answerContent;
        private ImageView userImage;
        private TextView usertask;
        private RelativeLayout bottomrl;
        private int TYPE_NOTE = 1;
        private int TYPE_ANSWER = 2;
        private Answer mAnswer;
        private RichTextView.Callback mCallback;
        private Intent mIntent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lookAllAnswer = itemView.findViewById(R.id.look_all_answer);
            iAnswerLl = itemView.findViewById(R.id.i_answer_iv);
            answerUserName = itemView.findViewById(R.id.answer_user_name);
            answerContent = itemView.findViewById(R.id.answer_content);
            userImage = itemView.findViewById(R.id.user_image);
            usertask = itemView.findViewById(R.id.user_mark);
            mCallback = () -> mRecyclerView.requestDisallowInterceptTouchEvent(true);
        }

        public void bind(User user, Answer answer, AnswerQuestionAdapter adapter, int position) {
            this.mAnswer = answer;
            answerContent.setCallback(mCallback);
            if (type == 1) {
                lookAllAnswer.setText("删除回答");
                iAnswerLl.setText("修改回答");
            }

            answerContent.post(() -> dealWithContent(answerContent, TYPE_ANSWER));
            lookAllAnswer.setOnClickListener(v -> {
                if (type == 1) {
                    AnswerDao answerDao = new AnswerDao(mContext);
                    answerDao.deleteAnswer(answer.getAnswerId());
                    mAnswerList.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastUtils.show("删除成功");

                } else {
                    Log.d("点击","点击了");
                    if (callBack != null){
                        Log.d("点击","进入了");
                        callBack.itemClick(mNote);
                    }

                }
            });

            iAnswerLl.setOnClickListener(v -> {
                if (!Constante.isManage) {
                    mIntent = new Intent(mContext, QAInputAnswerActivity.class);
                    Bundle bundle = new Bundle();
                    if (type == 1) { //修改
                        bundle.putSerializable("note", mNote);
                        bundle.putSerializable("answer", answer);
                        mIntent.putExtra("data", bundle);
                        mIntent.putExtra("flag", 1);
                    } else {
                        //回答
                        bundle.putSerializable("note", mNote);
                        mIntent.putExtra("data", bundle);
                        mIntent.putExtra("flag", 0);
                    }
                    mContext.startActivity(mIntent);
                }else {
                    AnswerDao answerDao = new AnswerDao(mContext);
                    answerDao.deleteAnswer(answer.getAnswerId());
                    mAnswerList.remove(position);
                    adapter.notifyDataSetChanged();
                    ToastUtils.show("删除成功");
                }

            });


            if (user != null) {
                answerUserName.setText(user.getName());
                usertask.setText(user.getSignature());
                byte[] bytes = UIUtil.string2byte(user.getImagePath());
                Glide.with(mContext).load(bytes).circleCrop().into(userImage);
            }

        }

        private void dealWithContent(RichTextView editor, int type) {
            editor.clearAllLayout();
            if (type == TYPE_NOTE) {
                showDataSync(mNote.getContent(), editor);
            } else if (type == TYPE_ANSWER) {
                showDataSync(mAnswer.getAnswerContent(), editor);
                Log.d("回答", "内容=" + mAnswer.getAnswerContent());
            }
        }

        private void showDataSync(final String html, RichTextView editor) {
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

                            UIUtil.showToast(mContext, "解析错误：图片不存在或已损坏");
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            // subsLoading = d;
                        }

                        @Override
                        public void onNext(String text) {
                            try {
                                if (editor != null) {
                                    if (text.contains("img=")) {
                                        String imagePath = StringUtils.getImgString(text);
                                        //  et_new_content.addEditTextAtIndex(et_new_content.getLastIndex(), "");
                                        editor.addImageViewAtIndex(editor.getLastIndex(), imagePath);
                                    } else if (text.contains("video=")) {
                                        String[] viedoPath = StringUtils.getVideoImage(text);
                                        if (viedoPath[1] != null) {
                                            editor.imagePath = viedoPath[1];
                                            Log.d("数据", "图片=" + viedoPath[1]);
                                        }
                                        if (viedoPath[0] != null) {
                                            editor.addVideoViewAtIndex(editor.getLastIndex(), viedoPath[0]);
                                            Log.d("数据", "视频=" + viedoPath[0]);
                                        }
                                    } else {
                                        editor.addTextViewAtIndex(editor.getLastIndex(), text);
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
                for (int i = 0; i < textList.size(); i++) {
                    String text = textList.get(i);
                    emitter.onNext(text);
                }
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }

    }
}
