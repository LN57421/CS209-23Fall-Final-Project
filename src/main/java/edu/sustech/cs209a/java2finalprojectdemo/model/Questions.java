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

    public int getQuestion_id() {
        return question_id;
    }

    public Integer getAnswer_count() {
        return answer_count;
    }

    public int getIs_answered() {
        return is_answered;
    }

    public int getCreation_date() {
        return creation_date;
    }

    public int getScore() {
        return score;
    }

    public int getView_count() {
        return view_count;
    }

    public String getTags() {
        return tags;
    }

    public void setAnswer_count(Integer answer_count) {
        this.answer_count = answer_count;
    }

    public void setIs_answered(int is_answered) {
        this.is_answered = is_answered;
    }

    public void setCreation_date(int creation_date) {
        this.creation_date = creation_date;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "question_id=" + question_id +
                ", answer_count=" + answer_count +
                ", is_answered=" + is_answered +
                ", creation_date=" + creation_date +
                ", score=" + score +
                ", view_count=" + view_count +
                ", tags='" + tags + '\'' +
                '}';
    }
}