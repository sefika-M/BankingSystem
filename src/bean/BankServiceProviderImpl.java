package bean;
import entity.*;
import exception.*;
import service.IBankServiceProvider;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BankServiceProviderImpl extends CustomerServiceProviderImpl implements IBankServiceProvider {
    private String branchName;
    private String branchAddress;

    public BankServiceProviderImpl(String branchName, String branchAddress) {
    	  super(); 
          this.branchName = branchName;
          this.branchAddress = branchAddress;
    }

    @Override
    public Account createAccount(Customer customer, String accType, float balance, float interestRate)  throws IllegalArgumentException, InvalidAmountException {
        Account acc = null;
        switch (accType.toLowerCase()) {
            case "savings":
                acc = new SavingsAccount(balance, customer, interestRate); 
                break;
                
            case "zerobalance":
                acc = new ZeroBalanceAccount(customer);
                break;
            default:
                System.out.println("Invalid account type.");
        }
        if (acc != null) {
        	accountList.add(acc);
            accountMap.put(acc.getAccountNumber(), acc);
            System.out.println("Account created successfully.");
        }
        return acc;
    }
    @Override
    public Account createAccount(Customer customer, String accType, float balance) throws IllegalArgumentException, InvalidAmountException {
        Account acc = null;
        if ("current".equalsIgnoreCase(accType)) {
            acc = new CurrentAccount(balance, customer);
        } else if ("zerobalance".equalsIgnoreCase(accType)) {
            acc = new ZeroBalanceAccount(customer);
        } else {
            throw new IllegalArgumentException("Invalid account type.");
        }
        if (acc != null) {
        	accountList.add(acc);
            accountMap.put(acc.getAccountNumber(), acc);
            System.out.println("Account created successfully.");
        }
        return acc;
    }
    
    @Override
    public Account[] listAccounts() {
    	 accountList.sort(Comparator.comparing(a -> a.getCustomer().getFirstName().toLowerCase()));
         return accountList.toArray(new Account[0]);
    }

    @Override
    public void calculateInterest() {
        boolean found = false;
        for (Account acc : accountList) {
            if (acc instanceof SavingsAccount) {
                found = true;
                System.out.println("\nApplying interest to Account Number: " + acc.getAccountNumber());
                ((SavingsAccount) acc).calculateInterest();
                System.out.println("Updated Balance: " + acc.getAccountBalance());
                System.out.println("--------------------------------------");
            }
        }
        if (!found) {
            System.out.println("No savings accounts found.");
        } else {
            System.out.println("Interest calculation completed for your Savings Accounts.");
        }
    }
    public List<Account> getAccountList() {
        return accountList;
    }

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

}
