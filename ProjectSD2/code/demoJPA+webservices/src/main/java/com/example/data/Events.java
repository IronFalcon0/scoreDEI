package com.example.data;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@Entity
@XmlRootElement
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String content;
    private Date eventTime;
    @OneToMany(mappedBy = "events") // one to many
    private Game game;
    @OneToMany(mappedBy = "events") // one to many
    private GameStateOptions gameOp;

    public Events() {
    }

    public Events(String content, Date eventTime) {
        this.content = content;
        this.eventTime = eventTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String toString() {
        return " (id = " + this.id + "). Content: " + this.content + " Time: " + this.eventTime; // ver como fica
    }

}