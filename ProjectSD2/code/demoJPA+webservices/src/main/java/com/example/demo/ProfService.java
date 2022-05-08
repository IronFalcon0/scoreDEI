package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import java.util.ArrayList;    
import org.springframework.beans.factory.annotation.Autowired;    
import org.springframework.stereotype.Service;

import com.example.data.Professor;

@Service    
public class ProfService   
{    
    @Autowired    
    private ProfRepository profRepository;

    public List<Professor> getAllProfessors()  
    {    
        List<Professor>userRecords = new ArrayList<>();    
        profRepository.findAll().forEach(userRecords::add);    
        return userRecords;    
    }

    public void addProfessor(Professor prof)  
    {
        System.out.println(prof);
        profRepository.save(prof);    
    }

    public Optional<Professor> getProfessor(int id) {
        return profRepository.findById(id);
    }

    @Transactional
    public void changeProfOffice(int id, String newoffice) {
        Optional<Professor> p = profRepository.findById(id);
        if (!p.isEmpty())
            p.get().setOffice(newoffice);
    }

}    