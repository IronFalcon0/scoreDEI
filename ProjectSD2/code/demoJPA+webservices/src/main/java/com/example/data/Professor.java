package com.example.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@JsonIgnoreProperties({ "students" })
@XmlRootElement
public class Professor {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name, office;
    @ManyToMany(mappedBy="profs")
    private List<Student> st;

    public Professor() {}

    public Professor(String name, String office) {
        this.name = name;
        this.office = office;
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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name + " (id = " + this.id + "). Office: " + this.office;
    }

    public List<Student> getStudents() {
        return this.st;
    }
}
