package edu.sustech.cs209a.java2finalprojectdemo.model;


import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author ln
 */
@Entity
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int question_id;

    public Integer answer_count;

    public int is_answered;

    public int creation_date;

    public int score;

    public int view_count;

    public String tags;

}