package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.example.data.Team;
import com.example.data.Game;
import com.example.data.User;
import com.example.data.Role;
import com.example.data.Event;
import com.example.data.Player;
import com.example.formdata.FormData;
import com.fasterxml.classmate.GenericType;
import com.google.gson.JsonObject;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ch.qos.logback.core.html.NOPThrowableRenderer;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;

@Controller
public class DataController {
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

    @Autowired
    RoleService roleService;

    int numTeams = 3;
    int numGames = 5;
    int numPlayers = 6;
    Boolean gotData = false;

    String apiKey = "25e4028474ad234869fcfe1da360a9ab";

    @GetMapping("/getData")
    public String getData() {
        return "getData";
    }

    // @RequestMapping(value = "/getData", method = { RequestMethod.GET,
    // RequestMethod.POST })

    void generateTeams(JSONArray response) {

        for (int i = 0; i < response.length(); i++) {
            var team = response.getJSONObject(i).getJSONObject("team");
            int teamApiId = team.getInt("id");
            Team newTeam = new Team(team.getString("name"), team.getString("logo"));
            // Team newTeam = new Team(team.getString("name"));
            System.out.println("Team idapi: " + teamApiId + "and team real id: " + newTeam.getId());

            // System.out.println("This team id is " + newTeam.getId());
            newTeam.setNumberWins((int) (Math.random() * 19));
            newTeam.setNumberLoses((int) (Math.random() * 19));
            newTeam.setNumberLoses((int) (Math.random() * 19));

            newTeam.setIdApi(teamApiId);
            this.teamService.addTeam(newTeam);
            if (i == numTeams - 1)
                break;
        }
    }

