package cst;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@SpringBootApplication
public class Main {

    public JSONArray users = new JSONArray().put(createUser("user", 10));
    public HashMap<String, User> userObject = new HashMap<>();

    public String message = users.toString();
    public SecureRandom random = new SecureRandom();

    public String redirect = "http://localhost:8080/scores";

    public String url = "http://localhost:8080/digitized2024";

    public record LoginRequest(String username, String password) {}

    /**
     *
     * @param payload
     *  {
     *      username: "the username" //must be alphanumeric
     *      password: "the password"
     *  }
     */
    @PostMapping(value = "/create_user")
    public void createUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute LoginRequest loginRequest) throws IOException {
        synchronized (userObject) {
            System.out.println(loginRequest.password);
            System.out.println(loginRequest.username);

            String name = loginRequest.username;
            String password = loginRequest.password;


            if(name.matches("[A-Za-z0-9]+")) {
                if(name.length() < 32) {
                    if(userObject.containsKey(name)) {
                        //update user token
                    } else {
                        User user = new User(name, password, 0, new String[] {}, request.getSession().getId());
                        userObject.put(name, user);

                        users.put(createUser(name, 0));
                        message = users.toString();
                    }
                }
            }
            response.sendRedirect(redirect);
        }
    }

    @PostMapping("/update_user")
    public void updateUser(HttpServletRequest request, @RequestBody String payload) {

    }

    /**
     *
     * @return
     * [
     *     {
     *          username: "the username:",
     *          score: "score"
     *     }
     * ]
     */
    @GetMapping("/get_scores")
    public String getScores() {
        return message;
    }

    public JSONObject createUser(String name, int score) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("username", name);
        jsonObject.put("score", score);
        jsonObject.put("flags", new JSONArray());

        return jsonObject;
    }

    @RequestMapping(value = "/scores")
    public String scores() {
        return readString("/fetcher.html");
    }

    @RequestMapping(value = "/style.css")
    public String style() {
        return readString("/style.css");
    }

    @RequestMapping(value = "/digitized2024")
    public String login() {
        return readString("/index.html");
    }

    public static String readString(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream stream = Main.class.getResourceAsStream(path);
        if(stream == null) {
            System.out.println("Cant read file: " + path);
            return "";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        reader.lines().forEach(string -> stringBuilder.append(string).append("\n"));
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    public static class User {

        public String username;
        public String password;
        int score;
        String[] flags;

        public String sessionID;


        public User(String username, String password, int score, String[] flags, String sessionID) {
            this.username = username;
            this.password = password;
            this.score = score;
            this.flags = flags;

            this.sessionID = sessionID;
        }
    }
}