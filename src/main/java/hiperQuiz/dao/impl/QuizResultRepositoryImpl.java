package hiperQuiz.dao.impl;



import hiperQuiz.dao.KeyGenerator;
import hiperQuiz.dao.QuizResultRepository;
import hiperQuiz.model.QuizResult;

public class QuizResultRepositoryImpl extends RepositoryMemoryImpl<Long, QuizResult>
        implements QuizResultRepository {


    public QuizResultRepositoryImpl(KeyGenerator<Long> keyGenerator) {
        super(keyGenerator);
    }
}
