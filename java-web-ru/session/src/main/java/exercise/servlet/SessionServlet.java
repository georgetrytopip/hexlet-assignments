package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;

import java.util.Map;

import static exercise.App.getUsers;
import exercise.Users;

public class SessionServlet extends HttpServlet {

    private Users users = getUsers();

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        if (request.getRequestURI().equals("/login")) {
            showLoginPage(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        switch (request.getRequestURI()) {
            case "/login" -> login(request, response);
            case "/logout" -> logout(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showLoginPage(HttpServletRequest request,
                               HttpServletResponse response)
                 throws IOException, ServletException {

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
        requestDispatcher.forward(request, response);
    }

    private void login(HttpServletRequest request,
                               HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();

        Map<String, String> user = users.findByEmail(request.getParameter("email"));

        if (user != null) {
            String userPass = request.getParameter("password");
            if (!userPass.equals("password")) {
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
                request.setAttribute("user", user);
                session.setAttribute("flash", "Неверный логин или пароль");
                response.setStatus(422);
                requestDispatcher.forward(request, response);
                return;
            }

            session.setAttribute("userId", user.get("id"));
            session.setAttribute("flash", "Вы успешно вошли");
            response.sendRedirect("/");
            return;
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login.jsp");
        request.setAttribute("user", user);
        session.setAttribute("flash", "Неверный логин или пароль");
        response.setStatus(422);
        requestDispatcher.forward(request, response);

    }



    private void logout(HttpServletRequest request,
                               HttpServletResponse response)
                 throws IOException {

        HttpSession session = request.getSession();
        session.removeAttribute("userId");
        session.setAttribute("flash", "Вы успешно вышли");
        response.sendRedirect("/");
    }
}
