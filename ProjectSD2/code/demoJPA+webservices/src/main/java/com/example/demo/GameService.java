package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Event;
import com.example.data.Game;
import com.example.data.Team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public List<Game> getAllGames() {
        List<Game> userRecords = new ArrayList<>();
        gameRepository.findAll().forEach(userRecords::add);
        return userRecords;
    }

    public void addGame(Game game) {
        gameRepository.save(game);
    }

    public Optional<Game> getGame(int id) {
        return gameRepository.findById(id);
    }

    public List<Team> getGameTeams(int id) {
        Game game = getGame(id).get();
        if (game == null) {
            //
        }
        return game.getTeams();
    }

    public List<Event> getGameEvents(int id) {
        Game game = getGame(id).get();
        if (game == null) {
            //
        }
        return game.getEvents();
    }

    /*
     * public List<Game> findByNameEndsWith(String chars) {
     * return gameRepository.findByNameEndsWith(chars);
     * }
     */

}
