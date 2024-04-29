package cst;

import jakarta.servlet.http.Cookie;
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
    public RandomString randomString = new RandomString(32);

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
                        User user = userObject.get(name);

                        if(password.equals(user.password)) {
                            String cookie = findCookie(request, "user_token");

                            if (cookie == null || !cookie.equals(user.sessionID)) {
                                //new browser instance give them the cookie
                                //or the cached cookie is stale so resend them it
                                response.addCookie(new Cookie("user_token", user.sessionID));
                            } else {
                                System.out.println("User is good to go");
                            }
                        } else {
                            //invalid password
                        }
                    } else {
                        String sessionToken = randomString.nextString();

                        User user = new User(name, password, 0, new String[] {}, sessionToken);
                        userObject.put(name, user);

                        users.put(createUser(name, 0));
                        message = users.toString();

                        response.addCookie(new Cookie("user_token", sessionToken));
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

    public String scores;
    @RequestMapping(value = "/scores")
    public String scores() {
        return scores;
    }

    public String style;
    @RequestMapping(value = "/style.css")
    public String style() {
        return style;
    }

    public String login;
    @RequestMapping(value = "/digitized2024")
    public String login() {
        return login;
    }

    public Main() {
        scores = readString("/fetcher.html");
        style = readString("/style.css");
        login = readString("/login.html");
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

    public String findCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return null;
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