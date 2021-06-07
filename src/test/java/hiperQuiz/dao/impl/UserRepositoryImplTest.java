package hiperQuiz.dao.impl;

import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.exception.EntityAlreadyExistsException;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.Quiz;
import hiperQuiz.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    @Test
    void findByIdReturnsUserWithTheIdIfItExists(){
        fillinUsers();
        Optional<User> user = userRepository.findById((long)1);
        Optional<User> user2 = userRepository.findById((long)2);
        assertEquals("elena", user.get().getUsername());
        assertEquals("ivan", user2.get().getUsername());

    }

    @Test
    void findByIdReturnsOptionalOfNullWhenUserIsNotFound(){
        fillinUsers();
        Optional<User> user = userRepository.findById((long)8);

        assertTrue(user.isEmpty());


    }

    @Test
    @DisplayName("Find all users")
    void findAll() {
        fillinUsers();
        List<User> result = userRepository.findAll();
        assertNotNull(result, "Products result is null"); // test
        assertEquals(USERS.size(), result.size(), "Products result size");
    }

    @Test
    public void deleteByIdDeletesEntityWithTheIdIfPresent() throws EntityNotFoundException {
        fillinUsers();
        assertEquals(2, userRepository.count());
        User deletedUser = userRepository.deleteById(1L);
        assertEquals(1, userRepository.count());
        assertEquals("elena", deletedUser.getUsername());
    }

    @Test
    public void deleteByIdThrowsExceptionWhenUserIsNotPresent() throws EntityNotFoundException {
        fillinUsers();
        assertThrows(EntityNotFoundException.class, () -> userRepository.deleteById(8L));

    }

    @Test
    public void dropClearsTheDataFromRepo(){
        fillinUsers();
        assertEquals(2, userRepository.count());
        userRepository.drop();
        assertEquals(0, userRepository.count());
    }

    private void fillinUsers() {
        USERS.forEach((u) ->
                userRepository.create(u));
    }

    @Test
    public void createBatchFillInRepoFromCollectionAndReturnsTheNUmberOfCreatedEntities() throws EntityAlreadyExistsException {
        USERS.get(0).setId(1L); // sets id-s to the entities
        USERS.get(1).setId(2L);
        int createdEntities = userRepository.createBatch(USERS);
        assertEquals(2, userRepository.count());
        assertEquals(2, createdEntities);
    }

    @Test
    public void createBatchThrowsExceptionIfEntityAlreadyExist() throws EntityAlreadyExistsException {
        USERS.get(0).setId(1L); // sets id-s to the entities
        userRepository.create(USERS.get(0));
        assertThrows(EntityAlreadyExistsException.class, () -> userRepository.createBatch(USERS));

    }

    @Test
    public void createBatchThrowsExceptionIfEntityIdIsNull() throws EntityAlreadyExistsException {// sets id-s to the entities
        userRepository.create(USERS.get(0));
        assertThrows(EntityAlreadyExistsException.class, () -> userRepository.createBatch(USERS));

    }


}