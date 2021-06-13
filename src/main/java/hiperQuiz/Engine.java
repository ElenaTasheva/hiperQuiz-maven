package hiperQuiz;

import hiperQuiz.commands.*;
import hiperQuiz.dao.PlayerRepository;
import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.QuizResultRepository;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.dao.impl.*;
import hiperQuiz.model.*;
import hiperQuiz.model.enums.Gender;
import hiperQuiz.service.QuizService;
import hiperQuiz.service.UserService;
import hiperQuiz.service.impl.QuizServiceImpl;
import hiperQuiz.service.impl.UserServiceImpl;
import hiperQuiz.util.PrintUtil;
import hiperQuiz.util.ValidationUtil;
import lombok.SneakyThrows;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static hiperQuiz.commands.CommandMenu.*;
import static hiperQuiz.util.Alignment.*;


public class Engine implements Runnable {

    private static final Map<String, CommandMenu> commands = new HashMap<>();
    private final Scanner scanner;
    private QuizService quizService;
    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;
    private final PlayerRepository playerRepository;
    private User user;
    private final UserService userService;
    private Player player = null;
    private final QuizRepository quizRepository;
    private final PrintingCommand printingCommand;
    private final ReadingCommand readingCommand;


    public Engine() {
        this.printingCommand = new PrintingCommand();
        scanner = new Scanner(System.in);
        this.readingCommand = new ReadingCommand(scanner);
        this.userRepository = new UserRepositoryImpl(new LongKeyGenerator());
        this.quizResultRepository = new QuizResultRepositoryImpl(new LongKeyGenerator());
        this.playerRepository = new PlayerRepositoryImpl(new LongKeyGenerator());
        this.quizRepository = new QuizRepositoryImpl(new LongKeyGenerator());
        this.userService = new UserServiceImpl(userRepository, printingCommand, readingCommand);
        this.quizService = new QuizServiceImpl(quizRepository, printingCommand, readingCommand, quizResultRepository);
        user = new User();
        //  fillInCommands();


    }


