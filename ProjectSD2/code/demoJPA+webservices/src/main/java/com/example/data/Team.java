package com.example.data;

import java.util.List;
import java.util.ArrayList;
//import java.awt.Image;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
    private byte[] image;
    @ManyToMany(mappedBy="teams", cascade = CascadeType.ALL)
    private List<Game> games;
    private int numberWins;
    private int numberLoses;
    private int numberDraws;

    @OneToMany(mappedBy="teamPlayer", cascade = CascadeType.ALL)
    private List<Player> players;

    @OneToMany(mappedBy="teamEvent", cascade = CascadeType.ALL)
    private List<Event> events;

    public Team() {
        this.name = "";
        this.games = new ArrayList<>();
        this.players = new ArrayList<>();
        this.events = new ArrayList<>();
        this.numberWins = 0;
        this.numberLoses = 0;
        this.numberDraws = 0;
    }

    public Team(String name) {
        this.name = name;
        this.games = new ArrayList<>();
        this.players = new ArrayList<>();
        this.events = new ArrayList<>();
        this.numberWins = 0;
        this.numberLoses = 0;
        this.numberDraws = 0;
    }

    public Team(String name, byte[] image) {
        this.name = name;
        this.image = image;
        this.games = new ArrayList<>();
        this.players = new ArrayList<>();
        this.events = new ArrayList<>();
        this.numberWins = 0;
        this.numberLoses = 0;
        this.numberDraws = 0;
    }

    public void appendEvent(Event event) {
        this.events.add(event);
    }

    public int getNumberWins() {
        return numberWins;
    }

    public void setNumberWins(int number) {
        this.numberWins = number;
    }

    public int getNumberLoses() {
        return numberLoses;
    }

    public void setNumberLoses(int number) {
        this.numberLoses = number;
    }

    public int getNumberDraws() {
        return this.numberDraws;
    }

    public void setNumberDraws(int number) {
        this.numberDraws = number;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int numberGames() {
        return this.numberWins + this.numberDraws + this.numberLoses;
    }

    public void addGame(Game game) {
        this.games.add(game);
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public String toString() {
        return this.name + " id = " + this.id;
    }
    
}
