package entity;

public class SavingsAccount extends Account {
    private float interestRate;

    public SavingsAccount(float accountBalance,  Customer customer, float interestRate) {
        super("Savings", (accountBalance >= 500 ? accountBalance : 500), customer);
        this.interestRate = interestRate;
    }

    @Override
    public void calculateInterest() {
        float interest = getAccountBalance() * (interestRate / 100);
        setAccountBalance(getAccountBalance() + interest);
        System.out.println("Interest added: " + interest + "\nNew balance: " + getAccountBalance());
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
        if (amount > 0) {
        	float bal = getAccountBalance() - amount;
        	if (bal >= 500) {
        		setAccountBalance(bal);
                System.out.println("Withdrew: " + amount + "\nNew balance: " + bal);
                return true;
        	} else {
                System.out.println("Withdrawal failed. Minimum balance of ₹500 must be maintained.");
            }
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
        return false;
    }
}

        	
        
    




