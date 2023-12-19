package edu.sustech.cs209a.java2finalprojectdemo.model;

import jakarta.persistence.Id;

import javax.persistence.Entity;

@Entity
public class FatalErrors {

    public Integer id;
    public String body;
    public Integer question_id;
    public String name;

    public FatalErrors(Integer id, String body, Integer question_id, String name) {
        this.id = id;
        this.body = body;
        this.question_id = question_id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
