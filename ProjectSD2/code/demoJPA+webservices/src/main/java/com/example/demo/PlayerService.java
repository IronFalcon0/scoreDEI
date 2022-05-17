package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers() {
        List<Player> userRecords = new ArrayList<>();
        playerRepository.findAll().forEach(userRecords::add);
        return userRecords;
    }

    public void addPlayer(Player player) {
        playerRepository.save(player);
    }

    public Optional<Player> getPlayer(int id) {
        return playerRepository.findById(id);
    }

    public List<Player> listBestPlayers() {
        return playerRepository.listBestPlayers();
    }

    /*
     * public List<Player> findByNameEndsWith(String chars) {
     * return playerRepository.findByNameEndsWith(chars);
     * }
     */

}
