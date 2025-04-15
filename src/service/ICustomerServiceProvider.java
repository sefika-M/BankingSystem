package service;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

import entity.Account;
import entity.Transaction;
import exception.*;

public interface ICustomerServiceProvider {
	float getAccountBalance(long accountNumber) throws InvalidAccountException, SQLException;
    float deposit(long accountNumber, float amount) throws InvalidAccountException, InvalidAmountException, SQLException;
    float withdraw(long accountNumber, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException, SQLException;
    boolean transfer(long fromAccountNumber, long toAccountNumber, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException, SQLException;
    Account getAccountDetails(long accountNumber) throws InvalidAccountException, SQLException;
    List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) throws InvalidAccountException, SQLException;

}



