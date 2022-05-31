package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.Game;

public interface GameRepository extends CrudRepository<Game, Integer> {
    //@Query("select s from Student s where s.name like %?1")
    //public List<Game> findByNameEndsWith(String chars);
} 
