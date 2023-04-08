package exercise.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import exercise.User;
import org.apache.commons.lang3.ArrayUtils;


@WebServlet("/users/*")

public class UsersServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            showUsers(request, response);
            return;
        }

        String[] pathParts = pathInfo.split("/");
        String id = ArrayUtils.get(pathParts, 1, "");

        showUser(request, response, id);
    }


    private List<User> getUsers() throws JsonProcessingException, IOException {

        ObjectMapper mapper = new ObjectMapper();

        String json = new String(Files.readAllBytes(Paths.get("/home/george/Hexlet/hexlet-assignments/java-web-ru/html/src/main/resources/users.json")));

        List<User> users = mapper.readValue(json, new TypeReference<List<User>>() {
        });

        return users;
    }


    private void showUsers(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {

        List<User> allUsers = getUsers();
        StringBuilder body = new StringBuilder();

        body.append("""
                <!DOCTYPE html>
                <html lang=\"ru\">
                    <head>
                        <meta charset=\"UTF-8\">
                        <title>Example application | Users</title>
                        <link rel=\"stylesheet\" href=\"mysite.css\">
                    </head>
                    <body>
                        <table> 
                            <tr> 
                                <th>id</th> 
                                <th>fullName</th>
                            </tr>
                """);

        for (User user : allUsers) {

            body.append("<tr>\n");
            body.append("<td>").append(user.getId()).append("</td>\n");
            body.append("<td><a href=\"/users/").append(user.getId()).append("\">").append(user.getFirstName() + " " + user.getLastName()).append("</a></td>\n");
            body.append("</tr>\n");
        }

        body.append("</table>\n");
        body.append("</body>\n");
        body.append("</html>\n");

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println(body);
    }

    private void showUser(HttpServletRequest request,
                          HttpServletResponse response,
                          String id) throws IOException {

        List<User> allUsers = getUsers();
        StringBuilder body = new StringBuilder();
        boolean userFound = false;


        for (User user : allUsers) {
            if (user.getId().equals(id)) {
                body.append("""
                        <!DOCTYPE html>
                        <html lang=\"ru\">
                            <head>
                                <meta charset=\"UTF-8\">
                                <title>Example application | Users</title>
                                <link rel=\"stylesheet\" href=\"mysite.css\">
                            </head>
                            <body>
                                <table> 
                                    <tr> 
                                        <th>id</th> 
                                        <th>firstName</th>
                                        <th>lastName</th>
                                        <th>email</th>
                                        <th>
                                    </tr>
                        """);

                body.append("<tr>\n");
                body.append("<td>").append(user.getId()).append("</td>\n");
                body.append("<td>").append(user.getFirstName()).append("</td>\n");
                body.append("<td>").append(user.getLastName()).append("</td>\n");
                body.append("<td>").append(user.getEmail()).append("</td>\n");
                body.append("</tr>\n");
                body.append("</table>\n");
                body.append("</body>\n");
                body.append("</html>\n");
                response.setContentType("text/html;charset=UTF-8");

                PrintWriter out = response.getWriter();
                out.println(body);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
        }

    }
}






