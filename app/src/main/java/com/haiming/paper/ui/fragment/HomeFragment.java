package com.haiming.paper.ui.fragment;

import com.haiming.paper.bean.Note;

public class HomeFragment extends BaseQATavFragment {

//    public NoteDao mNoteDao;
////    public GroupDao mGroupDao;
////    @Override
////    protected void loadData() {
////        mNoteDao = new NoteDao(getActivity());
////        mGroupDao = new GroupDao(getActivity());
//////        int id = mGroupDao.queryByNameToId("首页");
////        if (mNoteDao != null){
////            dataList = mNoteDao.queryNotesAll(1);
////            Log.d("数据","已实例化");
////        }else {
////            Log.d("数据","没有实例化");
////        }
////
////}


    @Override
    protected void loadData() {
        super.loadData();
        for (int i= 0;i<10;i++){
            Note note = new Note();
            note.setContent("你好");
            dataList.add(note);
        }
    }
}
