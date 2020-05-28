package com.haiming.paper.bean;

import java.io.Serializable;

public class Answer implements Serializable {

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
