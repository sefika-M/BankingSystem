package dao;
import entity.*;
import exception.*;
import util.DBConnUtil;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class BankRepositoryImpl implements IBankRepository {
    private Connection conn;
    public BankRepositoryImpl() {
        this.conn = DBConnUtil.getDbConnection();  
    }

	@Override
	public Customer addCustomer(Customer customer) throws SQLException {
	    String sql = "INSERT INTO Customers (first_name, last_name, DOB, email, phone_number, address) VALUES (?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

	        ps.setString(1, customer.getFirstName());
	        ps.setString(2, customer.getLastName());
	        ps.setDate(3, customer.getDob());
	        ps.setString(4, customer.getEmailAddress());
	        ps.setString(5, customer.getPhoneNumber());
	        ps.setString(6, customer.getAddress());

	        int affectedRows = ps.executeUpdate();

	        if (affectedRows == 0) {
	            throw new SQLException("Creating customer failed, no rows affected.");
	        }

	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                customer.setCustomerID(rs.getInt(1));  
	            } else {
	                throw new SQLException("Creating customer failed, no ID obtained.");
	            }
	        }

	        return customer;
	    }
	}
	
	@Override
	public Customer getCustomerById(int customerId) throws SQLException {
	    String sql = "SELECT * FROM Customers WHERE customer_id = ?";
	    try ( PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, customerId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return new Customer(
	                    rs.getInt("customer_id"),
	                    rs.getString("first_name"),
	                    rs.getString("last_name"),
	                    rs.getDate("DOB"),
	                    rs.getString("email"),
	                    rs.getString("phone_number"),
	                    rs.getString("address")
	            );
	        } else {
	            throw new SQLException("Customer with ID " + customerId + " not found.");
	        }
	    }
	}


    @Override
    public Account createAccount(Customer customer, long accNo, String accType, float balance,  float rate)  throws SQLException {
    	 Connection conn = null;
    	    try {
    	        conn = DBConnUtil.getDbConnection(); 
    	        conn.setAutoCommit(false); 
    	        accType = accType.trim().toLowerCase();
         if (accType.equalsIgnoreCase("savings")) {
            if (balance < 500) {
                throw new IllegalArgumentException("Minimum balance for Savings Account is 500.");
            }}
             String accSql = "INSERT INTO Accounts (customer_id, account_type, balance) VALUES (?, ?, ?)";
             try (PreparedStatement accStmt = conn.prepareStatement(accSql, Statement.RETURN_GENERATED_KEYS)) {
            	 accStmt.setInt(1, customer.getCustomerID());
                 accStmt.setString(2, accType);
                 accStmt.setFloat(3, balance);
                 accStmt.executeUpdate();
                 
                 try (ResultSet rs = accStmt.getGeneratedKeys()) {
                     if (rs.next()) {
                         long accId = rs.getLong(1);
                         System.out.println("New account created with Account ID: " + accId);
                         Account newAccount = new Account(accId, accType, balance, customer);
                         return newAccount;
                         }
                     }
                     }
                 
        catch (SQLException e) {
            conn.rollback(); 
            throw e;
        }  } catch (SQLException e) {
            throw new SQLException("Error creating account: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); 
            }
        }
        return null;    
}
    
        
    @Override
    public Account createAccount(Customer customer, long accNo, String accType, float balance) throws SQLException {
    	 Connection conn = null;
    	    try {
    	        conn = DBConnUtil.getDbConnection(); 
    	        conn.setAutoCommit(false); 
    	        accType = accType.trim().toLowerCase();
        if (accType.equalsIgnoreCase("savings")) {
            if (balance < 500) {
                throw new IllegalArgumentException("Minimum balance for Savings Account is 500.");
            }}
        String accSql = "INSERT INTO Accounts (customer_id, account_type, balance) VALUES (?, ?, ?)";
            try (PreparedStatement accStmt = conn.prepareStatement(accSql, Statement.RETURN_GENERATED_KEYS)) {
                accStmt.setInt(1, customer.getCustomerID());
                accStmt.setString(2, accType);
                accStmt.setFloat(3, balance);
                accStmt.executeUpdate();

                try (ResultSet rs = accStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        long accId = rs.getLong(1);
                        System.out.println("New account created with Account ID: " + accId);
                        conn.commit();
                        Account acc = new Account(accType, balance, customer);
                        acc.setAccountNumber(accId);
                        return acc;
                    } else {
                        throw new SQLException("Failed to retrieve generated account ID.");
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); 
               throw e;
            }
    	    }catch (SQLException e) {
        throw new SQLException("Error creating account: " + e.getMessage(), e);
    }finally {
        if (conn != null) {
            conn.setAutoCommit(true); 
        }
    }  
}        
    

    
    
    @Override
    public float getAccountBalance(long accNo) throws SQLException {
        String query = "SELECT balance FROM Accounts WHERE account_id = ?";
        try ( PreparedStatement ps = conn.prepareStatement(query)) {
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
        try  (PreparedStatement ps = conn.prepareStatement(update)) {
            ps.setFloat(1, amount);
            ps.setLong(2, accNo);
            int updated = ps.executeUpdate();
            if (updated == 1) {
            	String txn = "INSERT INTO transactions (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
            try (PreparedStatement txnPs = conn.prepareStatement(txn)) {
                txnPs.setLong(1, accNo);
                txnPs.setString(2, "deposit");
                txnPs.setFloat(3, amount);
                txnPs.executeUpdate();
            }return getAccountBalance(accNo);
            }else {
            	throw new SQLException("Deposit failed.");        
            }
        }
    }

    @Override
    public float withdraw(long accountNumber, float amount) throws SQLException, InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {
        PreparedStatement getPs = null;
        PreparedStatement updatePs = null;
        PreparedStatement txnPs = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnUtil.getDbConnection(); 
            conn.setAutoCommit(false);
        
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
                
                String txnQuery = "INSERT INTO transactions (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
                txnPs = conn.prepareStatement(txnQuery);
                txnPs.setLong(1, accountNumber);
                txnPs.setString(2, "withdrawal");
                txnPs.setFloat(3, amount);
                txnPs.executeUpdate();

                conn.commit();
                return newBalance;
            } else {
                throw new SQLException("Account not found.");
            }

        } catch (SQLException e) {
            if (conn != null) {
            	conn.rollback();
        }
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (getPs != null) getPs.close();
            if (updatePs != null) updatePs.close();
            if (txnPs != null) txnPs.close();
            if (conn != null) {
                conn.setAutoCommit(true); 
        }
    }
}

    public void updateAccountBalanceInDB(Account account) {
        String updateSQL = "UPDATE Accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
            stmt.setFloat(1, account.getAccountBalance());
            stmt.setLong(2, account.getAccountNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean transfer(long fromAcc, long toAcc, float amount) throws SQLException, InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {
            conn.setAutoCommit(false);
            if (conn == null || conn.isClosed()) {
                throw new SQLException("Database connection is not available.");
            }
            try {
                float fromBal = getAccountBalance(fromAcc);
                if (amount > fromBal) throw new SQLException("Insufficient balance.");
                withdraw(fromAcc, amount);
                deposit(toAcc, amount);
                String txnQuery = "INSERT INTO transactions (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
                try (PreparedStatement txnPs = conn.prepareStatement(txnQuery)) {
                    txnPs.setLong(1, fromAcc);
                    txnPs.setString(2, "transfer");
                    txnPs.setFloat(3, amount);
                    txnPs.executeUpdate();
                    
                    txnPs.setLong(1, toAcc);
                    txnPs.setString(2, "transfer");
                    txnPs.setFloat(3, amount);
                    txnPs.executeUpdate();
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                System.err.println("SQL Error: " + e.getMessage());
                conn.rollback();
                throw e;
            }finally {
                if (conn != null) conn.setAutoCommit(true);
            }
        }
    

    @Override
    public Account getAccountDetails(long accNo) throws SQLException {
    	
        String sql = "SELECT a.account_id, a.account_type, a.balance, c.customer_id, c.first_name, c.last_name, c.dob, c.email, c.phone_number, c.address FROM Accounts a JOIN Customers c ON a.customer_id = c.customer_id WHERE a.account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
        String sql = "SELECT a.account_id, a.account_type, a.balance, c.customer_id, c.first_name, c.last_name, c.dob, c.email, c.phone_number, c.address FROM Accounts a JOIN Customers c ON a.customer_id = c.customer_id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
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
    public float calculateInterest(long accountNumber, float interestRate) throws SQLException, InvalidAccountException {
        String sql = "SELECT balance FROM Accounts WHERE account_id = ? AND account_type = 'savings'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setLong(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                float balance = rs.getFloat("balance");
                float interest = balance * (interestRate / 100);
                float updatedBalance = balance + interest;
                String update = "UPDATE Accounts SET balance = ? WHERE account_id = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(update)) {
                    updatePs.setFloat(1, updatedBalance);
                    updatePs.setLong(2, accountNumber);
                    updatePs.executeUpdate();
                }

                return updatedBalance;
            } else {
                throw new InvalidAccountException("Savings account not found for ID: " + accountNumber);
            }
        }
    }

    
    @Override
    public List<Transaction> getTransactions(long accountId, java.sql.Date fromDate, java.sql.Date toDate) throws SQLException {
        List<Transaction> result = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE account_id = ? AND transaction_date BETWEEN ? AND ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, accountId);
            ps.setDate(2, fromDate);
            ps.setDate(3, toDate);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	 int transactionId = rs.getInt("transaction_id");
                 String transactionType = rs.getString("transaction_type");
                 float amount = rs.getFloat("amount"); 
                 Timestamp transactionDate = rs.getTimestamp("transaction_date");
                 Account account = getAccountById(accountId); 
                Transaction txn = new Transaction(transactionId, account, transactionType, amount, transactionDate);      
                result.add(txn);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching transactions.");
            e.printStackTrace();
        }
        return result; 
}
        
 

		@Override
		public Account getAccountById(long accountId) throws SQLException {
			 String query = "SELECT * FROM Accounts WHERE account_id = ?";
	            String customerQuery = "SELECT * FROM Customers WHERE customer_id = (SELECT customer_id FROM Accounts WHERE account_id = ?)";
	            try (PreparedStatement accountPs = conn.prepareStatement(query);
	                    PreparedStatement customerPs = conn.prepareStatement(customerQuery)) {
	                   accountPs.setLong(1, accountId);
	                   ResultSet accountRs = accountPs.executeQuery();
	                   
	                   if (accountRs.next()) {
	                       long accountNumber = accountRs.getLong("account_id");
	                       String accountType = accountRs.getString("account_type");
	                       float accountBalance = accountRs.getFloat("balance");
	                       
	                       customerPs.setLong(1, accountId);
	                       ResultSet customerRs = customerPs.executeQuery();
	                       
	                       if (customerRs.next()) {
	                           int customerId = customerRs.getInt("customer_id");
	                           String firstName = customerRs.getString("first_name");
	                           String lastName = customerRs.getString("last_name");
	                           Customer customer = new Customer(customerId, firstName, lastName);
	                           Account account = new Account(accountNumber, accountType, accountBalance, customer);
	                           return account;
	                       }
	                   }
	               }
	               return null; 
		}
}
