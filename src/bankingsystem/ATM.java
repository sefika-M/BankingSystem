package bankingsystem;

import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
      
        System.out.print("Enter your current balance:");
        double bal = scan.nextLong();
        System.out.println("Select a transaction:");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.print("Enter your choice:");
        int choice = scan.nextInt();
        if (choice == 1) {
            System.out.println("Your current balance is:" + bal);
        } else if (choice == 2) {
            System.out.print("Enter amount to withdraw:");
            double withdrawAmt = scan.nextDouble();

            if (withdrawAmt <= bal) {
                if (withdrawAmt % 100 == 0 || withdrawAmt % 500 == 0) {
                    bal -= withdrawAmt;
                    System.out.println("Money withdrawn successfully. New balance:" + bal);
                } else {
                    System.out.println("Withdrawal amount must be in multiples of 100 or 500.");
                }
            } else {
                System.out.println("Insufficient balance.");
            }
        } else if (choice == 3) {
            System.out.print("Enter amount to deposit: ");
            double depositAmt = scan.nextDouble();
            bal += depositAmt;
            System.out.println("Deposit successful. New balance:" + bal);
        } else {
            System.out.println("Invalid choice.");
        }
        scan.close();
    }
}
