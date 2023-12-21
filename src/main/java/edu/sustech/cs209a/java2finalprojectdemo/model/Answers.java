package edu.sustech.cs209a.java2finalprojectdemo.model;

import jakarta.persistence.Id;

import javax.persistence.Entity;

@Entity
public class Answers {
    @Id
    public Integer answer_id;
    public Integer question_id;
    public Integer score;
    public String body;


    public Answers(Integer answer_id, Integer question_id, Integer score, String body) {
        this.answer_id = answer_id;
        this.question_id = question_id;
        this.score = score;
        this.body = body;
    }
//
//    public Answers() {
//    }

    public Integer getAnswer_id() {
        return answer_id;
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

    public void setAnswer_id(Integer answer_id) {
        this.answer_id = answer_id;
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
        return "Answers{" +
                ", answer_id=" + answer_id +
                ", question_id=" + question_id +
                ", score=" + score +
                ", body='" + body + '\'' +
                '}';
    }
}

