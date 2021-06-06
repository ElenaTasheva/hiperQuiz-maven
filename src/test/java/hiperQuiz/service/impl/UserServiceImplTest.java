package hiperQuiz.service.impl;

import hiperQuiz.dao.UserRepository;
import hiperQuiz.dao.impl.LongKeyGenerator;
import hiperQuiz.dao.impl.UserRepositoryImpl;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.User;
import hiperQuiz.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private  UserRepository userRepository;
    private  LongKeyGenerator keyGenerator;
    private  UserService userService;
    private  User user;

    @BeforeEach
    public void setUp() {
        keyGenerator=new LongKeyGenerator();
        userRepository=new UserRepositoryImpl(keyGenerator);
        userService = new UserServiceImpl(userRepository);
        user = new User("TestUser");
    }


    public void fillInData() {
        userRepository.create(user);

    }

    @Test
    void findByUserNameFindsAUserIfItsPresent() throws EntityNotFoundException {
        System.out.println();
        fillInData();
        assertEquals(user, userService.findByUserName("TestUser"));

    }

    @Test
    void findByUserNameThrowsExceptionIfUserisNotPresent() throws EntityNotFoundException {
        fillInData();
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.findByUserName("does not exist");
        });
    }

    @Test
    void updatePasswordUpdatesPasswordWhenUserIsPresent() throws EntityNotFoundException {
        fillInData();
        assertEquals(user.getPassword(), userService.findByUserName("TestUser").getPassword());
        userService.updatePassword(user, "changes");
        assertEquals("changes", userService.findByUserName("TestUser").getPassword());

    }


}