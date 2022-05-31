package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
    @Query("select ev from Event ev where ev.game.id = ?1")
    public List<Event> listEventsOfGame(int gameId);
}
