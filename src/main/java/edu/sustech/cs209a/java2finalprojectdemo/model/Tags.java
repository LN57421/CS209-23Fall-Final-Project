package edu.sustech.cs209a.java2finalprojectdemo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tags {

    public Integer id;

    public String name;
    public int score;
    public int view_count;
    public int question_id;

    public Tags(Integer id, String name, int score, int view_count, int question_id) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.view_count = view_count;
        this.question_id = question_id;
    }

    public Tags() {
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getView_count() {
        return view_count;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", view_count=" + view_count +
                ", question_id=" + question_id +
                '}';
    }
}

