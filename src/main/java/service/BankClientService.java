package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) throws DBException {
        return getBankClientDAO().getClientById(id);
    }

    public BankClient getClientByName(String name) {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (DBException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BankClient> getAllClient() {
        return  getBankClientDAO().getAllBankClient();
    }

    public boolean deleteClient(String name) throws DBException {
        BankClientDAO dao = getBankClientDAO();
        BankClient bc = dao.getClientByName(name);
        dao.deleteClient(bc);
        return true;
    }

    public boolean addClient(BankClient client) {
        try {
//            getBankClientDAO().addClient(client);
            return getBankClientDAO().addClient(client);
        } catch (DBException e) {
//            e.printStackTrace();
            return false;
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException {
        return getBankClientDAO().sendMoney(sender, name, value);
    }

    public void cleanUp() throws DBException {
        //default method
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        //default method
        BankClientDAO dao = getBankClientDAO();
        try {
            getBankClientDAO().createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        //default method
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    //For testing
//                    append("db_example?").          //db name
                    append("test?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
