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

@Entity
@XmlRootElement
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String content;
    private Date eventTime;

    @ManyToOne()
    private Game game;

    @ManyToOne()
    private Team teamEvent;

    @ManyToOne()
    private Player playerEvent;


    public Event() {
    }

    public Event(String content, String telephone, Date eventTime) {
        this.content = content;
        this.eventTime = eventTime;
        this.game = new Game();
        this.teamEvent = new Team();
        this.playerEvent = new Player();
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

    public Team getTeam() {
        return teamEvent;
    }

    public Player getPlayer() {
        return playerEvent;
    }


    public String toString() {
        return this.content + "(id = " + this.id + "). Content: " + this.content + ". EventTime: " + this.eventTime;
    }
}