package hiperQuiz.dao.impl;


import hiperQuiz.dao.KeyGenerator;
import hiperQuiz.dao.PlayerRepository;
import hiperQuiz.model.Player;


public class PlayerRepositoryImpl extends RepositoryMemoryImpl<Long, Player> implements
        PlayerRepository {

public PlayerRepositoryImpl(KeyGenerator<Long> keyGenerator) {
        super(keyGenerator);
        }
        }
