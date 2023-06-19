package exercise.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController

@RequiredArgsConstructor
public class PeopleController {


    private final JdbcTemplate jdbc;


    @PostMapping(path = "/people")
    public void createPerson(@RequestBody Map<String, Object> person) {
        String query = "INSERT INTO person (first_name, last_name) VALUES (?, ?)";
        jdbc.update(query, person.get("first_name"), person.get("last_name"));
    }

    @GetMapping("/people")
    public List<Map<String, Object>> getPeople() {
        String sql = "SELECT * FROM person";
        return jdbc.queryForList(sql);
    }

    @GetMapping("/people/{id}")
    public Map<String, Object> getPerson(@PathVariable("id") int id){
        String sql = "SELECT * FROM person WHERE id = ?";
        return jdbc.queryForMap(sql, id);
    }

}


