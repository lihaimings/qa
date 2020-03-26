package com.haiming.paper.bean;

public class Answer {

    //    db.execSQL("create table db_answer
//    (a_id integer primary key autoincrement,
//    a_note_id integer,
//    a_content varchar,
//    a_user_id integer)");

    private int answerId; //回答id
    private int answerNoteId;
    private int answerUserId;
    private String answerContent;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getAnswerNoteId() {
        return answerNoteId;
    }

    public void setAnswerNoteId(int answerNoteId) {
        this.answerNoteId = answerNoteId;
    }

    public int getAnswerUserId() {
        return answerUserId;
    }

    public void setAnswerUserId(int answerUserId) {
        this.answerUserId = answerUserId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }
}
