package hiperQuiz;

import hiperQuiz.commands.LoadEntitiesCommand;
import hiperQuiz.commands.SaveEntitiesCommand;
import hiperQuiz.dao.PlayerRepository;
import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.QuizResultRepository;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.dao.impl.*;
import hiperQuiz.exception.EntityNotFoundException;
import hiperQuiz.model.*;
import hiperQuiz.model.enums.Gender;
import hiperQuiz.service.QuizService;
import hiperQuiz.service.UserService;
import hiperQuiz.service.impl.QuizServiceImpl;
import hiperQuiz.service.impl.UserServiceImpl;
import hiperQuiz.util.PrintUtil;
import hiperQuiz.util.ValidationUtil;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;

import static hiperQuiz.util.Alignment.*;


public class Engine implements Runnable{

    private final Scanner scanner;
    // make it final
    private  QuizService quizService;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final PlayerRepository playerRepository;
    private  User user;
    // todo make it final
    private  UserService userService;
    private Player player;
    private final QuizRepository quizRepository;



    public Engine() {
        scanner = new Scanner(System.in);
        this.userRepository = new UserRepositoryImpl(new LongKeyGenerator());
        this.quizResultRepository = new QuizResultRepositoryImpl(new LongKeyGenerator());
        this.playerRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
        this.quizRepository = new QuizRepositoryImpl(new LongKeyGenerator());

        user = new User();
    }



