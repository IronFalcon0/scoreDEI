package com.example.data;

import java.util.Date;
import java.util.List;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String place;
    private Date date;
    @ManyToMany(mappedBy="games")
    private List<Team> teams; 
    private int goalsTeam1, goalsTeam2;
    private String gameState;
    private Team winnerTeam, loserTeam;
    private Boolean isTie;

    public Game() {}

    public Game(String place, Date date) {
        this.place = place;
        this.date = date;
        //this.teams = Arrays.asList(team1, team2);
        goalsTeam1 = 0;
        goalsTeam2 = 0;
        this.gameState = new String();

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

    public Team getWinnerTeam() {
        return this.winnerTeam;
    }

    public void setWinnerTeam(Team team) {
        this.winnerTeam = team;
    }

    public Team getLoserTeam() {
        return this.loserTeam;
    }

    public void setLoserTeam(Team team) {
        this.loserTeam = team;
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

}