    @SneakyThrows
    @Override
    public void run() {

        String propsParh = "C:\\Users\\elena.tasheva\\IdeaProjects\\hiperQuiz-maven\\src\\main\\resources\\db.properties";
        Properties props = new Properties();
        props.load(new FileInputStream(propsParh));
        List<User> users = new ArrayList<>();


        // 2. Load DB driver
//        try {
//            Class.forName(props.getProperty("driver"));
//            System.out.println("DB driver loaded");
//        } catch (ClassNotFoundException e) {
//            System.out.println("Error: DB driver not found");
//            e.printStackTrace();
//            return;
//        }

        // 3. Connect to DB
//        try (Connection connection = DriverManager.getConnection(props.getProperty("url"), props)) {
//            System.out.printf("Successfully connected to: %s%n", props.getProperty("url"));
//            // 4. Create and execute PreparedStatement
//            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users");
//            ResultSet rs = stmt.executeQuery();
//
//            while(rs.next()){
//                StringBuilder sb = new StringBuilder();
//                sb.append(rs.getInt("id")).append(", ").append(rs.getString("username"))
//                        .append(",").append(rs.getString("email")).append(", ")
//                        .append(rs.getBoolean("status"));
//                // TODO: 6/9/2021 fix data
//                Date created = rs.getDate("created");
//                sb.append(", ").append(created);
//                Gender gender1 = Gender.valueOf(rs.getString("gender"));
//                sb.append(", ").append(gender1);
//                System.out.println(sb);
//                User user = new User();
//                user.setId(rs.getLong("id"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setEmail(rs.getString("email"));
//                user.setGender(gender1);
//               // user.setCreated(created);
//                users.add(user);
        }

//        }
//        catch (SQLException throwable) {
//            throwable.printStackTrace();
//        }
//
//        for (User user1 : users) {
//            System.out.println(user1);
//        }
//
//
////        loadData();
////
////        printMenu();
//
//        String command = scanner.nextLine();
//
//
//
//        while (true) {
//            CommandMenu commandMenu = commands.get(command);
//            while (commandMenu == null) {
//                System.out.println("Invalid command. Please try again");
//                command = scanner.nextLine();
//                commandMenu = commands.get(command);
//            }
//                switch (commandMenu){
//                    case EXIT:
//                    try {
//                        SaveEntitiesCommand saveCommand = new SaveEntitiesCommand(new FileOutputStream("quizzes.db"),
//                                playerRepository, userRepository, quizService.getQuizRepository(), quizResultRepository);
//                        System.out.println(saveCommand.execute());
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println("Thank you! BYE :))");
//                    System.exit(0);
//                    break;
//
//                    case CREATE_QUIZ:
//                        user = userService.logIn(user);
//                        settingPlayer();
//                        Quiz quiz = quizService.createQuiz(user);
//                        user.getQuizzes().add(quiz);
//                        break;
//
//                    case CREATE_USER:
//                    this.userService.createUser();
//                    player = new Player(user.getUsername());
//                    playerRepository.create(player);
//                    settingPlayer();
//                    // change the menu;
//                    break;
//
//                    case LOAD_QUIZEZ:
//                        printQuizez();
//
//                    case PLAY_QUIZ:
//                    printQuizez();
//                    // todo optimize method
//                    userService.logIn(user);
//                    settingPlayer();
//                    player = quizService.play(player);
//
//
//                    break;
//
//                    case LOAD_USERS:
//                    printUsers();
//
//                    break;
//               // case 5:
////                   // userLogin();
////                    System.out.println("Enter a new password");
////                    String password = scanner.nextLine();
////                    try {
////                        this.userService.updatePassword(user, password);
////                        System.out.println("Your password has been change successfully");
////                    } catch (EntityNotFoundException e) {
////                        System.out.println(e.getMessage());
////                    }
////                    break;
//            }
//            printMenu();
//            command = scanner.nextLine();
//        }
//    }
//
//
//
//    private void loadData()  {
//
//        quizRepository.drop();
//        quizResultRepository.drop();
//        userRepository.drop();
//        playerRepository.drop();
//
//        LoadEntitiesCommand loadCommand = null;
//        try {
//           loadCommand = new LoadEntitiesCommand(new FileInputStream("quizzes.db"),
//                    playerRepository, userRepository, quizRepository, quizResultRepository);
//        }
//        catch (FileNotFoundException ex){
//            ex.printStackTrace();
//        }
//        System.out.println(loadCommand.execute());
//
//    }
//
//    private void printUsers() {
//
//        List<PrintUtil.ColumnDescriptor> userColumns = new ArrayList<>(List.of(
//                new PrintUtil.ColumnDescriptor("id", "ID", 5, CENTER),
//                new PrintUtil.ColumnDescriptor("username", "Username", 10, LEFT),
//                new PrintUtil.ColumnDescriptor("overallScore", "overallScore", 12, LEFT),
//                new PrintUtil.ColumnDescriptor("rank", "rank", 20, LEFT)
//
//        ));
//
//
//        String userReport = PrintUtil.formatTable(userColumns, playerRepository.findAll().stream().sorted(Comparator.comparing(Player::getOverallScore).reversed()).collect(Collectors.toList()), "STATISTIC");
//        System.out.println(userReport);
//    }
//
//
//
//
//    private void settingPlayer() {
//        Optional<Player> player = this.playerRepository.findAll().stream()
//                .filter(player1 -> player1.getUsername().equals(user.getUsername())).findFirst();
//        // user is found ==> player is found as well as we commit them together in the repos;
//        this.player = player.get();
//    }
//
//
//
//
//    private void printMenu() {
//        System.out.println("---MENU---\n -Please enter a number-\n"+
//                "~0~ Exit\n" +
//                "~1~ Create Quiz\n" +
//                "~2~ Create User\n"+
//                "~3~ Pick a Quiz\n"+ // printing quizzez
//                "~4~ Print Users\n"+
//                "~5~ Edit Profile");
//    }
//
//
//    private void printQuizez() {
//
//        List<PrintUtil.ColumnDescriptor> metadataColumns = getMetaData();
//;
//        List<PrintUtil.ColumnDescriptor> quizColumns = new ArrayList<>(List.of(
//                new PrintUtil.ColumnDescriptor("id", "ID", 5, CENTER),
//                new PrintUtil.ColumnDescriptor("title", "Title", 15, LEFT),
//                new PrintUtil.ColumnDescriptor("author", "Author", 12, LEFT),
//                new PrintUtil.ColumnDescriptor("description", "Description", 20, LEFT),
//                new PrintUtil.ColumnDescriptor("expectedDuration", "Duration", 8, RIGHT, 2),
//                        //   new PrintUtil.ColumnDescriptor("text", "Question", 8, RIGHT, 2),
//                new PrintUtil.ColumnDescriptor("URL", "Picture URL", 11, CENTER)
//        ));
//
//                quizColumns.addAll(metadataColumns);
//
//        String quizReport = PrintUtil.formatTable(quizColumns, quizService.findAll(), "Quiz List:");
//        System.out.println(quizReport);
//
//    }
//
//    private List<PrintUtil.ColumnDescriptor> getMetaData() {
//        return  List.of(
//                new PrintUtil.ColumnDescriptor("created", "Created", 19, CENTER),
//                new PrintUtil.ColumnDescriptor("updated", "Updated", 19, CENTER)
//        );
//    }
//
//
//    private void fillInCommands() {
//        commands.put("0", EXIT);
//        commands.put("1", CREATE_QUIZ);
//        commands.put("2", CREATE_USER);
//        commands.put("3", LOAD_QUIZEZ);
//        commands.put("4", LOAD_USERS);
//        commands.put("5", EDIT_USER);
//    }


    }





