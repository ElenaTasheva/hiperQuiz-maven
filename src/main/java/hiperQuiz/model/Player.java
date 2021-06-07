package hiperQuiz.model;


import hiperQuiz.model.enums.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends User {

    private List<QuizResult> results = new ArrayList<>();
    private int overallScore;
    private Rank rank;
    private int quizIndex = 0;

    public Player(String username) {
        super(username);
    }




    public int getOverallScore() {
        AtomicInteger score = new AtomicInteger();
         results.stream().forEach(quizResult -> {
            score.addAndGet(quizResult.getScore());
        });
         return score.get();
    }

    public Rank getRank() {
        if (overallScore < 50) {
            rank = Rank.JUNIOR;
        } else if (overallScore > 50 && overallScore < 100) {
            rank = Rank.MID;
        } else {
            rank = Rank.MASTER;
        }
        return rank;
    }

    public List<QuizResult> getResults() {
        return results;
    }





    public void setQuizIndex(int quizIndex) {
        this.quizIndex = quizIndex;
    }

    public void addQuizResult(QuizResult quizResult) {
        this.results.add(quizResult);
        quizIndex++;
    }

    public void setResults(List<QuizResult> results) {
        this.results = results;
    }



    public int getCurrentQuizScore() {
        return results.get(quizIndex).getScore();
    }
}