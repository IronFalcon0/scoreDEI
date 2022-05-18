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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@XmlRootElement
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String content;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date eventTime;

    @ManyToOne(cascade = CascadeType.ALL)
    private Game game;

    @ManyToOne(cascade = CascadeType.ALL)
    private Team teamEvent;

    @ManyToOne(cascade = CascadeType.ALL)
    private Player playerEvent;


    public Event() {
    }

    public Event(String content, Date eventTime) {
        this.content = content;
        this.eventTime = eventTime;
        this.game = new Game();
        this.teamEvent = new Team();
        this.playerEvent = new Player();
    }

    public Event(String content, Date eventTime, Game game) {
        this.content = content;
        this.eventTime = eventTime;
        this.game = game;
        /*this.teamEvent = new Team();
        this.playerEvent = new Player();*/
    }

    public Event(String content, Date eventTime, Game game, Team team) {
        this.content = content;
        this.eventTime = eventTime;
        this.game = game;
        this.teamEvent = team;
        this.playerEvent = new Player();
    }

    public Event(String content, Date eventTime, Game game, Team team, Player player) {
        this.content = content;
        this.eventTime = eventTime;
        this.game = game;
        this.teamEvent = team;
        this.playerEvent = player;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // @XmlElementWrapper(content = "player")
    // @XmlElement(content = "player")

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Team getTeam() {
        return teamEvent;
    }

    public void setTeam(Team team) {
        this.teamEvent = team;
    }

    public Player getPlayer() {
        return playerEvent;
    }

    public void setPlayer(Player player) {
        this.playerEvent = player;
    }

    public String getPlayerName() {
        if (this.playerEvent == null) {
            return "";
        }
        return this.playerEvent.getName();
    }

    public String getTeamName() {
        if (this.teamEvent == null) {
            return "";
        }
        return this.teamEvent.getName();
    }

    public String toString() {
        return this.content + "(id = " + this.id + "). Content: " + this.content + ". EventTime: " + this.eventTime;
    }
}