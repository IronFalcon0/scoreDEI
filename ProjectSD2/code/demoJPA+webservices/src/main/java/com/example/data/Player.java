package com.example.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.aspectj.weaver.IntMap;

@Entity
@XmlRootElement
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int goalsScored, yellowCards, redCards;
    private String name, playerPosition;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate;
    
    @ManyToOne()
    private Team teamPlayer;

    @OneToMany(mappedBy="playerEvent", cascade = CascadeType.ALL)
    private List<Event> events;
     

    public Player() {
    }

    public Player(String name, String playerPosition, Date birthDate, Team team) {
        this.name = name;
        this.playerPosition = playerPosition;
        this.birthDate = birthDate;
        this.goalsScored = 0;
        this.yellowCards = 0;
        this.redCards = 0;
        this.teamPlayer = team;
        this.events = new ArrayList<>();
    }

    public Player(String name, String playerPosition, Date birthDate, int goalsScored, int yellowCards,
            int redCards) {
        this.name = name;
        this.playerPosition = playerPosition;
        this.birthDate = birthDate;
        this.goalsScored = goalsScored;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.teamPlayer = new Team();
        this.events = new ArrayList<>();
    }

    public Player(String name, String playerPosition, Date birthDate, int goalsScored, int yellowCards,
            int redCards, Team team) {
        this.name = name;
        this.playerPosition = playerPosition;
        this.birthDate = birthDate;
        this.goalsScored = goalsScored;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.teamPlayer = team;
        this.events = new ArrayList<>();
    }

    public void appendEvent(Event event) {
        this.events.add(event);
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

    public String getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(String playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public void setGoalsScored(int goalsScored) {
        this.goalsScored = goalsScored;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getRedCards() {
        return redCards;
    }

    public void setRedCards(int redCards) {
        this.redCards = redCards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return teamPlayer;
    }

    public String toString() {
        return this.name + " (id = " + this.id + "). PlayerPosition: " + this.playerPosition;
    }

    public String getTeamName() {
        return teamPlayer.getName();
    }

}