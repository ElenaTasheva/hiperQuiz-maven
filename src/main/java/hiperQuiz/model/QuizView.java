package hiperQuiz.model;

import java.util.Collection;
import java.util.List;

public class QuizView {
    private String question;
    private String answer;

    public QuizView() {
    }

    public QuizView(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
