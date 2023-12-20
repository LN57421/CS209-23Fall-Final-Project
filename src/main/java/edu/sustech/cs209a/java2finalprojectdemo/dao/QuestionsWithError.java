package edu.sustech.cs209a.java2finalprojectdemo.dao;

import edu.sustech.cs209a.java2finalprojectdemo.model.Questions;

import javax.persistence.Id;
import java.util.Date;

public class QuestionsWithError {

    public int question_id;

    public int is_answered;

    public Date creation_date;

    public int score;

    public int view_count;

    public String title;

    public String body;
    private String errorName;

    public QuestionsWithError(int question_id, int is_answered, Date creation_date, int score, int view_count, String title, String body, String errorName) {
        this.question_id = question_id;
        this.is_answered = is_answered;
        this.creation_date = creation_date;
        this.score = score;
        this.view_count = view_count;
        this.title = title;
        this.body = body;
        this.errorName = errorName;
    }

    // Add other necessary methods or properties
}
