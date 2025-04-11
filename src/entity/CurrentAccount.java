package entity;

public class CurrentAccount extends Account{
    public static final float OVERDRAFT_LIMIT = 5000.0f;
    
    public CurrentAccount(float balance, Customer customer)  {
        super("Current", balance, customer);
    }
    
    @Override
    public void deposit(float amount) {
        if (amount > 0) {
        	setAccountBalance(getAccountBalance() + amount);
            System.out.println("Deposited: " + amount + "\nNew balance: " + getAccountBalance());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    
    @Override
    public boolean withdraw(float amount) {
    	float currentBal = getAccountBalance();
    	if (amount > 0) {
        if (amount <= currentBal + OVERDRAFT_LIMIT) {
        	 setAccountBalance(currentBal - amount);
            System.out.println("Withdrew: " + amount + "\nNew balance: " + getAccountBalance());
            if (getAccountBalance() < amount) {
                System.out.printf("Overdraft used. Remaining limit:" + (OVERDRAFT_LIMIT + getAccountBalance()));
            } 
            return true;
        }else {
            System.out.println("Withdrawal denied! Exceeds overdraft limit.");
        }
    	}else {
    		System.out.println("Invalid withdrawal amount!");
        }
        return false;
    }
    
    @Override
    public void calculateInterest() {
        System.out.println("No interest for current account.");
    }
}


    
   
