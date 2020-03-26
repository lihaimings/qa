package com.haiming.paper.ui.fragment;

import android.content.Context;
import android.util.Log;

import com.haiming.paper.bean.Note;
import com.haiming.paper.db.GroupDao;
import com.haiming.paper.db.NoteDao;
import com.haiming.paper.thread.ThreadManager;

import java.util.ArrayList;
import java.util.List;

public class CarFragment extends BaseQATavFragment {

    private GroupDao mGroupDao;
    private NoteDao mNoteDao;
    private Context mContext ;

    public CarFragment(Context context){
        this.mContext = context;
    }

    @Override
    protected void loadData() {
        ThreadManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                mNoteDao = new NoteDao(mContext);
//        int id = mGroupDao.queryGroupById(2);
                if (mNoteDao != null){
                    Log.d("数据","笔记已实例化");
                    List<Note> notes = new ArrayList<>();
                    dataList = mNoteDao.queryNotesAll(2);
                    for (Note note:dataList){
                        Log.d("数据","dataList"+note.getContent());
                    }
                }else {
                    Log.d("数据","笔记未实例化");
                }

            }
        });

    }

//    @Override
//    protected void loadData() {
//        super.loadData();
//        for (int i= 0;i<10;i++){
//            Note note = new Note();
//            note.setContent("你好");
//            dataList.add(note);
//        }
//    }
}
