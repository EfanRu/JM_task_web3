package dao;

//import com.sun.deploy.util.SessionState;
import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {
    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws DBException {
        List<BankClient> list = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("select * from bank_client");
            ResultSet result = stmt.getResultSet();
            while (result.next()){
                BankClient bc = new BankClient(result.getLong(1),
                        result.getString(2),
                        result.getString(3),
                        result.getLong(4));
                list.add(bc);
            }
        } catch (SQLException e) {
            throw new DBException(e);
        }
        return list;
    }

    public boolean validateClient(String name, String password) throws DBException {
        return getClientByName(name).getPassword().equals(password);
    }

    public void updateClientsMoney(String name, String password, Long transactValue) throws DBException {
        BankClient bc = getClientByName(name);
        if (bc.getPassword().equals(password) && (bc.getMoney() + transactValue) >= 0) {
            String sql = "update bank_client set money=money + ? where id=? and name=? and password=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setLong(1, transactValue);
                pstmt.setLong(2, bc.getId());
                pstmt.setString(3, name);
                pstmt.setString(4, password);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                throw new DBException(e);
            }
        } else {
            System.out.println("Not enought money at user " + bc.getName() + " Id: " + bc.getId());
            throw new DBException(new Throwable());
        }
    }

    public boolean sendMoney(BankClient sender, String name, Long value) throws DBException {
        try {
            if (validateClient(sender.getName(), sender.getPassword())
                && isClientHasSum(sender.getName(), value)) {
                connection.setAutoCommit(false);
                updateClientsMoney(sender.getName(), sender.getPassword(), -1*value);
                BankClient bc = getClientByName(name);
                updateClientsMoney(bc.getName(), bc.getPassword(), value);
                connection.commit();
                connection.setAutoCommit(true);
                return true;
            } else {
                return false;
            }
        } catch (SQLException | NullPointerException e) {
            throw new DBException(e);
        }
    }

    public BankClient getClientById(long id) throws DBException {
        String sql = "select * from bank_client where id='?'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeQuery();
            ResultSet result = pstmt.getResultSet();
            result.next();
            result.close();
            return new BankClient(result.getLong(1),
                    result.getString(2),
                    result.getString(3),
                    result.getLong(4));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws DBException {
        return getClientByName(name).getMoney() >= expectedSum;
    }

    public long getClientIdByName(String name) throws DBException {
        return getClientByName(name).getId();
    }

    public BankClient getClientByName(String name) throws DBException {
        String sql = "select * from bank_client where name=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeQuery();
            ResultSet result = pstmt.getResultSet();
            result.next();
            return new BankClient(result.getLong(1),
                    result.getString(2),
                    result.getString(3),
                    result.getLong(4));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean addClient(BankClient client) throws DBException {

        for (BankClient bc : getAllBankClient()) {
            if (bc.getName().equals(client.getName())) {
                System.out.println(bc.getName()
                + " equals "
                + client.getName()
                + " ?");
                System.out.println("Exception in check double client");
                return false;
            }
        }

        String sql = "insert into bank_client values(?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, client.getId());
            pstmt.setString(2, client.getName());
            pstmt.setString(3, client.getPassword());
            pstmt.setLong(4, client.getMoney());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void deleteClient(BankClient client) throws DBException {
        String sql = "delete from bank_client where id='?' name='?' password='?'";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, client.getId());
            pstmt.setString(2, client.getName());
            pstmt.setString(3, client.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
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
