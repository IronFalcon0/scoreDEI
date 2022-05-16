package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.example.data.Team;


public interface TeamRepository extends CrudRepository<Team, Integer> {
    //@Query("select s from Student s where s.name like %?1")
    public List<Team> findByNameEndsWith(String chars);

    /*@Query(value = "select count(teamIdWinner) from Game g where g.status like 'over' and g.teamIdWinner = ?1", nativeQuery = true)
    public int countWinsOfTeam(int teamId);*/

    @Query("SELECT t FROM Team t ORDER BY numberWins + numberDraws +numberLoses DESC")
    public List<Team> listGamesOrdered();

    @Query("SELECT t FROM Team t ORDER BY numberWins DESC")
    public List<Team> listWinsOrdered();

    @Query("SELECT t FROM Team t ORDER BY numberDraws DESC")
    public List<Team> listDrawsOrdered();
    
    @Query("SELECT t FROM Team t ORDER BY numberLoses DESC")
    public List<Team> listLosesOrdered();
}    