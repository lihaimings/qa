package com.haiming.paper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.haiming.paper.bean.Answer;

import java.util.ArrayList;
import java.util.List;

public class AnswerDao {
    private MyOpenHelper mHelper;

    public AnswerDao(Context context) {
        mHelper = new MyOpenHelper(context);
    }

//    db.execSQL("create table db_answer
//    (a_id integer primary key autoincrement,
//    a_note_id integer,
//    a_content varchar,
//    a_user_id integer)");


    /**
     * 查询所有的用户
     */
    public List<Answer> queryAnswerAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<Answer> userList = new ArrayList<>();

        Answer answer;
        Cursor cursor = null;
        try {
            cursor = db.query("db_answer", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int answerId = cursor.getInt(cursor.getColumnIndex("a_id"));
                int answerNoteId = cursor.getInt(cursor.getColumnIndex("a_note_id"));
                int answerUserId = cursor.getInt(cursor.getColumnIndex("a_user_id"));
                String answerContent = cursor.getString(cursor.getColumnIndex("a_content"));
                //生成一个类
                answer = new Answer();
                answer.setAnswerId(answerId);
                answer.setAnswerNoteId(answerNoteId);
                answer.setAnswerUserId(answerUserId);
                answer.setAnswerContent(answerContent);
                userList.add(answer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return userList;
    }

    /**
     * 根据回答的Id进行查找
     */
    public Answer queryByAanswerId(int answerId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        Answer answer;
        Cursor cursor = null;
        try {
            cursor = db.query("db_answer", null, "a_id=?", new String[]{answerId + ""}, null, null, null);
            while (cursor.moveToNext()) {
                int answerNoteId = cursor.getInt(cursor.getColumnIndex("a_note_id"));
                int answerUserId = cursor.getInt(cursor.getColumnIndex("a_user_id"));
                String answerContent = cursor.getString(cursor.getColumnIndex("a_content"));
                //生成一个类
                answer = new Answer();
                answer.setAnswerId(answerId);
                answer.setAnswerNoteId(answerNoteId);
                answer.setAnswerUserId(answerUserId);
                answer.setAnswerContent(answerContent);
                return answer;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return null;
    }

    /**
     * 根据用户id查询
     */
    public List<Answer> queryUserById(int userId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<Answer> userList = new ArrayList<>();

        Answer answer = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_answer", null, "a_user_id=?", new String[]{userId + ""}, null, null, null);
            while (cursor.moveToNext()) {
                int answerId = cursor.getInt(cursor.getColumnIndex("a_id"));
                int answerNoteId = cursor.getInt(cursor.getColumnIndex("a_note_id"));
                int answerUserId = cursor.getInt(cursor.getColumnIndex("a_user_id"));
                String answerContent = cursor.getString(cursor.getColumnIndex("a_content"));
                //生成一个类
                answer = new Answer();
                answer.setAnswerId(answerId);
                answer.setAnswerNoteId(answerNoteId);
                answer.setAnswerUserId(answerUserId);
                answer.setAnswerContent(answerContent);
                userList.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return userList;
    }

    /**
     * 根据问题id查询回答
     *
     * @return
     */
    public List<Answer> queryNoteById(int noteId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        List<Answer> userList = new ArrayList<>();

        Answer answer = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_answer", null, "a_note_id=?", new String[]{noteId + ""}, null, null, null);
            while (cursor.moveToNext()) {
                int answerId = cursor.getInt(cursor.getColumnIndex("a_id"));
                int answerNoteId = cursor.getInt(cursor.getColumnIndex("a_note_id"));
                int answerUserId = cursor.getInt(cursor.getColumnIndex("a_user_id"));
                String answerContent = cursor.getString(cursor.getColumnIndex("a_content"));
                //生成一个类
                answer = new Answer();
                answer.setAnswerId(answerId);
                answer.setAnswerNoteId(answerNoteId);
                answer.setAnswerUserId(answerUserId);
                answer.setAnswerContent(answerContent);
                userList.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return userList;
    }


    /**
     * 添加一个回答
     */
    public void insertAnswer(Context context, Answer answer) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        NoteDao noteDao = new NoteDao(context);

        Cursor cursor = null;
        try {
            ContentValues values = new ContentValues();
            values.put("a_note_id", answer.getAnswerNoteId());
            values.put("a_user_id", answer.getAnswerUserId());
            values.put("a_content", answer.getAnswerContent());
            db.insert("db_answer", null, values);
            noteDao.updataAnswerSize(answer.getAnswerNoteId(),answer.getAnswerContent());
            Log.d("回答","存储成功");
        } catch (Exception e) {
            Log.d("回答","存储异常");
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
     * 更新一个回答
     */
    public void updateAnswer(Answer answer) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put("a_id", answer.getAnswerId());
            values.put("a_note_id", answer.getAnswerNoteId());
            values.put("a_user_id", answer.getAnswerUserId());
            values.put("a_content", answer.getAnswerContent());
            db.update("db_answer", values, "a_id=?", new String[]{answer.getAnswerId() + ""});
            Log.d("更新问题","完成");
        } catch (Exception e) {
            Log.d("更新问题","异常");
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * 删除一个回答
     */
    public int deleteAnswer(int answerId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        int ret = 0;
        try {
            ret = db.delete("db_answer", "a_id=?", new String[]{answerId + ""});
            Log.d("删除回答","完成");
        } catch (Exception e) {
            Log.d("删除回答","异常");
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return ret;
    }


}
