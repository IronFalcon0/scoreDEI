package com.example.data;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private int image;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Game> games;

    @OneToMany(mappedBy="teamPlayer", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(mappedBy="teamEvent", cascade = CascadeType.ALL)
    private List<Event> events;

    public Team() {}

    public Team(String name, int image) {
        this.name = name;
        this.image = image;
        this.games = new ArrayList<>();
        this.players = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    public List<Game> getGames() {
        return games;
    }
    
    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Player> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Event> getEvents() {
        return events;
    }
    
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String toString() {
        return this.name + " id = " + this.id;
    }
    
}
