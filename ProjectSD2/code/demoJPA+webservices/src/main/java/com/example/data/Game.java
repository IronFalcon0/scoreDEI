package com.example.data;

import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@XmlRootElement
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String place;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Team> teams; 
    private int goalsTeam1, goalsTeam2;
    private String gameState;
    private Boolean isTie;
    private String status;

    @OneToMany(mappedBy="game", cascade = CascadeType.ALL)
    private List<Event> events;

    public Game() {}

    public Game(String place, Date date) {
        this.place = place;
        this.date = date;
        this.teams = new ArrayList<>();
        goalsTeam1 = 0;
        goalsTeam2 = 0;
        this.gameState = new String();
        this.events = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlace() {
        return this.place;
    }
    
    public void setPlace(String place) {
        this.place = place;
    }

    public Date getDate() {
        return this.date;
    }

    public String getDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(this.date);
    }
    
    public void setPlace(Date date) {
        this.date = date;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public int getGoalsTeam1() {
        return this.goalsTeam1;
    }
    
    public void setGoalsTeam1(int goalsTeam1) {
        this.goalsTeam1 = goalsTeam1;
    }

    public int getGoalsTeam2() {
        return this.goalsTeam2;
    }
    
    public void setGoalsTeam2(int goalsTeam2) {
        this.goalsTeam2 = goalsTeam2;
    }

    public String getGameState() {
        return this.gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public Boolean getIsTie() {
        return this.isTie;
    }

    public void setIsTie(Boolean isTie) {
        this.isTie = isTie;
    }

    public String toString() {
        return "todo";
    }

    public List<Event> getEvents() {
        return events;
    }
    
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setTeams(Team team1, Team team2) {
        this.teams.clear();
        this.teams.add(team1);
        this.teams.add(team2);

    }


}
