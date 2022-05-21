package com.example.data;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date date;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Team> teams; 
    private int goalsTeam1, goalsTeam2;
    private String gameState;
    private Boolean isOver;
    private Boolean isTie;

    @OneToMany(mappedBy="game", cascade = CascadeType.ALL)
    private List<Event> events;

    public Game() {
        this.teams = new ArrayList<>();
        this.goalsTeam1 = 0;
        this.goalsTeam2 = 0;
        this.gameState = "Game not started";
        this.isOver = false;
    }

    public Game(String place, Date date) {
        this.place = place;
        this.date = date;
        this.teams = new ArrayList<>();
        goalsTeam1 = 0;
        goalsTeam2 = 0;
        this.gameState = new String();
        this.events = new ArrayList<>();
        this.isOver = false;
    }

    public void appendEvent(Event event) {
        this.events.add(event);
    }

    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
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
        return this.date.toString();
    }

    public void setDate(String date) throws ParseException {
        System.out.println(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
        this.date = (Date)formatter.parse(date);
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

    public Boolean getIsOver() {
        return this.isOver;
    }

    public void setIsOver(Boolean isOver) {
        this.isOver = isOver;
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

    public void setTeams(List<Team> team) {;
        this.teams.addAll(team);
    }

    public void set2Teams(Team team1, Team team2) {
        this.teams.clear();
        this.teams.add(team1);
        this.teams.add(team2);

    }


}
