package cst;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Serving {

    public Serving() {
        style = Main.readString("/i_style.css");
        style1 = Main.readString("/f_style.css");
        style2 = Main.readString("/h_style.css");

        login = Main.readString("/index.html");
        scores = Main.readString("/fetcher.html");
        main = Main.readString("/main.html");
        mainjs = Main.readString("/main.js");
        challenges = Main.readString("challenges.json");
    }

    public String scores;
    @RequestMapping(value = "/scores")
    public String scores() {
        return scores;
    }

    public String style;
    @RequestMapping(value = "/i_style.css")
    public String style() {
        return style;
    }

    public String style1;
    @RequestMapping(value = "/f_style.css")
    public String style1() {
        return style1;
    }

    public String style2;
    @RequestMapping(value = "/h_style.css")
    public String style2() {
        return style2;
    }

    public String login;
    @RequestMapping(value = "/digitized2024")
    public String login() {
        return login;
    }

    public String main;
    @RequestMapping(value = "/main")
    public String main() {
        return main;
    }

    public String mainjs;
    @RequestMapping(value = "/main.js")
    public String mainjs() {
        return mainjs;
    }

    public String challenges;
    @RequestMapping(value = "/challenges.json")
    public String challenges() {
        return challenges;
    }
}
