package com.example.demo;    

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.data.Student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service    
public class StudentService   
{    
    @Autowired    
    private StudentRepository studentRepository;

    public List<Student> getAllStudents()  
    {    
        List<Student>userRecords = new ArrayList<>();    
        studentRepository.findAll().forEach(userRecords::add);    
        return userRecords;    
    }

    public void addStudent(Student student)  
    {    
        studentRepository.save(student);    
    }

    public Optional<Student> getStudent(int id) {
        return studentRepository.findById(id);
    }


    public List<Student> findByNameEndsWith(String chars) {
        return studentRepository.findByNameEndsWith(chars);
    }

}    