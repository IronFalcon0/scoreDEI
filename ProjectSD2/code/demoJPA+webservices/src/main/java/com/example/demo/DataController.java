package com.example.demo;

import java.util.List;
import java.util.Optional;
import java.util.Date;

import com.example.data.Professor;
import com.example.data.Student;
import com.example.data.Team;
import com.example.data.Game;
import com.example.data.User;
import com.example.data.Event;
import com.example.data.Player;
import com.example.formdata.FormData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ch.qos.logback.core.html.NOPThrowableRenderer;


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

    @Autowired
    EventService eventService;

    @Autowired
    PlayerService playerService;


    @GetMapping("/getData")
    public String getData() {
        return "getData";
    }

   // @RequestMapping(value = "/getData", method = { RequestMethod.GET, RequestMethod.POST })
    @PostMapping("/createData")
    public String getData(Model model) {
        Game[] games = {
            new Game("lisboa", new Date()),
            new Game("lisboa", new Date())
        };
        Team[] teams = {
            new Team("benfica", 0),
            new Team("porto", 1),
            new Team("sporting", 2)
        }; 
        games[0].setTeams(teams[0], teams[1]);
        games[1].setTeams(teams[1], teams[2]);

        Player[] players = {
            new Player("tiago", "avançado", new Date(), teams[0]),
            new Player("rod", "avançado", new Date(), teams[0]),
            new Player("sofia", "avançado", new Date(), teams[1])
        };

        players[0].setGoalsScored(players[0].getGoalsScored() + 1);
        players[1].setGoalsScored(players[1].getGoalsScored() + 1);

        //event goal
        Event[] events = {
            new Event("goal", new Date(), games[0], teams[0], players[0]),
            new Event("Game Ended", new Date(), games[0])
        };
        games[0].setGoalsTeam1(games[0].getGoalsTeam1() + 1);
        games[0].setGameState("Game Ended");
        teams[0].setNumberWins(teams[0].getNumberWins() + 1);
        teams[1].setNumberLoses(teams[1].getNumberLoses() + 1);

        
        
        for (Team t: teams) 
            this.teamService.addTeam(t);

        for (Game g : games)
            this.gameService.addGame(g);

        for (Player p : players)
            this.playerService.addPlayer(p);

        for (Event ev : events)
            this.eventService.addEvent(ev);

        return "redirect:/homepage";
    }

    @GetMapping("/homepage")
    public String homePage(Model model) {
        model.addAttribute("games", this.gameService.getAllGames());
        return "homePage";
    }

    @GetMapping("/listGames")
    public String listGames(Model model) {
        model.addAttribute("games", this.gameService.getAllGames());
        return "homePage";
    }

    @GetMapping("/stats")
    public String showStats(Model model) {
        model.addAttribute("teamsGames", this.teamService.listTeamsByGames());

        model.addAttribute("bestPlayers", this.playerService.listBestPlayers());
        model.addAttribute("teamsWins", this.teamService.listTeamsByWins());
        model.addAttribute("teamsDraws", this.teamService.listTeamsByDraws());
        model.addAttribute("teamsLoses", this.teamService.listTeamsByLoses());
        return "stats";
    }

    @GetMapping("/teamInfo")
    public String teamInfo(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Team> op = this.teamService.getTeam(id);
        // get player list
        if (op.isPresent()) {
            m.addAttribute("team", op.get());
            m.addAttribute("players", op.get().getPlayers());
            return "teamInfo";
        }
        return "redirect:/homepage";
    }


    @GetMapping("/")
    public String redirect() {
        return "redirect:/homepage";
    }

    @GetMapping("/gameInfo")
    public String gameDetails(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Game> op = this.gameService.getGame(id);
        // get event list
        if (op.isPresent()) {
            m.addAttribute("game", op.get());
            m.addAttribute("events", this.eventService.listEventsOfGame(id));
            return "gameInfo";
        }
        return "redirect:/homepage";
    }

    @GetMapping("/gameInfo/addEvent")
    public String addEvent(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Game> op = this.gameService.getGame(id);
        if (op.isPresent()) {
            Game g = op.get();
            List<Team> teams = g.getTeams();

            List<Player> playersTotal = teams.get(0).getPlayers();
            List<Player> playersTeam2 = teams.get(1).getPlayers();

            playersTotal.addAll(playersTeam2);
            System.out.println(playersTotal);

            m.addAttribute("game", g);
            m.addAttribute("players", playersTotal);
            m.addAttribute("event", new Event(new Date(), g));
            return "addEvent";

        } else {
            return "redirect:/homepage";
        }

        
    }

    @GetMapping("/gameInfo/changeGameStatus")
    public String changeGameStatus(@RequestParam(name="id", required=true) int id, Model m) {
        Optional<Game> op = this.gameService.getGame(id);
        if (op.isPresent()) {
            Game g = op.get();

            m.addAttribute("game", g);
            Event ev = new Event(new Date(), g);

            m.addAttribute("event", ev);
            return "gameStatus";

        } else {
            return "redirect:/homepage";
        }
    }

    @PostMapping("/saveEvent")
    public String saveEvent(@ModelAttribute Event event, Model m, RedirectAttributes redirectAttributes) {
        int gameId = event.getGame().getId();
        redirectAttributes.addAttribute("id", gameId);

        if (!event.getGame().getIsOver()) {            
            this.connectEvent(event);
            this.eventService.addEvent(event);
        }
        return "redirect:/gameInfo";
    }

    public void connectEvent(Event event) {
        // player event
        if (event.getPlayerEvent() != null) {
            switch (event.getContent()) {
                case "Goal":
                    event.getPlayerEvent().setGoalsScored(event.getPlayerEvent().getGoalsScored() + 1);
                    List<Team> ts = event.getGame().getTeams();
                    Team t = event.getPlayerEvent().getTeam();
                    if (ts.get(0).getName().equals(t.getName())) {
                        event.getGame().setGoalsTeam1(event.getGame().getGoalsTeam1() + 1);
                        event.setTeam(ts.get(0));
                    } else if (ts.get(1).getName().equals(t.getName())) {
                        event.getGame().setGoalsTeam2(event.getGame().getGoalsTeam2() + 1);
                        event.setTeam(ts.get(1));
                    }
                    break;

                case "Yellow card":
                    event.getPlayerEvent().setYellowCards(event.getPlayerEvent().getYellowCards() + 1);
                    break;

                case "Red card":
                    event.getPlayerEvent().setRedCards(event.getPlayerEvent().getRedCards() + 1);
                    break;
            }
        } else  {       // game event
            event.getGame().setGameState(event.getContent());
            if (event.getContent().equals("Game ended")) {
                event.getGame().setIsOver(true);
            }
        }
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