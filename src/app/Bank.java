package app;
import entity.*;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Account> accounts;
    private static long AccNo;
    
    public Bank() {
        this.accounts = new ArrayList<>();
        this.AccNo = 1001; 
    }

    public Account createAccount(Customer customer, String accType, float balance) {
        Account account = new Account(AccNo++, accType, balance, customer);
        accounts.add(account);
        return account;
    }

    public float getAccountBalance(long accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc.getAccountBalance();
            }
        }
        throw new IllegalArgumentException("Account not found.");
    }

    public float deposit(long accountNumber, float amount) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber && amount >0) {
                acc.setAccountBalance(acc.getAccountBalance() + amount);
                return acc.getAccountBalance();
            }
        }
        throw new IllegalArgumentException("Account not found or Invalid amount.");
    }

    public float withdraw(long accountNumber, float amount) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber && amount > 0) {
                float current = acc.getAccountBalance();
                if (current >= amount) {
                    acc.setAccountBalance(current - amount);
                    return acc.getAccountBalance();
                } else {
                    throw new IllegalArgumentException("Insufficient balance.");
                }
            }
        }
        throw new IllegalArgumentException("Account not found or Invalid amount.");
    }

    public void transfer(long fromAccNo, int toAccNo, float amount) {
        Account from = null, to = null;

        for (Account acc : accounts) {
            if (acc.getAccountNumber() == fromAccNo) from = acc;
            if (acc.getAccountNumber() == toAccNo) to = acc;
        }
        if (from == null || to == null) {
        	throw new IllegalArgumentException("Invalid account number(s).");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }
        if (from.getAccountBalance() < amount) {
            throw new IllegalArgumentException("Insufficient funds for transfer.");
        }

        from.setAccountBalance(from.getAccountBalance() - amount);
        to.setAccountBalance(to.getAccountBalance() + amount);
        System.out.println("Money Transfer successful.");
    }

    public void getAccountDetails(long accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                acc.accountDetails();
                return;
            }
        }
        System.out.println("Account not found.");
    }
}
