package exercise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.stream.Collectors;
import static exercise.Data.getCompanies;
import static exercise.Data.getCompany;


@WebServlet("/companies")
public class CompaniesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {



        PrintWriter out = response.getWriter();

        String search = request.getParameter("search");

        if(getCompany(search).isEmpty()) {
            out.println("Companies not found");
        }

        for (String cmpn : getCompany(search)) {
            out.println(cmpn);
        }
    }
}
