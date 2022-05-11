package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        List<Event> userRecords = new ArrayList<>();
        eventRepository.findAll().forEach(userRecords::add);
        return userRecords;
    }

    public void addEvent(Event event) {
        eventRepository.save(event);
    }

    public Optional<Event> getEvent(int id) {
        return eventRepository.findById(id);
    }

    /*
     * public List<Event> findByNameEndsWith(String chars) {
     * return eventRepository.findByNameEndsWith(chars);
     * }
     */

}
