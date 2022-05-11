package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    //@Query("select s from Student s where s.name like %?1")
    //public List<User> findByNameEndsWith(String chars);
} 
