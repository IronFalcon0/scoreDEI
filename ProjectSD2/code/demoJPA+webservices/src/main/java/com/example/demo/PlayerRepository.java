package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {
    @Query("SELECT p FROM Player p WHERE goalsScored = (SELECT MAX(p.goalsScored) FROM Player) GROUP BY p.teamPlayer.id")
    public List<Player> listBestPlayers();
}
