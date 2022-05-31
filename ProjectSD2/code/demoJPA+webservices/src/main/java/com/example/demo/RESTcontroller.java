package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.data.Team;
import com.example.data.Game;
import com.example.data.User;
import com.example.data.Event;
import com.example.data.Player;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;


@RestController
@RequestMapping("rest")
public class RESTcontroller {
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

    
    /*
    int numTeams = 3;
    int numGames = 5;
    int numPlayers = 6;
    String apiKey = "25e4028474ad234869fcfe1da360a9ab";

    

    @PostMapping("/createData1")
    public String getData1(Model model) {
        return "redirect:/homepage";
    }

    void generateTeams(JSONArray response) {

        for (int i = 0; i < response.length(); i++) {
            var team = response.getJSONObject(i).getJSONObject("team");
            Team newTeam = new Team(team.getString("name"), team.getString("logo"));
            // Team newTeam = new Team(team.getString("name"));
            int teamId = team.getInt("id");
            // System.out.println("Team id: " + teamId);

            newTeam.setId(teamId);
            // System.out.println("This team id is " + newTeam.getId());
            newTeam.setNumberWins((int) (Math.random() * 19));
            newTeam.setNumberLoses((int) (Math.random() * 19));
            newTeam.setNumberLoses((int) (Math.random() * 19));

            this.teamService.addTeam(newTeam);
            if (i == numTeams - 1)
                break;
        }
    }

    void generateGames() {
        var teams = teamService.getAllTeams(); // [t1,t2,...]
        // System.out.println("buscou teams");
        String[] places = { "Lisboa", "Coimbra", "Porto", "Leira", "Braga" };
        String[] gameStates = { "Game not started", "Game stopped", "Game started"};

        for (int i = 0; i < numGames; i++) {
            int indexTeam1 = (int) (Math.random() * numTeams) % numTeams;
            int indexTeam2;
            do {
                indexTeam2 = (int) (Math.random() * numTeams) % numTeams;
            } while (indexTeam2 == indexTeam1);

            int indexPlace = (int) (Math.random() * 5) % 5; // o % é só para ter a certeza que n vai out of bounds
            int numGoalsTeam1 = (int) (Math.random() * 4) % 4;
            int numGoalsTeam2 = (int) (Math.random() * 4) % 4;
            int indexState = (int) (Math.random() * 3) % 3;

            Game game = new Game(places[indexPlace], new Date());
            game.set2Teams(teams.get(indexTeam1), teams.get(indexTeam2));
            game.setGameState(gameStates[indexState]);
            // add goals and start game
            if (!gameStates[indexState].equals("Game not started")) {
                Event startEvent = new Event("Game started", game.getDate(), game);
                this.eventService.addEvent(startEvent);

                if (teams.get(indexTeam1).getPlayers().size() > 0) {
                    // add goals of team1
                    List<Player> players = teams.get(indexTeam1).getPlayers();
                    for (int j = 0; j < numGoalsTeam1; j++) {
                        Player p = players.get((int) (Math.random() * players.size()) % players.size());
                        this.eventService.addEvent(new Event("Goal",game.getDate(), game, teams.get(indexTeam1), p));
                    }
                }
                if (teams.get(indexTeam2).getPlayers().size() > 0) {
                    // add goals of team2
                    List<Player> players2 = teams.get(indexTeam2).getPlayers();
                    for (int j = 0; j < numGoalsTeam2; j++) {
                    Player p = players2.get((int) (Math.random() * players2.size()) % players2.size());
                    this.eventService.addEvent(new Event("Goal",game.getDate(), game, teams.get(indexTeam2), p));
                    }
                }

                game.setGoalsTeam1(numGoalsTeam1);
                game.setGoalsTeam2(numGoalsTeam2);
            }
            
            if (gameStates[indexState].equals("Game stopped")) {
                Event stopEvent = new Event("Game stopped", game.getDate(), game);
                this.eventService.addEvent(stopEvent);
                game.setIsPaused(true);
            } else {
                game.setIsPaused(false);
            }
            this.gameService.addGame(game);
        }

    }

    void generatePlayers() throws JSONException, ParseException {
        List<Team> teams = teamService.getAllTeams();

        for (int i = 0; i < teams.size(); i++) {
            int id = teams.get(i).getId();
            Integer goals = 0, yellowCards = 0, redCards = 0;
            JSONArray responsePlayers = Unirest
                    .get("https://v3.football.api-sports.io/players?league=39&season=2018&team=" + id)
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", "v3.football.api-sports.io").asJson()
                    .getBody()
                    .getObject().getJSONArray("response");

            for (int j = 0; j < responsePlayers.length(); j++) {

                var player = responsePlayers.getJSONObject(j).getJSONObject("player");
                var stats = responsePlayers.getJSONObject(j).getJSONArray("statistics");

                // int goals = stats.getJSONObject(0).getJSONObject("goals").getInt("total");
                // System.out.println("Stats size: " + stats.length() + " | " +
                // stats.getJSONObject(0).length() + " |");
                JSONObject cards = stats.getJSONObject(0).getJSONObject("cards");

                yellowCards = stats.getJSONObject(0).getJSONObject("cards").optInt("yellow");
                redCards = stats.getJSONObject(0).getJSONObject("cards").optInt("red");
                goals = stats.getJSONObject(0).getJSONObject("goals").optInt("total");

                String position = stats.getJSONObject(0).getJSONObject("games").getString("position");

                Player newPlayer = new Player(player.getString("name"), position,
                        new SimpleDateFormat("yyyy-MM-dd").parse(player.getJSONObject("birth").getString("date")),
                        goals, yellowCards, redCards,
                        teams.get(i));

                newPlayer.getTeamPlayer().addPlayer(newPlayer);
                this.playerService.addPlayer(newPlayer);
                if (j == numPlayers - 1)
                    break;
            }
        }

    }

    @PostMapping("/createData")
    public String getData(Model model) throws JSONException, ParseException {

        JSONArray responseTeams = Unirest
                .get("https://v3.football.api-sports.io/teams?league=39&season=2018")
                .header("x-rapidapi-key", apiKey)
                .header("x-rapidapi-host", "v3.football.api-sports.io").asJson()
                .getBody()
                .getObject().getJSONArray("response");

        // Retirar da API informção (nome) de 20 equipas
        // To Do: retirar imagens
        generateTeams(responseTeams);

        generatePlayers();

        generateGames();

        return "redirect:/homepage";
    }*/

    /*
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
    }*/
}
