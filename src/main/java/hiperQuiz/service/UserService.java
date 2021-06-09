package hiperQuiz.service;

import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.User;

public interface UserService {

    User findByUserName(String userName) throws EntityNotFoundException;

    void updatePassword(User user, String password) throws EntityNotFoundException;

    User logIn(User user);

    User createUser();
}
