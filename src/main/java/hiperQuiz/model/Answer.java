package hiperQuiz.model;

import jdk.jfr.Enabled;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
public class Answer extends BaseEntity<Long, Answer> {

     private Question question; //reference to the Question to which the Answer belongs;
     private String text;// - string 2 - 150 characters long, supporting Markdown syntax;
     private String picture; // (optional) - if the Answer includes picture, valid URL;
     private int score ; //integer number (could be negative too);

    public Answer() {
    }

    public Answer(String text) {
        this.text = text;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Column(nullable = false)
    @Size(min = 2, max = 150, message = "Answer`s length must be between 2 and 150 characters long")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Column
    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(text);
        return sb.toString();
    }
}
