package exercise;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WelcomeController{
    @GetMapping("/")
    public String index(){
        return "Welcome to Spring";
    }

    @GetMapping("/hello")
    public String welcomeUser(@RequestParam(name = "name", defaultValue = "World") String name){
        return String.format("Hello" + ", " + name + "!");
    }
}