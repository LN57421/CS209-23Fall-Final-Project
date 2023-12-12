package edu.sustech.cs209a.java2finalprojectdemo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Comments {

    @Id
    public Integer comment_id;
    public Integer question_id;
    public String body;

}
