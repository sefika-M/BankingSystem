package bankingsystem;

import java.util.Scanner;

public class BalanceCheck {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);
        int[] accNum = {1020, 1021, 1022, 1023};
        double[] bal = {50000.0, 120780.5, 129.75, 809280.0};
        boolean valid = false;
        while (!valid) {
            System.out.print("Enter your account number: ");
            int acc = sn.nextInt();
            for (int i = 0; i < accNum.length; i++) {
                if (accNum[i] == acc) {
                    System.out.println(" Your Account balance is: " + bal[i]);
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Invalid account number. Please try again.");
            }
        }
        sn.close();
    }
}