    @Override
    public void run() {

        loadData();
        userService = new UserServiceImpl(userRepository);
        quizService = new QuizServiceImpl(quizRepository);
        printMenu();

        int exNumber;

        exNumber = Integer.parseInt(scanner.nextLine());


        while (true) {
            switch (exNumber) {
                case 0:
                    try {
                        SaveEntitiesCommand saveCommand = new SaveEntitiesCommand(new FileOutputStream("quizzes.db"),
                                playerRepository, userRepository, quizService.getQuizRepository(), quizResultRepository);
                        System.out.println(saveCommand.execute());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    System.out.println("Thank you! BYE :))");
                    System.exit(0);
                case 1:
                    userLogin();
                    Quiz quiz = createTest(user);
                    quiz = quizService.create(quiz);
                    user.getQuizzes().add(quiz);
                    System.out.println("Your  quiz was created");
                    break;
                case 2:
                    createUser();
                    // change the menu;
                    break;
                case 3:
                    printQuizez();
                    userLogin();
                    System.out.println("Please enter the id of the quiz you want to play");
                    long quizId = Long.parseLong(scanner.nextLine());
                    while (quizService.findById(quizId).isEmpty()) {
                        System.out.println("Please enter a valid id");
                        quizId = Long.parseLong(scanner.nextLine());
                    }

                    // todo make a Player and a User;
                    Quiz quizToPlay = quizService.findById(quizId).get();
                    System.out.println("Starting the quiz....\n If you want to exit enter '0'");
                    String userInput = "";
                    List<Question> questions = quizToPlay.getQuestions();
                    QuizResult quizResult = createAQuizResultForTheChosenQuiz(player, quizToPlay);

                    player.getResults().add(quizResult);


                    for (Question question : questions) {
                        System.out.println(question);

                        userInput = scanner.nextLine();
                        if (userInput.equals("0")) {
                            break;
                        }
                        String answer = question.getAnswers().get(0).getText();
                        Answer answer1 = question.getAnswers().get(0);
                        if (userInput.equals(answer)) {
                            quizResult.calculateScore(answer1);
                            System.out.println("Good job!");
                        } else {
                            System.out.println(":( May be next time");
                        }
                    }

                    player.addQuizResult(quizResult);
                    System.out.printf("Thank you for playing.\n Your score is %d\n", player.getCurrentQuizScore());
                    System.out.println("Do you want to view the quiz?\n YES) 1 NO) 2");
                    String answer = scanner.nextLine();

                    if (answer.equals("1")) {
                        printQuiz(quizToPlay);
                    }

                    break;

                case 4:
                    printUsers();
                    break;
                case 5:
                    userLogin();
                    System.out.println("Enter a new password");
                    String password = scanner.nextLine();
                    try {
                        this.userService.updatePassword(user, password);
                        System.out.println("Your password has been change successfully");
                    } catch (EntityNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
            }
            printMenu();
            exNumber = Integer.parseInt(scanner.nextLine());
        }
    }

    private void printQuiz(Quiz quizToPlay) {


        List<Question> questions = quizToPlay.getQuestions();
        List<Answer> answers = quizToPlay.getQuestions().stream()
                .map(question -> question.getAnswers().get(0)).collect(Collectors.toList());
        String quizCaption = quizToPlay.getTitle();
        System.out.printf("                %s             \n", quizCaption);
        System.out.println("-".repeat(quizCaption.length() + 40));
        for (int i = 0; i < answers.size(); i++) {
            System.out.printf("%s | %s\n", questions.get(i), answers.get(i));
        }
        System.out.println();
    }

    private void loadData()  {

        quizRepository.drop();
        quizResultRepository.drop();
        userRepository.drop();
        playerRepository.drop();

        LoadEntitiesCommand loadCommand = null;
        try {
           loadCommand = new LoadEntitiesCommand(new FileInputStream("quizzes.db"),
                    playerRepository, userRepository, quizRepository, quizResultRepository);
        }
        catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        System.out.println(loadCommand.execute());

    }

    private void printUsers() {

        List<PrintUtil.ColumnDescriptor> userColumns = new ArrayList<>(List.of(
                new PrintUtil.ColumnDescriptor("id", "ID", 5, CENTER),
                new PrintUtil.ColumnDescriptor("username", "Username", 10, LEFT),
                new PrintUtil.ColumnDescriptor("overallScore", "overallScore", 12, LEFT),
                new PrintUtil.ColumnDescriptor("rank", "rank", 20, LEFT)

        ));


        String userReport = PrintUtil.formatTable(userColumns, playerRepository.findAll().stream().sorted(Comparator.comparing(Player::getOverallScore).reversed()).collect(Collectors.toList()), "STATISTIC");
        System.out.println(userReport);
    }

    private void userLogin() {
        String username = "";
        // todo check why user does not return true when == to null
       if (user.getUsername() == null) {
           System.out.println("Please enter your username or press 1 to register");
            username = scanner.nextLine();
            if(username.equals("1")){
                createUser();
            }
            user.setUsername(username);
       }
            try {
                user = this.userService.findByUserName(user.getUsername());
                settingPlayer();
            } catch (EntityNotFoundException e) {
                System.out.println(e.getMessage());
                createUser();
            }
        }


    private void settingPlayer() {
        Optional<Player> player = this.playerRepository.findAll().stream()
                .filter(player1 -> player1.getUsername().equals(user.getUsername())).findFirst();
        // user is found ==> player is found as well as we commit them together in the repos;
        this.player = player.get();
    }

    private void createUser() {
        System.out.println("---SIGN UP---");
        System.out.println("Please enter a valid username: (Username must be between 2 and 15 characters long.");
        String username = scanner.nextLine();
        while (!ValidationUtil.validateString(username, 2, 15)) {
            System.out.println("The username you enter is not valid. Please try again");
            username = scanner.nextLine();
        }
        System.out.println("Please enter a valid email address: ");
        String email = scanner.nextLine();
        // we can throw exceptions
        while (!ValidationUtil.validateEmail(email)) {
            System.out.println("Invalid email. Please try again.");
            email = scanner.nextLine();
        }
        System.out.println("Please enter password. (Must be between 3 and 15 characters.");
        String password = scanner.nextLine();
        while (!ValidationUtil.validateString(password, 3, 15)) {
            System.out.println("Invalid password.PLease try again");
            password = scanner.nextLine();
        }
        User user = new User(username, email, password);
        // todo make a menu with options
        System.out.println("Please enter your gender: (type male or female)");
        Gender gender = ValidationUtil.validateGender(scanner.nextLine());
        while (gender == null) {
            System.out.println("Invalid data. Please try again");
            gender = ValidationUtil.validateGender(scanner.nextLine());
        }
        user.setGender(gender);
        userRepository.create(user);
        Player player = new Player(user.getUsername());
        playerRepository.create(player);
        System.out.println("~~~Congratulations your profile was created successfully~~~");
        this.user = user;
        settingPlayer();
    }


    private void printMenu() {
        System.out.println("---MENU---\n -Please enter a number-\n"+
                "~0~ Exit\n" +
                "~1~ Create Quiz\n" +
                "~2~ Create User\n"+
                "~3~ Pick a Quiz\n"+ // printing quizzez
                "~4~ Print Users\n"+
                "~5~ Edit Profile");
    }

    private QuizResult createAQuizResultForTheChosenQuiz(User userPlaying, Quiz quizToPlay) {
        QuizResult quizResult = new QuizResult((Player) userPlaying, quizToPlay);
        return quizResultRepository.create(quizResult);
    }



    private void printQuizez() {

        List<PrintUtil.ColumnDescriptor> metadataColumns = getMetaData();
;
        List<PrintUtil.ColumnDescriptor> quizColumns = new ArrayList<>(List.of(
                new PrintUtil.ColumnDescriptor("id", "ID", 5, CENTER),
                new PrintUtil.ColumnDescriptor("title", "Title", 15, LEFT),
                new PrintUtil.ColumnDescriptor("author", "Author", 12, LEFT),
                new PrintUtil.ColumnDescriptor("description", "Description", 20, LEFT),
                new PrintUtil.ColumnDescriptor("expectedDuration", "Duration", 8, RIGHT, 2),
                        //   new PrintUtil.ColumnDescriptor("text", "Question", 8, RIGHT, 2),
                new PrintUtil.ColumnDescriptor("URL", "Picture URL", 11, CENTER)
        ));

                quizColumns.addAll(metadataColumns);

        String quizReport = PrintUtil.formatTable(quizColumns, quizService.findAll(), "Quiz List:");
        System.out.println(quizReport);

    }

    private List<PrintUtil.ColumnDescriptor> getMetaData() {
        return  List.of(
                new PrintUtil.ColumnDescriptor("created", "Created", 19, CENTER),
                new PrintUtil.ColumnDescriptor("updated", "Updated", 19, CENTER)
        );
    }

    private User findUserByUserName() {
        System.out.println("Please enter your username");
        String username = scanner.nextLine();

        // todo check and throw exception the demo is assuming data is correct
        return  userRepository.findAll().stream().filter(user1 -> user1.getUsername().equals(username))
                .findFirst().get();
    }



    // add exit number
    private Quiz createTest(User user) {
        System.out.println("Please enter a valid quiz title (Title must be between 2 and 80 characters");
        Quiz quiz;
        String title = scanner.nextLine();
        while(!ValidationUtil.validateString(title, 2, 80)){
            System.out.println("Please try again");
            title = scanner.nextLine();
        }
        // changing the requirements for the demo (20 and 250 constrains)
        System.out.println("Please enter a valid quiz description (Title must be 2 and 80  characters");
        String description = scanner.nextLine();
        while(!ValidationUtil.validateString(description, 2, 80)){
            System.out.println("Please try again");
            description = scanner.nextLine();
        }

        quiz = new Quiz(title, description);
        quiz.setAuthor(user);
        System.out.println("Please start entering questions.\n If you want to exit type '0'. \n " +
                "Once ready type 'done'");
        String input = scanner.nextLine();
        while (!input.equals("5") && !input.equals("done")){
            while (!ValidationUtil.validateString(input, 10, 30)){
                System.out.println("Questions minimum length is 10, max 30. Try again");
                input = scanner.nextLine();
            }
            Question question = new Question(input);
            System.out.println("Please enter a valid answer for your question");
            String answer = scanner.nextLine();
            while (!ValidationUtil.validateString(answer, 2, 150)){
                System.out.println("Answer minimum length is 2, max 150. Try again");
                answer = scanner.nextLine();
            }
            Answer answer1 = new Answer(answer);
            System.out.println("Please enter how many points does the answer give.");
            String points = scanner.nextLine();
            while (!ValidationUtil.validateInt(points)) {
                System.out.println("Invalid number. Try again");
                points = scanner.nextLine();
            }
            answer1.setScore(Integer.parseInt(points));
            // todo check if we want uni or bi directional
            answer1.setQuestion(question);
            // todo make a separate methods
            question.getAnswers().add(answer1);
            quiz.getQuestions().add(question);
            System.out.println("Please enter a question");
            input = scanner.nextLine();

        }
        quiz.setExpectedDuration(60);
        return quiz;
    }


}



