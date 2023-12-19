package edu.sustech.cs209a.java2finalprojectdemo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Tags {

    public Tags(Integer id, String name, int score, int view_count, int count) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.view_count = view_count;
        this.count = count;
    }

    @Id
    public Integer id;

    public String name;
    public int score;
    public int view_count;
    public int count;

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

    public int getCount() {
        return count;
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

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Tags{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", view_count=" + view_count +
                ", count=" + count +
                '}';
    }
}

