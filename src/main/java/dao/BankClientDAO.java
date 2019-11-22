package dao;

//import com.sun.deploy.util.SessionState;
import model.BankClient;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() {
        try (Statement stmt = connection.createStatement()) {
            List<BankClient> list = new ArrayList<>();
            stmt.execute("select * from bank_client");
            ResultSet result = stmt.getResultSet();
            do {
                BankClient bc = new BankClient(result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getLong(4));
                list.add(bc);
            } while (!result.isLast());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean validateClient(String name, String password) {
        return false;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) {

    }

    public BankClient getClientById(long id) throws SQLException {
        return null;
    }

    public boolean isClientHasSum(String name, Long expectedSum) {
        return false;
    }

    public long getClientIdByName(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("select * from bank_clien where name='" + name + "'");
        ResultSet result = stmt.getResultSet();
        result.next();
        Long id = result.getLong(1);
        result.close();
        stmt.close();
        return id;
    }

    public BankClient getClientByName(String name) {
        return null;
    }

    public void addClient(BankClient client) {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("nsert into bank_client values(" +
//                    client.getId() + ", " +
                    client.getName() + ", " +
                    client.getPassword() + ", " +
                    client.getMoney() +")");
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
