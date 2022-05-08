package com.example.data;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@XmlRootElement
public class Card extends Events {

    public Card() {
    }

    public Card(String content, Date eventTime) {
        this.setContent(content);
        this.setEventTime(eventTime);
    }

    public String toString() {
        return " (id = " + this.getId() + "). Content: " + this.getContent() + " Time: " + this.getEventTime();
    }

}