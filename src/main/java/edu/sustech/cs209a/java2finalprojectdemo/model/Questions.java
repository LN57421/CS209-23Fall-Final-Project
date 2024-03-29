package edu.sustech.cs209a.java2finalprojectdemo.model;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
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
    public int question_id;

    public int is_answered;

    public Date creation_date;

    public int score;

    public int view_count;

    public String title;

    public String body;

    public Questions(int question_id, int is_answered, Date creation_date, int score, int view_count, String title, String body) {
        this.question_id = question_id;
        this.is_answered = is_answered;
        this.score = score;
        this.creation_date = creation_date;
        this.view_count = view_count;
        this.title = title;
        this.body = body;
    }
//
//    public Questions() {
//    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public int getIs_answered() {
        return is_answered;
    }



    public int getScore() {
        return score;
    }

    public int getView_count() {
        return view_count;
    }


    public void setIs_answered(int is_answered) {
        this.is_answered = is_answered;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }


}