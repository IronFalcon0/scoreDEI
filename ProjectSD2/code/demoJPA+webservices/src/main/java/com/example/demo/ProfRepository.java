package com.example.demo;

import org.springframework.data.repository.CrudRepository;

import com.example.data.Professor;

public interface ProfRepository extends CrudRepository<Professor, Integer>   
{ } 