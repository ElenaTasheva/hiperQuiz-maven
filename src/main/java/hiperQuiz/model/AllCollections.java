package hiperQuiz.model;

import java.io.Serializable;
import java.util.List;

public class AllCollections implements Serializable {
    private List<User> users;
    private List<Quiz> quizzes;
    private List<Player> players;
    private List<QuizResult> quizResults;

    public AllCollections() {
    }

    public AllCollections(List<User> users, List<Quiz> quizzes, List<Player> players, List<QuizResult> quizResults) {
        this.users = users;
        this.quizzes = quizzes;
        this.players = players;
        this.quizResults = quizResults;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<QuizResult> getQuizResults() {
        return quizResults;
    }

    public void setQuizResults(List<QuizResult> quizResults) {
        this.quizResults = quizResults;
    }
}
