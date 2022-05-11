package com.example.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name = "UserTable")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name, password, email, phone;
    private Boolean admin;

    public User() {}

    public User(String name, String password, String email, String phone, Boolean isAdmin) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        admin = isAdmin;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public String toString() {
        return "user " + this.id + ": (name = " + this.name + ", password = " + this.password + "), email = " + this.email + ", phone = " + this.phone + ", is Admin = " + this.admin;
    }
    
}
