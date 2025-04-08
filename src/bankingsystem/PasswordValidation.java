package bankingsystem;

import java.util.Scanner;

public class PasswordValidation {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Please create a password for your bank account: ");
        String pass = scan.nextLine();

        boolean hasUppercase = false;
        boolean hasDigit = false;

        if (pass.length() < 8) {
            System.out.println("Password Invalid. Password must be at least 8 characters long.");
        } else{
            for (int i = 0; i < pass.length(); i++) {
                char ch = pass.charAt(i);
                
                if (Character.isUpperCase(ch)) {
                    hasUppercase = true;
                }
                if (Character.isDigit(ch)) {
                    hasDigit = true;
                }
            }
            if (!hasUppercase) {
                System.out.println("Password Invalid. Password must contain at least one uppercase letter.");
            }
            if (!hasDigit) {
                System.out.println("Password Invalid. Password must contain at least one digit.");
            }
            if (pass.length() >= 8 && hasUppercase && hasDigit) {
                System.out.println("Your password is valid. Password created successfully!");
            }
        }
        scan.close();
    }
}