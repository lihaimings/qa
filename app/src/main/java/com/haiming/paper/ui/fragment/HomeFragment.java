package com.haiming.paper.ui.fragment;

import android.content.Context;
import android.util.Log;

import com.haiming.paper.bean.Note;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.db.UserDao;
import com.haiming.paper.thread.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseQATavFragment {

    private NoteDao mNoteDao;
    private Context mContext = getActivity();
    private UserDao mUserDao;

    public HomeFragment() {
    }

    @Override
    protected void loadData() {
        ThreadManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao = new NoteDao(getContext());
                mUserDao = new UserDao(getContext());

                if (mNoteDao != null) {
                    List<Note> notes = new ArrayList<>();
                    notes = mNoteDao.queryNotesAll(2);
                    if (notes != null) {
                        dataList.addAll(notes);
                        Log.d("数据", "大小=" + dataList.size() + "");
                    }
                    if (dataList != null || dataList.size() > 0){
                        for (Note note : dataList){
                            mUserList.add(mUserDao.queryUserById(note.getUserId()));
                        }
                    }
                } else {
                    Log.d("数据", "笔记未实例化");
                }

            }
        });
    }
}
