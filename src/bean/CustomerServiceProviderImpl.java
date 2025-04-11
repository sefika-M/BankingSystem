package bean;
import exception.*;
import entity.Account;
import entity.CurrentAccount;
import entity.SavingsAccount;
import entity.Transaction;
import service.ICustomerServiceProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceProviderImpl implements ICustomerServiceProvider {
    protected Map<Long, Account> accountMap;
    protected List<Account> accountList;
    protected List<Transaction> transactionList;

    public CustomerServiceProviderImpl() {
        this.accountMap = new HashMap<>();
        this.accountList = new ArrayList<>();
        this.transactionList = new ArrayList<>();
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
    public float getAccountBalance(long accountNumber) throws InvalidAccountException {
    	Account acc = accountMap.get(accountNumber);
        if (acc != null) {
        	return acc.getAccountBalance();
            }
        throw new IllegalArgumentException("Account not found.");
    }

    @Override
    public float deposit(long accountNumber, float amount) throws InvalidAccountException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Deposit amount must be positive.");
        Account acc = accountMap.get(accountNumber);
        if (acc != null) {
                acc.deposit(amount);
                transactionList.add(new Transaction(acc, "Deposit", new Date(), "Deposit", amount));
                return acc.getAccountBalance();
            }
        throw new IllegalArgumentException("Account not found or Invalid amount.");
    }

    @Override
    public float withdraw(long accountNumber, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {
    	Account acc = accountMap.get(accountNumber);
        if (acc == null) throw new InvalidAccountException("Account not found.");
            	if (acc instanceof CurrentAccount current) {
            		float limit = current.getAccountBalance() + CurrentAccount.OVERDRAFT_LIMIT;
                    if (amount > limit) {
                        throw new OverDraftLimitExceededException("Amount exceeds overdraft limit.");
                    }
            	}  else if (acc instanceof SavingsAccount) {
            		 float balAfter = acc.getAccountBalance() - amount;
                     if (balAfter < 500) {
                         throw new InsufficientFundException("Minimum 500 balance must be maintained.");
                     }
            	}else {
                    if (acc.getAccountBalance() < amount) {
                        throw new InsufficientFundException("Insufficient balance.");
                    }
                }
            	boolean success = acc.withdraw(amount);
                if (success) {
                transactionList.add(new Transaction(acc, "Withdrawal", new Date(), "Withdraw", amount));
                return acc.getAccountBalance();
                } else {
                    return -1; 
                }
    }

    @Override
    public boolean transfer(long fromAccNo, long toAccNo, float amount) throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException, InvalidAmountException {
        if (amount <= 0) throw new InvalidAmountException("Amount must be greater than 0.");
        Account from = accountMap.get(fromAccNo);
        Account to = accountMap.get(toAccNo);
        if (from == null || to == null) throw new InvalidAccountException("One or both accounts not found.");
        if (from.getAccountBalance() < amount) throw new InsufficientFundException("Insufficient funds for transfer.");
        from.withdraw(amount);
        to.deposit(amount);
        transactionList.add(new Transaction(from, "Transfer to " + toAccNo, new Date(), "Transfer", amount));
        transactionList.add(new Transaction(to, "Transfer from " + fromAccNo, new Date(), "Transfer", amount));
        System.out.println("Transfer successful.");
        return true;
    }

    @Override
    public void getAccountDetails(long accountNumber) throws InvalidAccountException{
        Account acc = accountMap.get(accountNumber);
        if (acc != null) {
                acc.accountDetails();
                return;
            } else {
                throw new InvalidAccountException("Account not found.");
            }
        }
    @Override
    public List<Transaction> getTransactions(long accountNumber, Date fromDate, Date toDate) throws InvalidAccountException {
        if (!accountMap.containsKey(accountNumber)) throw new InvalidAccountException("Account not found.");
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactionList) {
            if (t.getAccount().getAccountNumber() == accountNumber &&
                !t.getDate().before(fromDate) && !t.getDate().after(toDate)) {
                result.add(t);
            }
        }
        return result;
    }
    }


    

        