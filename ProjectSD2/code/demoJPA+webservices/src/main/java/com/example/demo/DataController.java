package com.example.demo;

import java.util.List;
import java.util.Optional;

import com.example.data.Professor;
import com.example.data.Student;
import com.example.data.Team;
import com.example.data.Game;
import com.example.data.User;
import com.example.formdata.FormData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class DataController {
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

    @GetMapping("/")
    public String redirect() {
        return "redirect:/listStudents";
    }

    @GetMapping("/createData")
    public String createData() {
        return "createData";
    }

	@PostMapping("/saveData")
	public String saveData(Model model) {
        Professor[] myprofs = { 
            new Professor("José", "D3.1"), 
            new Professor("Paulo", "135"), 
            new Professor("Estrela", "180")
        };
        Student[] mystudents = { 
            new Student("Paula", "91999991", 21),
            new Student("Artur", "91999992", 21),
            new Student("Rui", "91999993", 19),
            new Student("Luísa", "91999994", 20),
            new Student("Alexandra", "91999995", 21),
            new Student("Carlos", "91999995", 22)
        };

        mystudents[0].addProf(myprofs[0]);
        mystudents[0].addProf(myprofs[1]);
        mystudents[1].addProf(myprofs[1]);
        mystudents[1].addProf(myprofs[2]);
        mystudents[2].addProf(myprofs[0]);
        mystudents[3].addProf(myprofs[2]);
        mystudents[4].addProf(myprofs[1]);
        mystudents[5].addProf(myprofs[0]);
        mystudents[5].addProf(myprofs[1]);
        mystudents[5].addProf(myprofs[2]);

        for (Student s : mystudents)
            this.studentService.addStudent(s);
    
		return "redirect:/listStudents";
	}

    @GetMapping("/listStudents")
    public String listStudents(Model model) {
        model.addAttribute("students", this.studentService.getAllStudents());
        return "listStudents";
    }

    @GetMapping("/createStudent")
    public String createStudent(Model m) {
        m.addAttribute("student", new Student());
        m.addAttribute("allProfessors", this.profService.getAllProfessors());
        return "editStudent";
    }

    @GetMapping("/editStudent")
    public String editStudent(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Student> op = this.studentService.getStudent(id);
        if (op.isPresent()) {
            m.addAttribute("student", op.get());
            m.addAttribute("allProfessors", this.profService.getAllProfessors());
            return "editStudent";
        }
        else {
            return "redirect:/listStudents";
        }
    }    

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute Student st) {
        this.studentService.addStudent(st);
        return "redirect:/listStudents";
    }

    @GetMapping("/queryStudents")
    public String queryStudent1(Model m) {
        m.addAttribute("person", new FormData());
        return "queryStudents";
    }

    /* Note the invocation of a service method that is served by a query in jpql */
    @GetMapping("/queryResults")
    public String queryResult1(@ModelAttribute FormData data, Model m) {
        List<Student> ls = this.studentService.findByNameEndsWith(data.getName());
        m.addAttribute("students", ls);
        return "listStudents";
    }

    @GetMapping("/listProfessors")
    public String listProfs(Model model) {
        model.addAttribute("professors", this.profService.getAllProfessors());
        return "listProfessors";
    }

    @GetMapping("/createProfessor")
    public String createProfessor(Model m) {
        m.addAttribute("professor", new Professor());
        return "editProfessor";
    }

    private String getEditProfessorForm(int id, String formName, Model m) {
        Optional<Professor> op = this.profService.getProfessor(id);
        if (op.isPresent()) {
            m.addAttribute("professor", op.get());
            return formName;
        }
        return "redirect:/listProfessors";
    }

    @GetMapping("/editProfessor")
    public String editProfessor(@RequestParam(name="id", required=true) int id, Model m) {
        return getEditProfessorForm(id, "editProfessor", m);
    }    

    @PostMapping("/saveProfessor")
    public String saveProfessor(@ModelAttribute Professor prof) {
        this.profService.addProfessor(prof);
        return "redirect:/listProfessors";
    }

    /* For the sake of illustrating the use of @Transactional */
    @GetMapping("/changeOffice")
    public String getOfficeForm(@RequestParam(name="id", required=true) int id, Model m) {
        return getEditProfessorForm(id, "editProfessorOffice", m);
    }

    @PostMapping("/sumbitOfficeChange")
    public String changeOffice(@ModelAttribute Professor prof) {
        this.profService.changeProfOffice(prof.getId(), prof.getOffice());
        return "redirect:/listProfessors";
    }

}