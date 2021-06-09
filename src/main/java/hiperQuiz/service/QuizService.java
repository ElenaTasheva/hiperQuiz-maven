package hiperQuiz.service;

import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.QuizResultRepository;
import hiperQuiz.model.Player;
import hiperQuiz.model.Quiz;
import hiperQuiz.model.User;

import java.util.Collection;
import java.util.Optional;

public interface QuizService {

    QuizRepository getQuizRepository();

    Optional<Quiz> findById(long quizId);

    Quiz create(Quiz quiz);

    Collection<?> findAll();

    Quiz createQuiz(User user);

    Player play(Player player);
}
