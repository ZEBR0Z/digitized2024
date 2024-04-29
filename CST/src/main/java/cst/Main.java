package cst;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

@RestController
@SpringBootApplication
public class Main {

    public JSONArray users = new JSONArray();

    public String message = users.toString();

    /**
     *
     * @param payload
     *  {
     *      name: "the username"
     *  }
     */
    @PostMapping("/create_user")
    public void createUser(HttpServletRequest request, @RequestBody String payload) {
        synchronized (users) {
            JSONObject jsonObject = new JSONObject(payload);

            String name = jsonObject.getString("name");
            //String password = jsonObject.getString("password");
            if(!name.matches("[A-Za-z0-9]+")) {
                users.put(name);
                message = users.toString();
            }
        }
    }

    /**
     *
     * @return
     * [
     *     {
     *          name: "the username:",
     *          score: "score"
     *     }
     * ]
     */
    @GetMapping("/get_scores")
    public String getScores() {
        JSONArray array = new JSONArray();
        array.put(makeUser("parsa", 100));
        array.put(makeUser("hilligans", new Random().nextInt(200)));
        System.out.println("sent");
        return array.toString();
    }

    public JSONObject makeUser(String name, int score) {
        return new JSONObject().put("name", name).put("score", score);
    }

    @RequestMapping(value = "/scores")
    public String scores() {
        return readString("/fetcher.html");
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
}