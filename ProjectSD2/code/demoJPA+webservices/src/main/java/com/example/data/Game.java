package com.example.data;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
public class Game {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String place;
    private Date date;
    @OneToMany(mappedBy="teamID")
    private int idTeam1;
    @OneToMany(mappedBy="teamID")
    private int idTeam2;
    private int goalsTeam1, goalsTeam2;

    public Game() {}

    public Game(String place, Date date, int idTeam1, int idTeam2) {
        this.place = place;
        this.date = date;
        this.idTeam1 = idTeam1;
        this.idTeam2 = idTeam2;
        goalsTeam1 = 0;
        goalsTeam2 = 0;
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

    public int getIdTeam1() {
        return this.idTeam1;
    }
    
    public void setIdTeam1(int idTeam1) {
        this.idTeam1 = idTeam1;
    }

    public int getIdTeam2() {
        return this.idTeam2;
    }
    
    public void setIdTeam2(int idTeam2) {
        this.idTeam2 = idTeam2;
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

    public String toString() {
        return "todo";
    }

}
