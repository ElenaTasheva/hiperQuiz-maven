package hiperQuiz.model;

import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Question extends BaseEntity<Long, Question> {

    private Quiz quiz; //reference to the Quiz the Question belongs;
    private String text; // - string 10 - 300 characters long, supporting Markdown syntax;
    private String picture; //(optional) - if the Question includes picture, valid URL;
    private List<Answer> answers; //list of Answer entities for the Question;

    public Question() {
    }

    public Question(String s) {
        this.answers = new ArrayList<>();
        this.text = s;
    }


    @ManyToOne(optional = false)
    @NonNull
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Column(nullable = false)
    @Size(min = 10, max = 300, message = "Question length must be between 10 and 300 characters long")
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

    @OneToMany(mappedBy = "question")
    @NonNull
    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    // to string for quiz representation
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(text + "?");
        return sb.toString();
    }
}
