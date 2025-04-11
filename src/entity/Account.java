package entity;

public class Account {
	private static long AccNo = 1001;
    private long accountNumber;
    private String accountType;
    private float accountBalance;
    private Customer customer;

    public Account() {}

    public Account(String accountType, float accountBalance, Customer customer) {
        this.accountNumber = AccNo++;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.customer = customer;
    }
    
    public Account(long accountNumber, String accountType, float accountBalance, Customer customer) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
        this.customer = customer;
    }


    
    public long getAccountNumber() {return accountNumber;}
    public void setAccountNumber(long accountNumber) {this.accountNumber = accountNumber;}
	
	public String getAccountType() {return accountType;}
	public void setAccountType(String accountType) {this.accountType = accountType;}
		
    public float getAccountBalance() {return accountBalance;}
	public void setAccountBalance(float accountBalance) {this.accountBalance = accountBalance;}
		
	public Customer getCustomer() { return customer; }
	public void setCustomer(Customer customer) { this.customer = customer; }

	 public void accountDetails() {
	        System.out.println("Account Number: " + accountNumber);
	        System.out.println("Account Type: " + accountType);
	        System.out.println("Balance:" + accountBalance);
	        customer.customerDetails();
	    }
	 
	 
	public void deposit(int amount) {
        deposit((float) amount);
    }
    public void deposit(double amount) {
        deposit((float) amount);
    }
    
	
	public void deposit(float amount) {
        if (amount > 0) {
            accountBalance += amount;
            System.out.println("Deposited " + amount + "\nNew balance: " + accountBalance);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

	public void withdraw(int amount) {
        withdraw((float) amount);
    }
    public void withdraw(double amount) {
        withdraw((float) amount);
    }
    
    
    public boolean withdraw(float amount) {
        if (amount > 0) {
            if (accountBalance >= amount) {
                accountBalance -= amount;
                System.out.println("Withdrew " + amount + "\nNew balance: " + accountBalance);
                return true;
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Invalid withdrawal amount!");
        }
        return false;
    }
    
    public void calculateInterest() {
        System.out.println("Interest calculation not applicable for this generic account types.");
     }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Account other = (Account) obj;
        return accountNumber == other.accountNumber;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(accountNumber);
    }
}