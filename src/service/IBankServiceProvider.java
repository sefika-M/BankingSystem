package service;
import entity.Account;
import entity.Customer;
import exception.*;

public interface IBankServiceProvider {
    Account createAccount(Customer customer, String accType, float balance, float interestRate)  throws InvalidAmountException;
    Account createAccount(Customer customer, String accType, float balance)  throws InvalidAmountException;
    Account[] listAccounts();
    void calculateInterest(); 
}