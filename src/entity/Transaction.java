package entity;
import java.sql.Timestamp;
import java.util.Date;

public class Transaction {
	private Account account;
    private String description;
    private Timestamp date;
    private String transactionType; 
    private float transactionAmount;
    private int transactionId;


    public Transaction() {}
    
    public Transaction(Account account, String description, Timestamp date, String transactionType, float transactionAmount) {
        this.account = account;
        this.description = description;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionAmount = transactionAmount;
    }
 
    public Transaction(int transactionId, Account account, String transactionType, float amount, Timestamp transactionDate) {
        this.transactionId = transactionId;
        this.account = account;
        this.transactionType = transactionType;
        this.transactionAmount = amount;
        this.date = transactionDate;
    }

    public Account getAccount() { return account;}
    public void setAccount(Account account) {
        this.account = account;
    }   

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public float getAmount() { return transactionAmount; }
    public void setAmount(float transactionAmount) { this.transactionAmount = transactionAmount; }
    
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public Timestamp getTransactionDate() {
        return date;
    }
    
    public void printTransactionDetails() {
        if (account != null) {
        System.out.println("Account No: " + account.getAccountNumber());}
        System.out.println("Date: " + date);
        System.out.println("Type: " + transactionType);
        System.out.println("Amount: ₹" + transactionAmount);
        System.out.println("Transaction Date: " + date);
    }
}


