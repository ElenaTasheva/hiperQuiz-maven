package hiperQuiz.commands;


import hiperQuiz.dao.PlayerRepository;
import hiperQuiz.dao.QuizRepository;
import hiperQuiz.dao.QuizResultRepository;
import hiperQuiz.dao.UserRepository;
import hiperQuiz.exception.EntityAlreadyExistsException;
import hiperQuiz.model.AllCollections;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

@Slf4j
public class LoadEntitiesCommand implements Command {
    private PlayerRepository playerRepository;
    private UserRepository userRepo;
    private QuizRepository quizRepository;
    private QuizResultRepository quizResultRepository;
    private InputStream in;


    public LoadEntitiesCommand(InputStream in,PlayerRepository playerRepository, UserRepository userRepo,
                               QuizRepository quizRepository, QuizResultRepository quizResultRepository) {
        this.playerRepository = playerRepository;
        this.userRepo = userRepo;
        this.quizRepository = quizRepository;
        this.quizResultRepository = quizResultRepository;
        this.in = in;
    }

    @Override
    public String execute() {
        try(ObjectInputStream ois = new ObjectInputStream(in)){
            AllCollections allCollections = (AllCollections) ois.readObject();
            quizRepository.createBatch(allCollections.getQuizzes());
            userRepo.createBatch(allCollections.getUsers());
            quizResultRepository.createBatch(allCollections.getQuizResults());
            playerRepository.createBatch(allCollections.getPlayers());
            return "All collections loaded successfully";
        } catch (IOException | ClassNotFoundException e) {
            log.error("Error reading collections from file", e);
            return "Error reading collections from file";
        } catch (EntityAlreadyExistsException e) {
            log.error("Error adding entities to repository", e);
            return "Error adding entities to repository";

    }
}
}