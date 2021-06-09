package hiperQuiz.service.impl;

import hiperQuiz.commands.PrintingCommand;
import hiperQuiz.commands.ReadingCommand;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.Player;
import hiperQuiz.model.User;
import hiperQuiz.model.enums.Gender;
import hiperQuiz.service.UserService;
import hiperQuiz.util.ValidationUtil;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PrintingCommand printingCommand;
    private final ReadingCommand readingCommand;

    public UserServiceImpl(UserRepository userRepository, PrintingCommand printingCommand1, ReadingCommand readingCommand) {
        this.userRepository = userRepository;
        this.printingCommand = printingCommand1;
        this.readingCommand = readingCommand;
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

    @Override
    public User logIn(User user) {
            String username = "";
            // todo check why user does not return true when == to null
            if (user.getUsername() == null) {
                printingCommand.print("Please enter your username or press 1 to register");
                username = readingCommand.readLIne();
                if(username.equals("1")){
                    user = createUser();
                }
                user.setUsername(username);
            }
            try {
                user = this.findByUserName(user.getUsername());
            } catch (EntityNotFoundException e) {
                printingCommand.print(e.getMessage());
                user = createUser();
            }
            return user;
        }


    @Override
    public User createUser() {
        printingCommand.print("---SIGN UP---");
        printingCommand.print("Please enter a valid username: (Username must be between 2 and 15 characters long.");
        String username = readingCommand.readLIne();
        while (!ValidationUtil.validateString(username, 2, 15)) {
            printingCommand.print("The username you enter is not valid. Please try again");
            username = readingCommand.readLIne();
        }
        printingCommand.print("Please enter a valid email address: ");
        String email = readingCommand.readLIne();
        // we can throw exceptions
        while (!ValidationUtil.validateEmail(email)) {
            printingCommand.print("Invalid email. Please try again.");
            email = readingCommand.readLIne();
        }
        printingCommand.print("Please enter password. (Must be between 3 and 15 characters.");
        String password = readingCommand.readLIne();
        while (!ValidationUtil.validateString(password, 3, 15)) {
            printingCommand.print("Invalid password.PLease try again");
            password = readingCommand.readLIne();
        }
        User user = new User(username, email, password);
        // todo make a menu with options
        printingCommand.print("Please enter your gender: (type male or female)");
        Gender gender = ValidationUtil.validateGender(readingCommand.readLIne());
        while (gender == null) {
            printingCommand.print("Invalid data. Please try again");
            gender = ValidationUtil.validateGender(readingCommand.readLIne());
        }
        user.setGender(gender);
        userRepository.create(user);
        printingCommand.print("~~~Congratulations your profile was created successfully~~~");
        return user;

    }


}

