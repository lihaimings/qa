package com.haiming.paper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.haiming.paper.Utils.CommonUtil;
import com.haiming.paper.Utils.StringUtils;
import com.haiming.paper.bean.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NoteDao {
    private MyOpenHelper helper;

    public NoteDao(Context context) {
        helper = new MyOpenHelper(context);
    }

    /**
     * 查询所有笔记
     */
    public List<Note> queryNotesAll(int groupId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Note> noteList = new ArrayList<>();
        Note note;
        String sql;
        Cursor cursor = null;
        try {
//            if (groupId > 0){
//                sql = "select * from db_note where n_group_id =" + groupId +
//                        "order by n_create_time desc";
//            } else {
//                sql = "select * from db_note " ;
//            }
//            cursor = db.rawQuery(sql, null);
            cursor = db.query("db_note", null, "n_group_id=?", new String[]{groupId + ""}, null, null, "n_id desc");

            while (cursor.moveToNext()) {
                //循环获得展品信息
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("n_id")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("n_title")));
                note.setContent(cursor.getString(cursor.getColumnIndex("n_content")));
                note.setGroupId(cursor.getInt(cursor.getColumnIndex("n_group_id")));
                note.setGroupName(cursor.getString(cursor.getColumnIndex("n_group_name")));
                note.setType(cursor.getInt(cursor.getColumnIndex("n_type")));
                note.setBgColor(cursor.getString(cursor.getColumnIndex("n_bg_color")));
                note.setIsEncrypt(cursor.getInt(cursor.getColumnIndex("n_encrypt")));
                note.setCreateTime(cursor.getString(cursor.getColumnIndex("n_create_time")));
                note.setUpdateTime(cursor.getString(cursor.getColumnIndex("n_update_time")));
                note.setUserId(cursor.getInt(cursor.getColumnIndex("n_user_id")));
                note.setAnswerSize(cursor.getInt(cursor.getColumnIndex("n_answer_size")));
                note.setAnswerId(cursor.getString(cursor.getColumnIndex("n_answer_id")));
                note.setAnswerDescription(cursor.getString(cursor.getColumnIndex("n_answer_description")));
                noteList.add(note);
            }
        } catch (Exception e) {
            Log.d("数据", "查询抛异常");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return noteList;
    }

    /**
     * 根据用户id查询提问
     */
    public List<Note> queryNotesByUserId(int userId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Note> noteList = new ArrayList<>();
        Note note;
        String sql;
        Cursor cursor = null;
        try {
            cursor = db.query("db_note", null, "n_user_id=?", new String[]{userId + ""}, null, null, "n_id desc");

            while (cursor.moveToNext()) {
                //循环获得展品信息
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("n_id")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("n_title")));
                note.setContent(cursor.getString(cursor.getColumnIndex("n_content")));
                note.setGroupId(cursor.getInt(cursor.getColumnIndex("n_group_id")));
                note.setGroupName(cursor.getString(cursor.getColumnIndex("n_group_name")));
                note.setType(cursor.getInt(cursor.getColumnIndex("n_type")));
                note.setBgColor(cursor.getString(cursor.getColumnIndex("n_bg_color")));
                note.setIsEncrypt(cursor.getInt(cursor.getColumnIndex("n_encrypt")));
                note.setCreateTime(cursor.getString(cursor.getColumnIndex("n_create_time")));
                note.setUpdateTime(cursor.getString(cursor.getColumnIndex("n_update_time")));
                note.setUserId(cursor.getInt(cursor.getColumnIndex("n_user_id")));
                note.setAnswerSize(cursor.getInt(cursor.getColumnIndex("n_answer_size")));
                note.setAnswerId(cursor.getString(cursor.getColumnIndex("n_answer_id")));
                note.setAnswerDescription(cursor.getString(cursor.getColumnIndex("n_answer_description")));
                noteList.add(note);
            }
        } catch (Exception e) {
            Log.d("数据", "查询抛异常");
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return noteList;
    }

    public Note queryNoteId(int noteId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Note note;
        String sql;
        Cursor cursor = null;
        try {

//            db.execSQL("create table db_note(n_id integer primary key autoincrement, n_title varchar, " +
//                    "n_content varchar, n_group_id integer, n_group_name varchar, n_type integer, " +
//                    "n_bg_color varchar, n_encrypt integer, n_create_time datetime," +
//                    "n_update_time datetime ,n_user_id integer,n_answer_size integer,n_answer_id varchar,n_answer_description varchar)");

            cursor = db.query("db_note", null, "n_id=?", new String[]{noteId + ""}, null, null, null);

            while (cursor.moveToNext()) {
                Log.d("列表", "查询问题开始");
                //循环获得展品信息
                note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex("n_id")));
                note.setTitle(cursor.getString(cursor.getColumnIndex("n_title")));
                note.setContent(cursor.getString(cursor.getColumnIndex("n_content")));
                note.setGroupId(cursor.getInt(cursor.getColumnIndex("n_group_id")));
                note.setGroupName(cursor.getString(cursor.getColumnIndex("n_group_name")));
                note.setType(cursor.getInt(cursor.getColumnIndex("n_type")));
                note.setBgColor(cursor.getString(cursor.getColumnIndex("n_bg_color")));
                note.setIsEncrypt(cursor.getInt(cursor.getColumnIndex("n_encrypt")));
                note.setCreateTime(cursor.getString(cursor.getColumnIndex("n_create_time")));
                note.setUpdateTime(cursor.getString(cursor.getColumnIndex("n_update_time")));
                note.setUserId(cursor.getInt(cursor.getColumnIndex("n_user_id")));
                note.setAnswerSize(cursor.getInt(cursor.getColumnIndex("n_answer_size")));
                note.setAnswerId(cursor.getString(cursor.getColumnIndex("n_answer_id")));
                note.setAnswerDescription(cursor.getString(cursor.getColumnIndex("n_answer_description")));
                Log.d("列表", "查询问题开始");
                return note;
            }
        } catch (Exception e) {
            Log.d("列表", "查询note在queryNoteId()中异常"+noteId);
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    public void updataAnswerSize(int noteId) {
        updataAnswerSize(noteId, "");
    }

    public void updataAnswerSize(int noteId, String content) {
        try {
            Note note = queryNoteId(noteId);
            if (note != null) {
                int size = note.getAnswerSize();
                if (size <= 0) {
                    List<String> contents = StringUtils.spliteString(content);
                    String description = contents.get(0);
                    note.setAnswerDescription(description);
                    note.setAnswerSize(1);
                } else {
                    int s = size + 1;
                    note.setAnswerSize(s);
                }
                updateNote(note);
            }
        }catch (Exception e){
            Log.d("列表", "问题在updataAnswerSize()中异常,id ="+noteId);
            e.printStackTrace();
        }

    }

    /**
     * 插入笔记
     */
    public long insertNote(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into db_note(n_title,n_content,n_group_id,n_group_name," +
                "n_type,n_bg_color,n_encrypt,n_create_time,n_update_time,n_user_id,n_answer_size,n_answer_id,n_answer_description) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        long ret = 0;
        //sql = "insert into ex_user(eu_login_name,eu_create_time,eu_update_time) values(?,?,?)";
        SQLiteStatement stat = db.compileStatement(sql);
        db.beginTransaction();
        try {
            stat.bindString(1, note.getTitle());
            stat.bindString(2, note.getContent());
            stat.bindLong(3, note.getGroupId());
            stat.bindString(4, note.getGroupName());
            stat.bindLong(5, note.getType());
            stat.bindString(6, note.getBgColor());
            stat.bindLong(7, note.getIsEncrypt());
            stat.bindString(8, CommonUtil.date2string(new Date()));
            stat.bindString(9, CommonUtil.date2string(new Date()));
            stat.bindLong(10, note.getUserId());
            stat.bindLong(11, note.getAnswerSize());
            stat.bindString(12, note.getAnswerId());
            stat.bindString(13, note.getAnswerDescription());
            ret = stat.executeInsert();
            db.setTransactionSuccessful();
            Log.d("数据", "已插入笔记");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return ret;
    }


    public void insertUser(Note note) {
        SQLiteDatabase db = helper.getWritableDatabase();

//        db.execSQL("create table db_note(n_id integer primary key autoincrement, n_title varchar, " +
//                "n_content varchar, n_group_id integer, n_group_name varchar, n_type integer, " +
//                "n_bg_color varchar, n_encrypt integer, n_create_time datetime," +
//                "n_update_time datetime ,n_user_id integer,n_answer_size integer,n_answer_id varchar)");

        Log.i("数据", "开始增加");
        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put("n_title", note.getTitle());
            values.put("n_content", note.getContent());
            values.put("n_group_id", note.getGroupId());
            values.put("n_group_name", note.getGroupName());
            values.put("n_type", note.getType());
            values.put("n_bg_color", note.getBgColor());
            values.put("n_encrypt", note.getIsEncrypt());
            values.put("n_update_time", CommonUtil.date2string(new Date()));
            values.put("n_user_id", note.getUserId());
            values.put("n_answer_size", note.getAnswerSize());
            values.put("n_answer_id", note.getAnswerId());
            values.put("n_answer_description", note.getAnswerDescription());
            db.insert("db_note", null, values);
            Log.i("数据", "数据已经增加");
        } catch (Exception e) {
            Log.i("数据", "数据增加异常");
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 更新笔记
     *
     * @param note
     */
    public void updateNote(Note note) {
//        db.execSQL("create table db_note(n_id integer primary key autoincrement, n_title varchar, " +
//                "n_content varchar, n_group_id integer, n_group_name varchar, n_type integer, " +
//                "n_bg_color varchar, n_encrypt integer, n_create_time datetime," +
//                "n_update_time datetime ,n_user_id integer,n_answer_size integer,n_answer_id varchar,n_answer_description varchar)");

        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            Log.d("列表", "开始更新问题");
            ContentValues values = new ContentValues();
            values.put("n_title", note.getTitle());
            values.put("n_content", note.getContent());
            values.put("n_group_id", note.getGroupId());
            values.put("n_group_name", note.getGroupName());
            values.put("n_type", note.getType());
            values.put("n_bg_color", note.getBgColor());
            values.put("n_encrypt", note.getIsEncrypt());
            values.put("n_create_time",note.getCreateTime());
            values.put("n_update_time", CommonUtil.date2string(new Date()));
            values.put("n_user_id", note.getUserId());
            values.put("n_answer_size", note.getAnswerSize());
            values.put("n_answer_id", note.getAnswerId());
            values.put("n_answer_description", note.getAnswerDescription());
            db.update("db_note", values, "n_id=?", new String[]{note.getId() + ""});
            Log.d("列表", "更新问题结束");
        } catch (Exception e) {
            Log.d("列表", "更新问题异常");
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除笔记
     */
    public int deleteNote(int noteId) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            ret = db.delete("db_note", "n_id=?", new String[]{noteId + ""});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }

    /**
     * 批量删除笔记
     *
     * @param mNotes
     */
    public int deleteNote(List<Note> mNotes) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int ret = 0;
        try {
            if (mNotes != null && mNotes.size() > 0) {
                db.beginTransaction();//开始事务
                try {
                    for (Note note : mNotes) {
                        ret += db.delete("db_note", "n_id=?", new String[]{note.getId() + ""});
                    }
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }
}
