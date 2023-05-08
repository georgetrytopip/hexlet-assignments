package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import exercise.TemplateEngineUtil;


public class ArticlesServlet extends HttpServlet {

    private String getId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return null;
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 1, null);
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "list";
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 2, getId(request));
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                showArticles(request, response);
                break;
            default:
                showArticle(request, response);
                break;
        }
    }

    private void showArticles(HttpServletRequest request,
                          HttpServletResponse response)
                throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");
        /*connection указан в скобках потому что нам необходимо жестко
        указать, что будет использоваться объект указанного в скобках типа*/
        List<Map<String, String>> articles = new ArrayList<>();


        String query = "SELECT id,title from articles  ORDER BY id ASC LIMIT 10 OFFSET ? ";

        try {
            String pageParam = request.getParameter("page");
            if(pageParam == null) {
                int page = 1;
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, 0);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    articles.add(Map.of(
                                    "id", rs.getString("id"),
                                    "title", rs.getString("title")
                            )
                    );
                }
                request.setAttribute("value", page);
            }

            if(pageParam != null){

                int intPage = Integer.parseInt(pageParam);

                int offset = intPage - 1;
                String newPageParam = offset + "0";
                int newOffset = Integer.parseInt(newPageParam);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, newOffset);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    articles.add(Map.of(
                                    "id", rs.getString("id"),
                                    "title", rs.getString("title")
                            )
                    );
                }
                request.setAttribute("value", intPage);
            }

        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }


        request.setAttribute("articles", articles);
        TemplateEngineUtil.render("articles/index.html", request, response);
    }




    private void showArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        ServletContext context = request.getServletContext();
        Connection connection = (Connection) context.getAttribute("dbConnection");

        Map<String, String> article = new HashMap<>();

        String query = "SELECT title, body FROM articles WHERE id = ?";

        try{

            String stringId = getId(request);

            if(stringId != null) {
                int id = Integer.parseInt(stringId);
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();

                if (!rs.next()) { // если ResultSet пустой, возвращаем 404
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }

                article.put("title", rs.getString("title"));
                article.put("body", rs.getString("body"));

                }


        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        request.setAttribute("article", article);
        TemplateEngineUtil.render("articles/show.html", request, response);
    }
}
