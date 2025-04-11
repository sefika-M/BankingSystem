package entity;

public class ZeroBalanceAccount extends Account {
    public ZeroBalanceAccount(Customer customer) {
        super("ZeroBalance", 0, customer); 
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
            if (getAccountBalance() >= amount) {
                setAccountBalance(getAccountBalance() - amount);
                System.out.println("Withdrew: " + amount + "\nNew balance: " + getAccountBalance());
            } else {
                System.out.println("Insufficient balance. Cannot withdraw.");
            }
        } else {
            System.out.println("Invalid withdrawal amount.");
        }
        return false;

    }
}
