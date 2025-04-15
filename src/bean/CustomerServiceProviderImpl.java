package bean;
import exception.*;
import entity.Account;
import entity.CurrentAccount;
import entity.SavingsAccount;
import entity.Transaction;
import service.ICustomerServiceProvider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import dao.*;

public class CustomerServiceProviderImpl implements ICustomerServiceProvider {
    protected Map<Long, Account> accountMap;
    protected List<Account> accountList;
    protected List<Transaction> transactionList;
    private IBankRepository bankRepo;


    public CustomerServiceProviderImpl() {
        this.accountMap = new HashMap<>();
        this.accountList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
        this.bankRepo = new BankRepositoryImpl();  

    }
    
    public Map<Long, Account> getAccountMap() {
        return accountMap;
    }
    public List<Account> getAccountList() {
        return accountList;
    }
    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    @Override
    public float getAccountBalance(long accountNumber) throws InvalidAccountException, SQLException {
        return bankRepo.getAccountBalance(accountNumber);  

    }

    @Override
    public float deposit(long accountNumber, float amount) throws InvalidAccountException, InvalidAmountException, SQLException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive.");
        return bankRepo.deposit(accountNumber, amount);
 
    }

    @Override
    public float withdraw(long accountNumber, float amount) throws InvalidAccountException, InvalidAmountException, InsufficientFundException, OverDraftLimitExceededException, SQLException {
    	if (amount <= 0)
            throw new InvalidAmountException("Withdraw amount must be positive.");
        return bankRepo.withdraw(accountNumber, amount);
    }

    @Override
    public boolean transfer(long fromAccNo, long toAccNo, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException, SQLException {
    	if (amount <= 0)
            throw new InvalidAmountException("Amount must be greater than 0.");
        return bankRepo.transfer(fromAccNo, toAccNo, amount);
    }

    @Override
    public Account getAccountDetails(long accountNumber) throws InvalidAccountException, SQLException{
        return bankRepo.getAccountDetails(accountNumber);
        }
    
    
    @Override
    public List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) throws InvalidAccountException, SQLException {
        return bankRepo.getTransactions(accountNumber, fromDate, toDate);
    }

}

         
    

        