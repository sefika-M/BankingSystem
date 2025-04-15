package bean;
import entity.*;
import exception.*;
import service.IBankServiceProvider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import dao.BankRepositoryImpl;
import dao.IBankRepository;

public class BankServiceProviderImpl extends CustomerServiceProviderImpl implements IBankServiceProvider {
    private String branchName;
    private String branchAddress;
    private List<Customer> customers; 
    private IBankRepository bankRepo;



    public BankServiceProviderImpl(String branchName, String branchAddress) {
    	  super(); 
          this.branchName = branchName;
          this.branchAddress = branchAddress;
          this.customers = new ArrayList<>();
          this.bankRepo = new BankRepositoryImpl(); 
    }

    
    public boolean addCustomer(Customer customer) {
    	try {
            Customer dbCustomer = bankRepo.addCustomer(customer);
            customers.add(dbCustomer);
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
            return false;
        }
    }

    public Customer getCustomerById(int id) {
    	try {
            return bankRepo.getCustomerById(id);
        } catch (SQLException e) {
            System.err.println("Error retrieving customer: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Account createAccount(Customer customer, String accType, float balance, float interestRate)  throws IllegalArgumentException, InvalidAmountException , InvalidAccountException{
    	if (balance < 0) { 
            throw new InvalidAmountException("Balance cannot be negative.");
        }
        accType = accType.trim().toLowerCase();
        
        // Validate account type
        if (!accType.equals("savings") && !accType.equals("current") && !accType.equals("zero_balance")) {
            throw new InvalidAccountException("Invalid account type. Allowed types are: savings, current, zero_balance.");
        }try {
            return bankRepo.createAccount(customer, 0, accType, balance, interestRate); 
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            throw new InvalidAccountException("Account creation failed.");
        }
    }
    
    
    @Override
    public Account createAccount(Customer customer, String accType, float balance) throws IllegalArgumentException, InvalidAmountException , InvalidAccountException{
    	if (balance < 0) { 
            throw new InvalidAmountException("Balance cannot be negative.");
        }
        accType = accType.trim().toLowerCase();
        
        // Validate account type
        if (!accType.equals("savings") && !accType.equals("current") && !accType.equals("zero_balance")) {
            throw new InvalidAccountException("Invalid account type. Allowed types are: savings, current, zero_balance.");
        }try {
            return bankRepo.createAccount(customer, 0, accType, balance); 
        } catch (SQLException e) {
            System.err.println("Error creating account: " + e.getMessage());
            throw new InvalidAccountException("Account creation failed.");
        }
    }
    
    @Override
    public Account[] listAccounts() {
    	 try {
    	        List<Account> dbAccounts = bankRepo.listAccounts();
    	        if (dbAccounts.isEmpty()) {
                    System.out.println("No accounts found.");
                } else {
    	        dbAccounts.sort(Comparator.comparing(a -> a.getCustomer().getFirstName().toLowerCase()));
                }return dbAccounts.toArray(new Account[0]);
    	    } catch (SQLException e) {
                System.err.println("Error listing accounts: " + e.getMessage());
    	        return new Account[0];
    	    }
    	}

    @Override
    public float calculateInterest(long accNo, float interestRate) throws SQLException, InvalidAccountException {
            return bankRepo.calculateInterest(accNo, interestRate);
       
    }

    
    public List<Account> getAccountList() {
    	try {
    		List<Account> accounts = new ArrayList<>();
            for (Customer customer : customers) {
                accounts.addAll(customer.getAccounts()); 
            }
            return accounts;
        } catch (Exception e) {
            System.err.println("Error retrieving account list: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    

    public List<Transaction> getTransactionList() {
         return transactionList;
    }

}
