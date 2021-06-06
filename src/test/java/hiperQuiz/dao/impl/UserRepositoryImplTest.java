package hiperQuiz.dao.impl;

import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.model.Quiz;
import hiperQuiz.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {

    private static final List<User> USERS = List.of(
           new User("elena"),
           new User("ivan")
    );
    private UserRepository userRepository;
    private LongKeyGenerator keyGenerator;



    @BeforeEach
    public void setUp(){
        keyGenerator=new LongKeyGenerator();
        userRepository=new UserRepositoryImpl(keyGenerator);
    }


    @Test
    void findByUsernameReturnsOptionalUserIfItExist() {
        fillinUsers();
        assertTrue(userRepository.findByUsername("elena").isPresent());
        Optional<User> user = userRepository.findByUsername("no existing");
        assertTrue(userRepository.findByUsername("no existing").isEmpty());

    }



    private void fillinUsers() {
        USERS.forEach((u) ->
                userRepository.create(u));
    }


}