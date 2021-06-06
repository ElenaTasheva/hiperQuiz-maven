package hiperQuiz.service;

import hiperQuiz.dao.QuizRepository;
import hiperQuiz.model.Quiz;

import java.util.Collection;
import java.util.Optional;

public interface QuizService {

    QuizRepository getQuizRepository();

    Optional<Quiz> findById(long quizId);

    Quiz create(Quiz quiz);

    Collection<?> findAll();
}
