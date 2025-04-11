package dao;
import entity.Account;
import entity.Customer;
import entity.Transaction;
import exception.*;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;

public interface IBankRepository {
    Account createAccount(Customer customer, long accNo, String accType, float balance) throws SQLException, InvalidAmountException;
    List<Account> listAccounts() throws SQLException;
    float calculateInterest() throws SQLException;
    float getAccountBalance(long accountNumber) throws SQLException, InvalidAccountException;
    float deposit(long accountNumber, float amount) throws SQLException, InvalidAccountException, InvalidAmountException;
    float withdraw(long accountNumber, float amount) throws SQLException, InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException;
    boolean transfer(long fromAccount, long toAccount, float amount) throws SQLException, InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException;
    Account getAccountDetails(long accountNumber) throws SQLException, InvalidAccountException;
    List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) throws SQLException, InvalidAccountException;
}
