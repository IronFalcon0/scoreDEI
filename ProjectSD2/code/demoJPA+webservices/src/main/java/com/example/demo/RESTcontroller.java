package com.example.demo;

import java.util.List;
import java.util.Optional;

import com.example.data.Professor;
import com.example.data.Student;
import com.example.data.Team;
import com.example.data.Game;
import com.example.data.User;
import com.example.data.Event;
import com.example.data.Player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("rest")
public class RESTcontroller {
    @Autowired
    ProfService profService;

    @Autowired
    StudentService studentService;

    @Autowired
    GameService gameService;
    
    @Autowired
    TeamService teamService;

    @Autowired
    UserService userService;
    
    @Autowired
    EventService eventService;

    @Autowired
    PlayerService playerService;

    @GetMapping(value = "professors", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Professor> getProfessors()
    {
        return profService.getAllProfessors();
    }

    @GetMapping(value = "students", produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Student> getStudents()
    {
        return studentService.getAllStudents();
    }

    @GetMapping(value = "students/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public Student getStudent(@PathVariable("id") int id) {
        Optional<Student> op = studentService.getStudent(id);
        if (op.isEmpty())
            return null;
        return op.get();
    }

    @GetMapping(value = "professors/{id}", produces = {MediaType.APPLICATION_XML_VALUE})
    public Professor getProfessor(@PathVariable("id") int id) {
        Optional<Professor> op = profService.getProfessor(id);
        if (op.isEmpty())
            return null;
        return op.get();
    }

    @PostMapping(value = "professors", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addProfessor(@RequestBody Professor p) {
        profService.addProfessor(p);
    }

    @PostMapping(value = "students", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void addStudent(@RequestBody Student s) {
        studentService.addStudent(s);
    }

    @PutMapping(value = "professors/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void addProfessor(@PathVariable("id") int id, @RequestBody Professor p) {
        Optional<Professor> op = profService.getProfessor(id);
        if (!op.isEmpty()) {
            Professor p1 = op.get();
            p1.setName(p.getName());
            p1.setOffice(p.getOffice());
            profService.addProfessor(p1);
        }
    }

    @PutMapping(value = "students/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public void addStudent(@PathVariable("id") int id, @RequestBody Student s) {
        System.out.println("PUT called");
        Optional<Student> op = studentService.getStudent(id);
        if (!op.isEmpty()) {
            Student s1 = op.get();
            s1.setName(s.getName());
            s1.setAge(s.getAge());
            s1.setTelephone(s.getTelephone());
            studentService.addStudent(op.get());
        }
    }
}
