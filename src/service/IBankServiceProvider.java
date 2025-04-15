package service;
import java.sql.SQLException;

import entity.Account;
import entity.Customer;
import exception.*;

public interface IBankServiceProvider {
	
    Account createAccount(Customer customer, String accType, float balance, float interestRate)  throws InvalidAmountException, InvalidAccountException;
    Account createAccount(Customer customer, String accType, float balance)  throws InvalidAmountException, InvalidAccountException;
    Account[] listAccounts();
	float calculateInterest(long accNo, float interestRate) throws SQLException, InvalidAccountException; 
}