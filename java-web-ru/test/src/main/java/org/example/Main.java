package org.example;



import java.io.IOException;



import java.util.List;


import java.nio.file.Paths;
import java.nio.file.Files;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;




public class Main {
    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        String json = new String(Files.readAllBytes(Paths.get("/home/george/Hexlet/hexlet-assignments/java-web-ru/test/src/main/resources/users.json")));

        try {
            List<User> users = mapper.readValue(json, new TypeReference<List<User>>() {
            });

            for (User user : users) {
                System.out.println("ID" + " " + user.getId() + "name" + " " + user.getFirstName());
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }
}
