package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
//import sun.awt.image.BadDepthException;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BankClient bc = new BankClient(req.getParameter("name"),
                    req.getParameter("password"),
                    Long.parseLong(req.getParameter("money")));
            new BankClientService().addClient(bc);
            resp.getWriter().println("Client successful added.");
        } catch (DBException e) {
            e.printStackTrace();
            resp.getWriter().println("Exception in registration client:" + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            resp.getWriter().println("Exception in registration data:" + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
        resp.setContentType("text/html;charset=utf-8");
    }
}
