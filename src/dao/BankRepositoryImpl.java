package dao;
import entity.*;
import exception.*;
import util.DBConnUtil;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BankRepositoryImpl implements IBankRepository {

    @Override
    public Account createAccount(Customer customer, long accNo, String accType, float balance) throws SQLException {
        Connection conn = DBConnUtil.getDbConnection();
        String custSql = "INSERT INTO Customers (customer_id, first_name, last_name, DOB, email, phone_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String accSql = "INSERT INTO Accounts (customer_id, account_type, balance) VALUES (?, ?, ?)";
        
        try (Connection con = DBConnUtil.getDbConnection()) {
            con.setAutoCommit(false);
        try (PreparedStatement custStmt = conn.prepareStatement(custSql);
             PreparedStatement accStmt = conn.prepareStatement(accSql, Statement.RETURN_GENERATED_KEYS)) {

            custStmt.setInt(1, customer.getCustomerID());
            custStmt.setString(2, customer.getFirstName());
            custStmt.setString(3, customer.getLastName());
            custStmt.setDate(4, customer.getDob());
            custStmt.setString(4, customer.getEmailAddress());
            custStmt.setString(5, customer.getPhoneNumber());
            custStmt.setString(6, customer.getAddress());
            custStmt.executeUpdate();

            accStmt.setInt(1, customer.getCustomerID());
            accStmt.setString(2, accType);
            accStmt.setFloat(3, balance);
            accStmt.executeUpdate();
            try 
            (ResultSet rs = accStmt.getGeneratedKeys()){
            if (rs.next()) {
                long accId = rs.getLong(1);
                System.out.println("New account created with Account ID: " + accId);
            }
            }
          

            con.commit();
            return new Account(accNo, accType, balance, customer);
            
        } catch (SQLException e) {
            con.rollback();
            throw e;
        }
        }finally {
            conn.close();
        }
    }
        

    @Override
    public float getAccountBalance(long accNo) throws SQLException {
        String query = "SELECT balance FROM Accounts WHERE account_id = ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, accNo);
            try
            (ResultSet rs = ps.executeQuery()){
            if (rs.next()) return rs.getFloat("balance");
            else throw new SQLException("Account not found with ID: " + accNo);
            }
        }
        
    }

    @Override
    public float deposit(long accNo, float amount) throws SQLException {
        if (amount <= 0) throw new SQLException("Invalid deposit amount.");
      
        String update = "UPDATE Accounts SET balance = balance + ? WHERE account_id = ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setFloat(1, amount);
            ps.setLong(2, accNo);
            int updated = ps.executeUpdate();
            if (updated == 1) return getAccountBalance(accNo);
            else throw new SQLException("Deposit failed.");        
        }
    }

    @Override
    public float withdraw(long accountNumber, float amount) throws SQLException {
        Connection conn = null;
        PreparedStatement getPs = null;
        PreparedStatement updatePs = null;
        ResultSet rs = null;

        try {
            conn = DBConnUtil.getDbConnection();
            conn.setAutoCommit(false); // start transaction

            String getQuery = "SELECT account_type, balance FROM Accounts WHERE account_id = ?";
            getPs = conn.prepareStatement(getQuery);
            getPs.setLong(1, accountNumber);
            rs = getPs.executeQuery();

            if (rs.next()) {
                String accType = rs.getString("account_type");
                float currentBalance = rs.getFloat("balance");

                float newBalance = currentBalance - amount;
                boolean canWithdraw = false;

                switch (accType.toLowerCase()) {
                    case "savings":
                        canWithdraw = newBalance >= 500;
                        break;
                    case "current":
                        canWithdraw = newBalance >= -5000;
                        break;
                    case "zerobalance":
                        canWithdraw = newBalance >= 0;
                        break;
                    default:
                        throw new SQLException("Unknown account type.");
                }

                if (!canWithdraw) {
                    throw new SQLException("Withdrawal violates balance policy.");
                }

                String updateQuery = "UPDATE Accounts SET balance = ? WHERE account_id = ?";
                updatePs = conn.prepareStatement(updateQuery);
                updatePs.setFloat(1, newBalance);
                updatePs.setLong(2, accountNumber);
                updatePs.executeUpdate();

                conn.commit();
                return newBalance;
            } else {
                throw new SQLException("Account not found.");
            }

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (getPs != null) getPs.close();
            if (updatePs != null) updatePs.close();
            if (conn != null) conn.close();
        }
    }


    @Override
    public boolean transfer(long fromAcc, long toAcc, float amount) throws SQLException {
    	try (Connection con = DBConnUtil.getDbConnection()) {
            con.setAutoCommit(false);
            try {
                float fromBal = getAccountBalance(fromAcc);
                if (amount > fromBal) throw new SQLException("Insufficient balance.");
                withdraw(fromAcc, amount);
                deposit(toAcc, amount);
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    @Override
    public Account getAccountDetails(long accNo) throws SQLException {
        String sql = "SELECT a.account_id, a.account_type, a.balance, c.customer_id, c.first_name, c.last_name, c.email, c.phone_number, c.address FROM Accounts a JOIN Customers c ON a.customer_id = c.customer_id WHERE a.account_id = ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, accNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer(
                		rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("dob"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address")
                );
                Account acc = new Account(
                        rs.getString("account_type"),
                        rs.getFloat("balance"),
                        customer
                    );
                    acc.setAccountNumber(rs.getLong("account_id"));
                    return acc;
                } else throw new SQLException("Account not found.");
        }
    }

    

            
        


    @Override
    public List<Account> listAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT a.account_id, a.account_type, a.balance, c.customer_id, c.first_name, c.last_name, c.email, c.phone_number, c.address FROM Accounts a JOIN Customers c ON a.customer_id = c.customer_id";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                		rs.getInt("customer_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("dob"),
                        rs.getString("email"),
                        rs.getString("phone_number"),
                        rs.getString("address")
                );
                Account acc = new Account(
                		 rs.getString("account_type"),
                         rs.getFloat("balance"),
                    c
                );
                acc.setAccountNumber(rs.getLong("account_id"));
                accounts.add(acc);
            }
        }
        return accounts;
    }

    @Override
    public float calculateInterest() throws SQLException {
        String sql = "UPDATE Accounts SET balance = balance + (balance * 0.045) WHERE account_type = 'savings'";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int updated = ps.executeUpdate();
            return updated;
        }
    }

    @Override
    public List<Transaction> getTransactions(long accNo, Date from, Date to) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE account_id = ? AND transaction_date BETWEEN ? AND ?";
        try (Connection conn = DBConnUtil.getDbConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accNo);
            ps.setTimestamp(2, new java.sql.Timestamp(from.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(to.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Transaction txn = new Transaction();
                txn.setTransactionId(rs.getInt("transaction_id"));
                txn.setTransactionType(rs.getString("transaction_type"));
                txn.setAmount(rs.getFloat("amount"));
                txn.setDate(new java.util.Date(rs.getTimestamp("transaction_date").getTime()));
                result.add(txn);
            }
        }
        return result;
    }
}
