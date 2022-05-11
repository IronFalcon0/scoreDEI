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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.aspectj.weaver.IntMap;

@Entity
@XmlRootElement
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int goalsMarked, yellowCards, redCards;
    private String name, playerPosition;
    private Date birthDate;
    
    @ManyToOne()
    private Team teamPlayer;

    @OneToMany(mappedBy="playerEvent", cascade = CascadeType.ALL)
    private List<Event> events;
     

    public Player() {
    }

    public Player(String name, String playerPosition, Date birthDate, int goalsMarked, int yellowCards,
            int redCards) {
        this.name = name;
        this.playerPosition = playerPosition;
        this.birthDate = birthDate;
        this.goalsMarked = goalsMarked;
        this.yellowCards = yellowCards;
        this.redCards = redCards;
        this.teamPlayer = new Team();
        this.events = new ArrayList<>();
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

    public int getGoalsMarked() {
        return goalsMarked;
    }

    public void setGoalsMarked(int goalsMarked) {
        this.goalsMarked = goalsMarked;
    }

    public int getYellowCard() {
        return yellowCards;
    }

    public void setYellowCard(int yellowCards) {
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

}