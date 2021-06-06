package hiperQuiz.service.impl;

import hiperQuiz.dao.UserRepository;
import hiperQuiz.dao.impl.LongKeyGenerator;
import hiperQuiz.dao.impl.UserRepositoryImpl;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.User;
import hiperQuiz.service.UserService;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Override
    public User findByUserName(String userName) throws EntityNotFoundException {
        if(userRepository.findByUsername(userName).isEmpty()){
            throw new EntityNotFoundException();
        }
        return userRepository.findByUsername(userName).get();
    }

    @Override
    public void updatePassword(User user, String password) throws EntityNotFoundException {
        user.setPassword(password);
        this.userRepository.update(user);
    }
}
