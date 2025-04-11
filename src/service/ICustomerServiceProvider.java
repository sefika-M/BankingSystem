package service;
import java.util.Date;
import java.util.List;
import entity.Transaction;
import exception.*;

public interface ICustomerServiceProvider {
	float getAccountBalance(long accountNumber) throws InvalidAccountException;
    float deposit(long accountNumber, float amount) throws InvalidAccountException, InvalidAmountException;
    float withdraw(long accountNumber, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException;
    boolean transfer(long fromAccountNumber, long toAccountNumber, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException;
    void getAccountDetails(long accountNumber) throws InvalidAccountException;
    List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) throws InvalidAccountException;

}



