package bankingsystem;

import java.util.Scanner;

public class LoanEligibility {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your credit score: ");
        int cs = scan.nextInt();
        System.out.println("Enter your annual income: ");
        double income = scan.nextDouble();
        if (cs > 700 && income >= 50000) {
            System.out.println("Congratulations! You are eligible for a loan.");
        } else{
            System.out.println("Sorry, you are not eligible for a loan.");
        }
        scan.close();
    }
}
