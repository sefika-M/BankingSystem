package bankingsystem;

import java.util.Scanner;

public class CompoundInterestCalc {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter number of customers: ");
        int n = scan.nextInt();
        
        if (n <= 0) {
            System.out.println("Invalid input. Number of customers must be at least 1.");
        } else {
        	for (int i = 1; i <= n; i++) {
                System.out.println("Customer " + i + ":");
                System.out.print("Enter initial balance: ");
                double initialBal = scan.nextDouble();
                System.out.print("Enter annual interest rate (%): ");
                double interestRate = scan.nextDouble();
                System.out.print("Enter number of years: ");
                int years = scan.nextInt();
                
                double futureBal = initialBal * Math.pow((1 + interestRate / 100), years);
                
                System.out.printf("Future Balance after %d years: %.2f\n", years, futureBal);
            }
        }
        scan.close();
    }
}
