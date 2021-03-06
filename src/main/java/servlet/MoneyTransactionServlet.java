package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> parameters = new HashMap<>();

        try {
            BankClient sender = new BankClient(
                    req.getParameter("senderName"),
                    req.getParameter("senderPass"),
                    Long.parseLong(req.getParameter("count")));
            if (bankClientService.sendMoneyToClient(sender,
                    req.getParameter("nameTo"),
                    Long.parseLong(req.getParameter("count")))) {
                parameters.put("message", "The transaction was successful");
            } else {
                parameters.put("message", "transaction rejected");
            }
        } catch (DBException | NullPointerException e) {
            parameters.put("message", "transaction rejected");
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", parameters));
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html;charset=utf-8");
    }
}
