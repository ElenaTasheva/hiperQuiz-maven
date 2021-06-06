package hiperQuiz.dao.impl;


import hiperQuiz.dao.KeyGenerator;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.User;

import javax.swing.text.html.Option;
import java.util.Optional;


public class UserRepositoryImpl extends RepositoryMemoryImpl<Long, User> implements
        UserRepository {

        public UserRepositoryImpl(KeyGenerator<Long> keyGenerator) {
                super(keyGenerator);
        }

        @Override
        public Optional<User> findByUsername(String userName) {
               return super.findAll().stream().filter(user -> user.getUsername().equals(userName))
                       .findFirst();

        }


}
