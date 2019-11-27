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

        System.out.println("Get data from test");
        System.out.println("Sender name is :" + req.getParameter("SenderName"));
        System.out.println("Sender pass is :" + req.getParameter("SenderPass"));
        System.out.println("Count is :" + req.getParameter("count"));
        System.out.println("Consumer is :" + req.getParameter("nameTo"));


        try {
            BankClient sender = new BankClient(
                    req.getParameter("SenderName"),
                    req.getParameter("SenderPass"),
                    Long.parseLong(req.getParameter("count")));
//            BankClient sender = bankClientService.getClientByName(req.getParameter("SenderName"));


                if (bankClientService.sendMoneyToClient(sender,
                    req.getParameter("nameTo"),
                    Long.parseLong(req.getParameter("count")))) {
                parameters.put("message", "The transaction was successful");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", parameters));
                resp.setStatus(HttpServletResponse.SC_OK);
                System.out.println("Transaction complete!");

//                if ((sender = bankClientService.getClientByName(req.getParameter("SenderName"))) != null
//                    && sender.getPassword().equals(req.getParameter("SenderPass"))) {
//                bankClientService.sendMoneyToClient(sender,
//                        req.getParameter("nameTo"),
//                        Long.parseLong(req.getParameter("count")));
//                parameters.put("message", "The transaction was successful");
//                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", parameters));
//                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                parameters.put("message", "transaction rejected");
                resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", parameters));
                resp.setStatus(HttpServletResponse.SC_OK);
//                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                System.out.println("Transaction abort!");
            }
        } catch (DBException | NullPointerException e) {
            parameters.put("message", "transaction rejected");
            resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", parameters));
            resp.setStatus(HttpServletResponse.SC_OK);
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            e.printStackTrace();
            System.out.println("Transaction abort!");
        }
        resp.setContentType("text/html;charset=utf-8");
    }
}
