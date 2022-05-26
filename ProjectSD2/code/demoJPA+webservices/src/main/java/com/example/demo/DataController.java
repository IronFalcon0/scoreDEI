package com.example.demo;

import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;

import com.example.data.Team;
import com.example.data.Game;
import com.example.data.User;
import com.example.data.Role;
import com.example.data.Event;
import com.example.data.Player;
import com.example.formdata.FormData;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
            new Team("benfica"),
            new Team("porto"),
            new Team("sporting")
        }; 
        games[0].set2Teams(teams[0], teams[1]);
        games[1].set2Teams(teams[1], teams[2]);

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
        Boolean addUser = true;
        Boolean addAdmin = true;

        List<Role> roles = this.roleService.getAllRoles();
        for(Role rol: roles) {
            if(rol.getName().equals("USER"))
                addUser = false;
            if(rol.getName().equals("ADMIN"))
                addAdmin = false;
        }
        if(addUser) {
            this.roleService.addRole(new Role("USER"));
        }
        if(addAdmin) {
            this.roleService.addRole(new Role("ADMIN"));
        }

        User adminUser = this.userService.getUserByName("admin");

        if (adminUser == null) {
            Role adminRole = this.roleService.getRoleByName("ADMIN");
            User admin = new User("admin", "admin", adminRole);

            this.userService.addUser(admin);

        }



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
        } else  {       // game event
            Game game = event.getGame();
            game.setGameState(event.getContent());
            if (event.getContent().equals("Game ended")) {
                // game is a draw
                if(game.getGoalsTeam1() == game.getGoalsTeam2()) {
                    game.setIsTie(true);
                    List<Team> teams = game.getTeams();
                    teams.get(0).setNumberDraws(teams.get(0).getNumberDraws() + 1);
                    teams.get(1).setNumberDraws(teams.get(1).getNumberDraws() + 1);

                } else if (game.getGoalsTeam1() > game.getGoalsTeam2()) {       // team 0 wins
                    game.setIsTie(false);
                    List<Team> teams = game.getTeams();
                    teams.get(0).setNumberWins(teams.get(0).getNumberWins() + 1);
                    teams.get(1).setNumberLoses(teams.get(1).getNumberLoses() + 1);

                } else {                                                        // team 1 wins
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
        return "addUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, Model m) {
        if (user.getUsername().isBlank() || user.getPassword().isBlank())
            return "redirect:/addUser";

        this.userService.addUser(user);
        return "redirect:/homepage";
    }

    @GetMapping("/addGame")
    public String addGame(Model m) {
        Game game = new Game();
        List<Team> teams = this.teamService.getAllTeams();
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
        System.out.println(team.getImage());
        this.teamService.addTeam(team);
        
        return "redirect:/homepage";
    }

    /*@GetMapping("/getImage/{id}")
    public byte[] showProductImage(@ModelAttribute int id) {
        //response.setContentType("image/png"); // Or whatever format you wanna use
        System.out.println("here");
        System.out.println(id);
        Optional<Team> op = this.teamService.getTeam(id);
        if (op.isPresent()) {
            Team team = op.get();
            InputStream is = new ByteArrayInputStream(team.getImage());
            System.out.println(team.getImage());
            IOUtils.copy(is, response.getOutputStream());

            return team.getImage();
        }
    }*/

    @GetMapping("/addPlayer")
    public String addPlayer(Model m) {
        Player player = new Player();
        m.addAttribute("player", player);
        m.addAttribute("teams", this.teamService.getAllTeams());

        return "addPlayer";
    }

    @PostMapping("/savePlayer")
    public String savePlayer(@ModelAttribute Player player, Model m) {
        this.playerService.addPlayer(player);
        
        return "redirect:/homepage";
    }

}