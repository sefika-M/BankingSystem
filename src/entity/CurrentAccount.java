package entity;

public class CurrentAccount extends Account{
    private static final double OVERDRAFT_LIMIT = 5000;
    
    public CurrentAccount(long accountNumber, double accountBalance) {
        super(accountNumber, "Current", accountBalance);
    }

    @Override
    public void withdraw(double amount) {
    	double currentBal = getAccountBalance();
        if (amount <= currentBal + OVERDRAFT_LIMIT) {
        	 setAccountBalance(currentBal - amount);
            System.out.println("Withdrew: " + amount + "\nNew balance: " + getAccountBalance());
            if (currentBal < amount) {
                System.out.printf("Overdraft used. Remaining limit:" + (OVERDRAFT_LIMIT + getAccountBalance()));
            } 
        }else {
            System.out.println("Withdrawal denied! Exceeds overdraft limit.");
        }
    }
    @Override
    public void calculateInterest() {
        System.out.println("No interest for current account.");
    }
}
