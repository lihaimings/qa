package com.haiming.paper.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.haiming.paper.R;
import com.haiming.paper.adapter.HomeRecycerViewAdapter;
import com.haiming.paper.adapter.LookMyAnswerAdapter;
import com.haiming.paper.adapter.LookMyQuestionAdapter;
import com.haiming.paper.adapter.LookUserListAdapter;
import com.haiming.paper.adapter.QADetailsListAdapter;
import com.haiming.paper.bean.Answer;
import com.haiming.paper.bean.Note;
import com.haiming.paper.db.AnswerDao;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.thread.ThreadManager;
import com.haiming.paper.ui.activity.QAAskQuestionActivity;
import com.haiming.paper.ui.activity.QAInputAnswerActivity;
import com.haiming.paper.ui.activity.video.ModifyMyInformation;
import com.haiming.paper.ui.view.window.CommonPopupWindow;

public class PopupWindowUtil {

    private static CommonPopupWindow qAndADislikeWindow;

    public static void showQAndADislikeWindow(Context context, View anchor, int noteId, final HomeRecycerViewAdapter adapter, final int pos) {
        int width = UIUtil.getResources().getDimensionPixelSize(R.dimen.x76);
        qAndADislikeWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.popup_window_dislike_q_and_a)
                .setWidthAndHeight(width, width)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    view.setOnClickListener(v -> {
                        ThreadManager.getLongPool().execute(() -> {
                            NoteDao noteDao = new NoteDao(context);
                            noteDao.deleteNote(noteId);
                        });
                        adapter.getDataList().remove(pos);
                        adapter.notifyDataSetChanged();
                        qAndADislikeWindow.dismiss();
                    });
                })
                .create();

        qAndADislikeWindow.setOnDismissListener(() -> {
            qAndADislikeWindow = null;
        });

        int xoff = UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75) - anchor.getWidth();
        qAndADislikeWindow.showAsDropDown(anchor, -xoff, -UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75));
    }

    private static CommonPopupWindow qAndADeleteWindow;

    public static void showQAndADeleteWindow(Context context, View anchor, int noteId, final LookMyQuestionAdapter adapter, final int pos) {
        int width = UIUtil.getResources().getDimensionPixelSize(R.dimen.x76);
        qAndADeleteWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.window_delete_updata)
                .setWidthAndHeight(width, width)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    Button delete = view.findViewById(R.id.delete_btn);
                    Button update = view.findViewById(R.id.update_btn);

                    delete.setOnClickListener(v -> {
                        NoteDao noteDao = new NoteDao(context);
                        noteDao.deleteNote(noteId);
                        adapter.getDataList().remove(pos);
                        adapter.notifyDataSetChanged();
                        qAndADeleteWindow.dismiss();
                    });

                    update.setOnClickListener(v -> {
                        NoteDao noteDao = new NoteDao(context);
                        Note note = new Note();
                        note = noteDao.queryNoteId(noteId);
                        Intent intent = new Intent(context, QAAskQuestionActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", note);
                        intent.putExtra("data", bundle);
                        intent.putExtra("flag", 1);
                        context.startActivity(intent);
                    });

                })
                .create();

        qAndADeleteWindow.setOnDismissListener(() -> {
            qAndADeleteWindow = null;
        });

        int xoff = UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75) - anchor.getWidth();
        qAndADeleteWindow.showAsDropDown(anchor, -xoff, -UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75));
    }

    private static CommonPopupWindow qAndADeleteContentWindow;

    public static void showQAndADeleteContentWindow(Context context, View anchor, Answer answer, final LookMyAnswerAdapter adapter, final int pos) {
        int width = UIUtil.getResources().getDimensionPixelSize(R.dimen.x76);
        qAndADeleteContentWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.window_delete_updata)
                .setWidthAndHeight(width, width)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    Button delete = view.findViewById(R.id.delete_btn);
                    Button update = view.findViewById(R.id.update_btn);

                    delete.setOnClickListener(v -> {
                        AnswerDao answerDao = new AnswerDao(context);
                        answerDao.deleteAnswer(answer.getAnswerId());
                        adapter.getDataList().remove(pos);
                        adapter.notifyDataSetChanged();
                        qAndADeleteContentWindow.dismiss();
                    });

                    update.setOnClickListener(v -> {
                        NoteDao noteDao = new NoteDao(context);
                        Note mNote =noteDao.queryNoteId(answer.getAnswerNoteId());
                        Intent mIntent = new Intent(context, QAInputAnswerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("note", mNote);
                        bundle.putSerializable("answer", answer);
                        mIntent.putExtra("data", bundle);
                        mIntent.putExtra("flag", 1);
                        context.startActivity(mIntent);
                    });

                })
                .create();

        qAndADeleteContentWindow.setOnDismissListener(() -> {
            qAndADeleteContentWindow = null;
        });

        int xoff = UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75) - anchor.getWidth();
        qAndADeleteContentWindow.showAsDropDown(anchor, -xoff, -UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75));
    }


    public static void showQAndADeleteContentWindow(Context context, View anchor, int answerId, final QADetailsListAdapter adapter, final int pos) {
        int width = UIUtil.getResources().getDimensionPixelSize(R.dimen.x76);
        qAndADislikeWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.popup_window_dislike_q_and_a)
                .setWidthAndHeight(width, width)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    view.setOnClickListener(v -> {
                        ThreadManager.getLongPool().execute(() -> {
                            AnswerDao answerDao = new AnswerDao(context);
                            answerDao.deleteAnswer(answerId);
                        });
                        adapter.getAnswerList().remove(pos);
                        adapter.getUserList().remove(pos);
                        adapter.notifyDataSetChanged();
                        qAndADislikeWindow.dismiss();
                    });
                })
                .create();

        qAndADislikeWindow.setOnDismissListener(() -> {
            qAndADislikeWindow = null;
        });

        int xoff = UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75) - anchor.getWidth();
        qAndADislikeWindow.showAsDropDown(anchor, -xoff, -UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75));
    }

    private static CommonPopupWindow qAndADeleteAskWindow;
    public static void showQAndADeleteAskWindow(Context context, View anchor, int noteId) {
        int width = UIUtil.getResources().getDimensionPixelSize(R.dimen.x76);
        qAndADeleteAskWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.popup_window_dislike_q_and_a)
                .setWidthAndHeight(width, width)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    view.setOnClickListener(v -> {
                        ThreadManager.getLongPool().execute(() -> {
                            NoteDao noteDao = new NoteDao(context);
                            noteDao.deleteNote(noteId);
                        });

                        qAndADeleteAskWindow.dismiss();
                    });
                })
                .create();

        qAndADeleteAskWindow.setOnDismissListener(() -> {
            qAndADeleteAskWindow = null;
        });

        int xoff = UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75) - anchor.getWidth();
        qAndADeleteAskWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
       // qAndADeleteAskWindow.showAsDropDown(anchor, -xoff, -UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75));
    }


    private static CommonPopupWindow howQAndAWindow;

    public static void showHowQAndADislikeWindow(Context context, View anchor) {
        howQAndAWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.window_what_good_question)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    ImageView imageView = view.findViewById(R.id.iv_close_window);
                    imageView.setOnClickListener(v -> {
                        howQAndAWindow.dismiss();
                    });
                })
                .create();

        howQAndAWindow.setOnDismissListener(() -> {
            howQAndAWindow = null;
        });

        howQAndAWindow.showAtLocation(anchor, Gravity.CENTER, 0, 0);
    }


    private static CommonPopupWindow qAndADeleteUserWindow;

    public static void showQAndADeleteUserWindow(Context context, View anchor, int userId, final LookUserListAdapter adapter, final int pos) {
        int width = UIUtil.getResources().getDimensionPixelSize(R.dimen.x76);
        qAndADeleteUserWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.window_delete_updata)
                .setWidthAndHeight(width, width)
                .setOutsideTouchable(true)
                .setViewOnclickListener((view, layoutResId, popupWindow) -> {
                    Button delete = view.findViewById(R.id.delete_btn);
                    Button update = view.findViewById(R.id.update_btn);

                    delete.setOnClickListener(v -> {
                        UserDao userDao = new UserDao(context);
                        userDao.deleteUser(userId);
                        adapter.getDataList().remove(pos);
                        adapter.notifyDataSetChanged();
                        qAndADeleteUserWindow.dismiss();
                    });

                    update.setOnClickListener(v -> {
                        Intent intent = new Intent(context, ModifyMyInformation.class);
                        intent.putExtra("userId", userId);
                        context.startActivity(intent);
                    });

                })
                .create();

        qAndADeleteUserWindow.setOnDismissListener(() -> {
            qAndADeleteUserWindow = null;
        });

        int xoff = UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75) - anchor.getWidth();
        qAndADeleteUserWindow.showAsDropDown(anchor, -xoff, -UIUtil.getResources().getDimensionPixelOffset(R.dimen.x75));
    }



}
