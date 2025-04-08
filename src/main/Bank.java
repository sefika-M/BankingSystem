package main;

import entity.*;
import java.util.Scanner;

public class Bank {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Account account = null;
        
        while (true) {
        System.out.println("\nWelcome to the Banking System!");
        System.out.println("1. Create new account.");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Calculate Interest");
        System.out.println("5. Exit");
        System.out.print("\nChoose an option: ");
        int choice = sc.nextInt();

        switch (choice) {
        case 1:
            if (account != null) {
                System.out.println("Account already exists.");
                break;
            }

            System.out.println("Choose account type:");
            System.out.println("1. Savings Account");
            System.out.println("2. Current Account");
            int accType = sc.nextInt();

            System.out.print("Enter Account Number: ");
            long accNo = sc.nextLong();

            System.out.print("Enter Initial Balance: ");
            double balance = sc.nextDouble();

            if (accType == 1) {
                System.out.print("Enter Interest Rate (%): ");
                double rate = sc.nextDouble();
                account = new SavingsAccount(accNo, balance, rate);
            } else if (accType == 2) {
                account = new CurrentAccount(accNo, balance);
            } else {
                System.out.println("Invalid account type.");
            }
            break;

        case 2:
            if (account == null) {
                System.out.println("No account found. Please create an account first.");
                break;
            }
            System.out.print("Enter amount to deposit: ");
            double deposit = sc.nextDouble();
            account.deposit(deposit);
            break;

        case 3:
            if (account == null) {
                System.out.println("No account found. Please create an account first.");
                break;
            }
            System.out.print("Enter amount to withdraw: ");
            double withdraw = sc.nextDouble();
            account.withdraw(withdraw);
            break;

        case 4:
            if (account == null) {
                System.out.println("No account found. Please create an account first.");
                break;
            }
            account.calculateInterest();
            break;

        case 5:
            System.out.println("Thank you for using the bank system.");
            sc.close();
            break;

        default:
            System.out.println("Invalid choice.");
        }
        }
    }
}
        
 
