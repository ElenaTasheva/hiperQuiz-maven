package hiperQuiz.service.impl;

import hiperQuiz.commands.PrintingCommand;
import hiperQuiz.commands.ReadingCommand;
import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.QuizResultRepository;
import hiperQuiz.model.*;
import hiperQuiz.service.QuizService;
import hiperQuiz.util.ValidationUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuizServiceImpl implements QuizService {

    private  QuizRepository quizRepository;
    private final PrintingCommand printingCommand;
    private final ReadingCommand readingCommand;
    private final QuizResultRepository quizResultRepository;

    public QuizServiceImpl(QuizRepository quizRepository, PrintingCommand printingCommand,
                           ReadingCommand readingCommand, QuizResultRepository quizResultRepository) {
        this.quizRepository = quizRepository;
        this.printingCommand = printingCommand;
        this.readingCommand = readingCommand;
        this.quizResultRepository = quizResultRepository;
    }



    public QuizRepository getQuizRepository() {
        return quizRepository;
    }

    @Override
    public Optional<Quiz> findById(long quizId) {
        return quizRepository.findById(quizId);
    }

    @Override
    public Quiz create(Quiz quiz) {
        quizRepository.create(quiz);
        return quiz;
    }

    @Override
    public Collection<?> findAll() {
        return quizRepository.findAll();
    }


    @Override
    public Quiz createQuiz(User user) {
        printingCommand.print("Please enter a valid quiz title (Title must be between 2 and 80 characters");
        Quiz quiz;
        String title = readingCommand.readLIne();
        while (!ValidationUtil.validateString(title, 2, 80)) {
            printingCommand.print("Please try again");
            title = readingCommand.readLIne();
        }
        // changing the requirements for the demo (20 and 250 constrains)
        printingCommand.print("Please enter a valid quiz description (Title must be 2 and 80  characters");
        String description = readingCommand.readLIne();
        while (!ValidationUtil.validateString(description, 2, 80)) {
            printingCommand.print("Please try again");
            description = readingCommand.readLIne();
        }

        quiz = new Quiz(title, description);
        quiz.setAuthor(user);
        printingCommand.print("Please start entering questions.\n If you want to exit type '5'. \n " +
                "Once ready type 'done'");
        String input = readingCommand.readLIne();
        // todo incl 5 as a general exit in the while
        while (!input.equals("5") && !input.equals("done")) {
            while (!ValidationUtil.validateString(input, 10, 30)) {
                printingCommand.print("Questions minimum length is 10, max 30. Try again");
                input = readingCommand.readLIne();
            }
            Question question = new Question(input);
            printingCommand.print("Please enter a valid answer for your question");
            String answer = readingCommand.readLIne();
            while (!ValidationUtil.validateString(answer, 2, 150)) {
                printingCommand.print("Answer minimum length is 2, max 150. Try again");
                answer = readingCommand.readLIne();
            }
            Answer answer1 = new Answer(answer);
            printingCommand.print("Please enter how many points does the answer give.");
            String points = readingCommand.readLIne();
            while (!ValidationUtil.validateInt(points)) {
                printingCommand.print("Invalid number. Try again");
                points = readingCommand.readLIne();
            }
            answer1.setScore(Integer.parseInt(points));
            // todo check if we want uni or bi directional
            answer1.setQuestion(question);
            // todo make a separate methods
            question.getAnswers().add(answer1);
            quiz.getQuestions().add(question);
            printingCommand.print("Please enter a question");
            input = readingCommand.readLIne();
        }
        printingCommand.print("Your quiz was created");
        quiz.setExpectedDuration(60);
        quizRepository.create(quiz);
        return quiz;
    }

    @Override
    public Player play(Player player) {
        printingCommand.print("Please enter the id of the quiz you want to play");
        long quizId = Long.parseLong(readingCommand.readLIne());
        while (quizRepository.findById(quizId).isEmpty()) {
            printingCommand.print("Please enter a valid id");
            quizId = Long.parseLong(readingCommand.readLIne());
        }

        // todo make a Player and a User;
        Quiz quizToPlay =quizRepository.findById(quizId).get();
        printingCommand.print("Starting the quiz....\n If you want to exit enter '0'");
        String userInput = "";
        List<Question> questions = quizToPlay.getQuestions();
        QuizResult quizResult = new QuizResult(player, quizToPlay);
        quizResultRepository.create(quizResult);

        player.getResults().add(quizResult);


        for (Question question : questions) {
            System.out.println(question);

            userInput = readingCommand.readLIne();
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
        viewQuiz(quizToPlay);

        return player;
    }

    private void viewQuiz(Quiz quizToPlay) {

        printingCommand.print("Do you want to view the quiz?\n YES) 1 NO) 2");
        String answer = readingCommand.readLIne();

        if (answer.equals("1")) {
            printQuiz(quizToPlay);
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
    }

