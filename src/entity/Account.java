package entity;

public class Account {
    private long accountNumber;
    private String accountType;
    private double accountBalance;

    public Account() {}

    public Account(long accountNumber, String accountType, double accountBalance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }

    
    public long getAccountNumber() {return accountNumber;}
    public void setAccountNumber(long accountNumber) {this.accountNumber = accountNumber;}
	
	public String getAccountType() {return accountType;}
	public void setAccountType(String accountType) {this.accountType = accountType;}
		
    public double getAccountBalance() {return accountBalance;}
	public void setAccountBalance(double accountBalance) {this.accountBalance = accountBalance;}
		
	
	public void deposit(float amount) {
        deposit((double) amount);
    }
    public void deposit(int amount) {
        deposit((double) amount);
    }
    
	
	public void deposit(double amount) {
        if (amount > 0) {
            accountBalance += amount;
            System.out.println("Deposited " + amount + "\nNew balance: " + accountBalance);
        } else {
            System.out.println("Invalid deposit amount!");
        }
    }

	public void withdraw(float amount) {
        withdraw((double) amount);
    }
    public void withdraw(int amount) {
        withdraw((double) amount);
    }
    
    
    public void withdraw(double amount) {
        if (amount > 0) {
            if (accountBalance >= amount) {
                accountBalance -= amount;
                System.out.println("Withdrew " + amount + "\nNew balance: " + accountBalance);
            } else {
                System.out.println("Insufficient balance!");
            }
        } else {
            System.out.println("Invalid withdrawal amount!");
        }
    }
    
    public void calculateInterest() {
        System.out.println("Interest calculation not applicable for this generic account types.");
     }
    
//        public void calculateInterest() {
//        if (accountType.equalsIgnoreCase("Savings")) {
//            double interest = accountBalance * 0.045;
//            accountBalance += interest;
//            System.out.println("Interest added: " + interest + "\nNew balance:" + accountBalance);
//        } else {
//            System.out.println("Interest calculation only for Savings accounts.");
//        }
//    }

    
        public void accountDetails() {
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Type: " + accountType);
        System.out.println("Balance:" + accountBalance);
    }
}