    void generateGames() {
        var teams = teamService.getAllTeams(); // [t1,t2,...]
        // System.out.println("buscou teams");
        String[] places = { "Lisboa", "Coimbra", "Porto", "Leira", "Braga" };
        String[] gameStates = { "Game not started", "Game stopped", "Game started" };

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
                        this.eventService.addEvent(new Event("Goal", game.getDate(), game, teams.get(indexTeam1), p));
                    }
                }
                if (teams.get(indexTeam2).getPlayers().size() > 0) {
                    // add goals of team2
                    List<Player> players2 = teams.get(indexTeam2).getPlayers();
                    for (int j = 0; j < numGoalsTeam2; j++) {
                        Player p = players2.get((int) (Math.random() * players2.size()) % players2.size());
                        this.eventService.addEvent(new Event("Goal", game.getDate(), game, teams.get(indexTeam2), p));
                    }
                }
                game.setGoalsTeam1(numGoalsTeam1);
                game.setGoalsTeam2(numGoalsTeam2);
            }

            if (gameStates[indexState].equals("Game stopped")) {
                Event stopEvent = new Event("Game stopped", game.getDate(), game);
                this.eventService.addEvent(stopEvent);
                game.setIsPaused(true);
            } else if (gameStates[indexState].equals("Game not started")) {
                game.setIsPaused(true);
            } else {
                game.setIsPaused(false);
            }
            this.gameService.addGame(game);
        }

    }

    void generatePlayers() throws JSONException, ParseException {
        List<Team> teams = teamService.getAllTeams();
        System.out.println("Teams size: " + teams.size());
        for (int i = 0; i < teams.size(); i++) {
            int id = teams.get(i).getIdApi();
            System.out.println("teams apid: " + id + " teams id: " + teams.get(i).getId());
            Integer goals = 0, yellowCards = 0, redCards = 0;
            JSONArray responsePlayers = Unirest
                    .get("https://v3.football.api-sports.io/players?league=39&season=2018&team=" + id)
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", "v3.football.api-sports.io").asJson()
                    .getBody()
                    .getObject().getJSONArray("response");

            System.out.println("Players size: " + responsePlayers.length());
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
        if (gotData) {
            return "redirect:/homepage";
        }
        gotData = true;
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

        // System.out.println("gerou players");

        /*
         * var teams = teamService.getAllTeams();
         * var games = gameService.getAllGames();
         * var players = playerService.getAllPlayers();
         * 
         * // event goal
         * Event[] events = {
         * new Event("goal", new Date(), games.get(0), teams.get(0), players.get(0)),
         * new Event("Game Ended", new Date(), games.get(0))
         * };
         * 
         * for (Event ev : events)
         * this.eventService.addEvent(ev);
         */

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
    public String teamInfo(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Team> op = this.teamService.getTeam(id);
        // get player list
        System.out.println("team 280 id: " + id);
        if (op.isPresent()) {
            m.addAttribute("team", op.get());
            m.addAttribute("players", op.get().getPlayers());
            return "teamInfo";
        }
        return "redirect:/homepage";
    }

    @GetMapping("/")
    public String redirect() {
        Boolean addUser = true;
        Boolean addAdmin = true;

        List<Role> roles = this.roleService.getAllRoles();
        for (Role rol : roles) {
            if (rol.getName().equals("USER"))
                addUser = false;
            if (rol.getName().equals("ADMIN"))
                addAdmin = false;
        }
        if (addUser) {
            this.roleService.addRole(new Role("USER"));
        }
        if (addAdmin) {
            this.roleService.addRole(new Role("ADMIN"));
        }

        User adminUser = this.userService.getUserByName("admin");

        if (adminUser == null) {
            Role adminRole = this.roleService.getRoleByName("ADMIN");
            User admin = new User("admin", "admin", adminRole);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(admin.getPassword());
            admin.setPassword(encodedPassword);

            this.userService.addUser(admin);

        }

        User normalUser = this.userService.getUserByName("user");

        if (normalUser == null) {
            Role userRole = this.roleService.getRoleByName("USER");
            User user = new User("user", "user", userRole);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);

            this.userService.addUser(user);

        }

        return "redirect:/homepage";
    }

    public User encodePassword(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        return user;
    }

    @GetMapping("/gameInfo")
    public String gameDetails(@RequestParam(name = "id", required = true) int id, Model m) {
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
    public String addEvent(@RequestParam(name = "id", required = true) int id, Model m) {
        Optional<Game> op = this.gameService.getGame(id);
        if (op.isPresent()) {
            Game g = op.get();
            List<Team> teams = g.getTeams();

            List<Player> playersTotal = teams.get(0).getPlayers();
            List<Player> playersTeam2 = teams.get(1).getPlayers();

            playersTotal.addAll(playersTeam2);

            m.addAttribute("game", g);
            m.addAttribute("players", playersTotal);
            m.addAttribute("event", new Event(new Date(), g));
            return "addEvent";

        } else {
            return "redirect:/homepage";
        }

    }

    @GetMapping("/gameInfo/changeGameStatus")
    public String changeGameStatus(@RequestParam(name = "id", required = true) int id, Model m) {
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
                    Game game = event.getGame();
                    List<Team> ts = game.getTeams();
                    Team t = event.getPlayerEvent().getTeamPlayer();
                    if (ts.get(0).getName().equals(t.getName())) {
                        game.setGoalsTeam1(game.getGoalsTeam1() + 1);

                    } else if (ts.get(1).getName().equals(t.getName())) {
                        game.setGoalsTeam2(game.getGoalsTeam2() + 1);
                    }
                    break;

                case "Yellow card":
                    event.getPlayerEvent().setYellowCards(event.getPlayerEvent().getYellowCards() + 1);
                    break;

                case "Red card":
                    event.getPlayerEvent().setRedCards(event.getPlayerEvent().getRedCards() + 1);
                    break;
            }
        } else { // game event
            Game game = event.getGame();
            game.setGameState(event.getContent());
            if (event.getContent().equals("Game ended")) {
                // game is a draw
                if (game.getGoalsTeam1() == game.getGoalsTeam2()) {
                    game.setIsTie(true);
                    List<Team> teams = game.getTeams();
                    teams.get(0).setNumberDraws(teams.get(0).getNumberDraws() + 1);
                    teams.get(1).setNumberDraws(teams.get(1).getNumberDraws() + 1);

                } else if (game.getGoalsTeam1() > game.getGoalsTeam2()) { // team 0 wins
                    game.setIsTie(false);
                    List<Team> teams = game.getTeams();
                    teams.get(0).setNumberWins(teams.get(0).getNumberWins() + 1);
                    teams.get(1).setNumberLoses(teams.get(1).getNumberLoses() + 1);

                } else { // team 1 wins
                    game.setIsTie(false);
                    List<Team> teams = game.getTeams();
                    teams.get(0).setNumberLoses(teams.get(0).getNumberLoses() + 1);
                    teams.get(1).setNumberWins(teams.get(1).getNumberWins() + 1);
                }

                event.getGame().setIsOver(true);
            }

            if (event.getContent().equals("Game stopped")) {
                game.setIsPaused(true);
            } else {
                game.setIsPaused(false);
            }

        }
    }

    @GetMapping("/addUser")
    public String addUser(Model m) {
        User us = new User();
        m.addAttribute("user", us);
        m.addAttribute("roles", this.roleService.getAllRoles());
        return "addUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model m) {
        if (user.getUsername().isBlank() || user.getPassword().isBlank())
            return "redirect:/addUser";

        user = this.encodePassword(user);

        User u1 = this.userService.getUserByName(user.getUsername());

        if (u1 == null) {
            this.userService.addUser(user);
        } else {
            m.addAttribute("errorMsg", "User already exist!");
            return "error";
        }

        return "redirect:/homepage";
    }

    @GetMapping("/addGame")
    public String addGame(Model m) {
        Game game = new Game();
        List<Team> teams = this.teamService.getAllTeams();

        if (teams.size() < 2) {
            m.addAttribute("errorMsg", "Not enough teams to create a game! Please add more teams.");
            return "error";
        }
        m.addAttribute("game", game);
        m.addAttribute("teamsAvailable", teams);

        return "addGame";
    }

    @PostMapping("/saveGame")
    public String saveGame(@ModelAttribute Game game, Model m) {
        List<Team> teams = game.getTeams();
        if (teams.get(0).getId() != teams.get(1).getId()) {
            game.getTeams().get(0).addGame(game);
            game.getTeams().get(1).addGame(game);

            this.gameService.addGame(game);
        }

        return "redirect:/homepage";
    }

    @GetMapping("/addTeam")
    public String addTeam(Model m) {
        Team team = new Team();
        m.addAttribute("team", team);

        return "addTeam";
    }

    @PostMapping("/saveTeam")
    public String saveTeam(@ModelAttribute Team team, Model m) {
        System.out.println(team.getImagePath());
        this.teamService.addTeam(team);

        return "redirect:/homepage";
    }

    @GetMapping("/addPlayer")
    public String addPlayer(Model m) {
        List<Team> teams = this.teamService.getAllTeams();

        if (teams.size() < 1) {
            m.addAttribute("errorMsg", "There are no teams available! Please add teams before adding players.");
            return "error";
        }

        Player player = new Player();
        m.addAttribute("player", player);
        m.addAttribute("teams", teams);

        return "addPlayer";
    }

    @PostMapping("/savePlayer")
    public String savePlayer(@ModelAttribute Player player, Model m) {
        this.playerService.addPlayer(player);

        return "redirect:/homepage";
    }

    @PostMapping("/logout")
    public String logout(Model m) {
        return "redirect:/homepage";
    }

    @PostMapping("/login")
    public String login(Model m) {
        return "redirect:/homepage";
    }

}