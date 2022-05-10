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
   //@ManyToOne()
    //private Player player;

    /*
     * @ManyToOne(mappedBy = "events")
     * private Team team;
     * 
     * @ManyToOne(mappedBy = "events")
     * private Game game;
     */
    public Event() {
    }

    public Event(String content, String telephone, Date eventTime, Team team, Game game) {
        this.content = content;
        this.eventTime = eventTime;
        //this.player = player;
        /*
         * this.team = team;
         * this.game = game;
         */
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

    /*public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }*/

    /*
     * public Team getTeam() {
     * return team;
     * }
     * 
     * public void setTeam(Team team) {
     * this.team = team;
     * }
     * 
     * public Game getGame() {
     * return game;
     * }
     * 
     * public void setGame(Game game) {
     * this.game = game;
     * }
     */
    public String toString() {
        return this.content + "(id = " + this.id + "). Content: " + this.content + ". EventTime: " + this.eventTime;
    }
}
