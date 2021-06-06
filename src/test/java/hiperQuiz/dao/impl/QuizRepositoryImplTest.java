package hiperQuiz.dao.impl;

import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.impl.LongKeyGenerator;
import hiperQuiz.dao.impl.QuizRepositoryImpl;
import hiperQuiz.model.Quiz;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuizRepositoryImplTest  {


        private static final List<Quiz> QUIZZES = List.of(
        new Quiz("testQuiz", "One and Only", 60),
        new Quiz("testQuiz2", "testing",60),
                new Quiz("testQuiz3", "testing",80)
);
        private QuizRepository qr;
        private LongKeyGenerator keyGenerator;

        @BeforeEach
        public void setUp(){
            keyGenerator=new LongKeyGenerator();
            qr=new QuizRepositoryImpl(keyGenerator);
        }

        private void fillInQuizzes() {
            QUIZZES.forEach((u) ->
                qr.create(u));
        }


        @Test
        void findsQuizezByExpectedDurationWhenTheyArePresent() {
            fillInQuizzes();
            List<Quiz> quizzes = qr.findByDuration(60);
            assertEquals(2, quizzes.size());
        }

    @Test
    void findsQuizezByExpectedDurationReturnsAnEmptyListWhenNoPresent() {
        fillInQuizzes();
        List<Quiz> quizzes = qr.findByDuration(100);
        Assertions.assertTrue(quizzes.isEmpty());
    }


    @Test
    void findByIsReturningASetOfTestsByTitleDescriptionOrTags() {
            fillInQuizzes();
         Set<Quiz> testSet = qr.findBy("test");
         // todo check if the method should search by Title or contains the title
         assertEquals(2, testSet.size());
        Set<Quiz> test2 = qr.findBy("One and Only");
        assertEquals(1, test2.size());
        Set<Quiz> empty = qr.findBy("no critiria");
        assertTrue(empty.isEmpty());
    }
}
