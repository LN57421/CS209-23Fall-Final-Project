package edu.sustech.cs209a.java2finalprojectdemo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Comments {

    public Integer comment_id;

    public Integer question_id;
    public Integer score;
    public String body;

    public Comments(Integer comment_id, Integer question_id, Integer score, String body) {
        this.comment_id = comment_id;
        this.question_id = question_id;
        this.score = score;
        this.body = body;
    }

    public Comments() {
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public Integer getScore() {
        return score;
    }

    public String getBody() {
        return body;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Comments{" +
                ", comment_id=" + comment_id +
                ", question_id=" + question_id +
                ", score=" + score +
                ", body='" + body + '\'' +
                '}';
    }
}
