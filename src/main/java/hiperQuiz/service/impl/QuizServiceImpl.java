package hiperQuiz.service.impl;

import hiperQuiz.dao.QuizRepository;
import hiperQuiz.model.Quiz;
import hiperQuiz.service.QuizService;

import java.util.Collection;
import java.util.Optional;

public class QuizServiceImpl implements QuizService {

    private  QuizRepository quizRepository;

    public QuizServiceImpl(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public QuizRepository getQuizRepository() {
        return quizRepository;
    }

    @Override
    public Optional<Quiz> findById(long quizId) {
        return quizRepository.findById(quizId);
    }

    @Override
    public Quiz create(Quiz quiz) {
        quizRepository.create(quiz);
        return quiz;
    }

    @Override
    public Collection<?> findAll() {
        return quizRepository.findAll();
    }
}
