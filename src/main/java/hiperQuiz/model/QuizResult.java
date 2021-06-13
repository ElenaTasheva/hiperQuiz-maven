package hiperQuiz.model;

import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "quiz_results")
public class QuizResult extends BaseEntity<Long, QuizResult> {

    //todo check if player can be Administrator as well;
    // changing player entity to User as there wont be a PLayerRepo or Table players
    private User player; //the -  reference to the User (Player) taking the 'Quiz;
    private Quiz quiz; //quiz - reference to the Quiz taken;
    private int score; // (sum of Answer scores for all answered questions);

    public QuizResult() {
    }

    public QuizResult(Player player, Quiz quiz) {
        this.player = player;
        this.quiz = quiz;
    }

    @ManyToOne(optional = false)
    @NonNull
    public User getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @ManyToOne(optional = false)
    @NonNull
    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Transient
    public int getScore() {
        return score;
    }

   public void calculateScore(Answer answer){
         score += answer.getScore();
   }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuizResult{");
        sb.append("created=").append(getCreated());
        sb.append(", id=").append(getId());
        sb.append(", player=").append(player);
        sb.append(", quiz=").append(quiz);
        sb.append(", score=").append(score);
        sb.append('}');
        return sb.toString();
    }


}
