package bankingsystem;

import java.util.ArrayList;
import java.util.Scanner;

public class TransactionHistory {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        ArrayList<String> transactions = new ArrayList<>();
        boolean transaction = true;

        while (transaction) {
            System.out.println("\nBank Transaction Options:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sn.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: ");
                    double deposit = sn.nextDouble();
                    transactions.add("Deposited: " + deposit);
                    System.out.println("Deposit successful.");
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    double withdrawal = sn.nextDouble();
                    transactions.add("Withdrew: " + withdrawal);
                    System.out.println("Withdrawal successful.");
                    break;
                case 3:
                	transaction = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
        System.out.println("\nTransaction History:");
        if (transactions.isEmpty()) {
            System.out.println("No transactions made.");
        } else{
            for (int i = 0; i < transactions.size(); i++) {
                System.out.println("- " + transactions.get(i));
            }
        }
        sn.close();
    }
}
