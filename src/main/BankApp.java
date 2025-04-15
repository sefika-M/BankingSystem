package main;
import entity.*;
import exception.*;
import util.*;
import bean.BankServiceProviderImpl;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankServiceProviderImpl bank = new BankServiceProviderImpl("Main Branch", "Chennai");

        while (true) {
            System.out.println("\n--- Welcome to the HMBank Banking System! ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Transfer Money");
            System.out.println("6. View Account Details");
            System.out.println("7. List All Accounts");
            System.out.println("8. Calculate Interest");
            System.out.println("9. View Transactions");
            System.out.println("10. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                    	try {
                    		
                    		 System.out.println("Are you already a customer? (yes/no)");
                             String isCustomer = sc.nextLine().toLowerCase();
                             Customer customer = null;
                             if (isCustomer.equals("yes")) {
                                 System.out.print("Enter Customer ID: ");
                                 int id = sc.nextInt();
                                 sc.nextLine(); 
                                 customer = bank.getCustomerById(id); 
                                 if (customer == null) {
                                     System.out.println("Customer not found with ID: " + id);
                                     break;
                                 }
                             } else if (isCustomer.equals("no")) {
                              
                                 System.out.print("Enter First Name: ");
                                 String fname = sc.nextLine();
                                 System.out.print("Enter Last Name: ");
                                 String lname = sc.nextLine();
                                 System.out.print("Enter DOB (yyyy-MM-dd): ");
                                 String dobStr = sc.nextLine();
                                 Date dob = Date.valueOf(dobStr);
                                 System.out.print("Enter Email: ");
                                 String email = sc.nextLine();
                                 System.out.print("Enter Phone Number (10 digits): ");
                                 String phone = sc.nextLine();
                                 System.out.print("Enter Address: ");
                                 String address = sc.nextLine();
                                 customer = new Customer(fname, lname, dob, email, phone, address);
                                 boolean added = bank.addCustomer(customer); 
                                 if (!added) {
                                     System.out.println("Failed to add new customer.");
                                     break;
                                 }
                                 System.out.println("Customer created successfully.");
                             } else {
                                 System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                                 break;
                             }
                             
                    	 System.out.println("\nSelect Account Type:");
                         System.out.println("1. Savings");
                         System.out.println("2. Current");
                         System.out.println("3. Zero Balance");
                         System.out.print("Enter your choice: ");
                         int accType = sc.nextInt();
                         sc.nextLine(); 
                                  
                        System.out.print("Enter Initial Balance: ");
                        float bal = sc.nextFloat();
                        
                        Account newAcc = null;
                        if(bal>=0) {
                        if (accType == 1) {
                            if (bal < 500) {
                                System.out.println("Minimum balance for Savings Account is 500.");
                            } else {
                                newAcc = bank.createAccount(customer, "savings", bal, 0); 
                            }
                            } else if (accType == 2) {
                            	if (bal <= 0) {
                                    System.out.println("Invalid Balance for current account.");
                                } else {
                                    newAcc = bank.createAccount(customer, "current", bal); 
                                }                        
                            	} else if (accType == 3) {
                            newAcc = bank.createAccount(customer, "zero_balance", bal);
                        } else {
                            System.out.println("Invalid account type selected.");
                            break;
                        }}else {
                            System.out.println("Invalid Balance.");

                        }
                        
                        if (newAcc != null) {
                        System.out.println("\nAccount created successfully!");
                        System.out.println("Account number: " + newAcc.getAccountNumber());
                        }
                    	} catch (NumberFormatException e) {
                    	        System.out.println(" Please enter valid numbers only.");
                    	   } catch (NullPointerException e) {
                    	        System.out.println("Error: System failure - unexpected null reference.");
                    	   } catch (IllegalArgumentException e) {
                               System.out.println("Validation Error: " + e.getMessage());
                    	   
                    	}catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Something went wrong.");
                    	}
                        break;

                    case 2:
                    	try {
                        System.out.print("Enter Account Number: ");
                        long depAcc = sc.nextLong();
                        System.out.print("Enter Amount to Deposit: ");
                        float depAmt = sc.nextFloat();
                        float newBal = bank.deposit(depAcc, depAmt);
                        System.out.println("Deposit successful.  New Balance: " + newBal);
                    	 } catch (InvalidAccountException e) {
                    	        System.out.println(" Account not found: " + e.getMessage());
                    	    } catch (InvalidAmountException e) {
                    	        System.out.println("Invalid amount: " + e.getMessage());
                    	    } catch (NullPointerException e) {
                    	        System.out.println("System error: Unexpected null value.");
                    	    } catch (Exception e) {
                    	    	e.printStackTrace();
                    	        System.out.println("Something went wrong: " + e.getMessage());
                    	    }
                    	break;

                    case 3:
                        System.out.print("Enter Account Number: ");
                        long withAcc = sc.nextLong();
                        System.out.print("Enter Amount to Withdraw: ");
                        float withAmt = sc.nextFloat();
                        try {
                        float withBal = bank.withdraw(withAcc, withAmt);
                        if (withBal >= 0) {
                            System.out.println("Withdrawal successful. New Balance: " + withBal);
                        } }catch (InvalidAccountException | InsufficientFundException | OverDraftLimitExceededException e) {
                            System.out.println(e.getMessage());
                        } catch (NullPointerException e) {
                        	e.printStackTrace();
                            System.out.println("Error: Something went wrong (null encountered).");
                        }
                        break;

                    case 4:
                    	try {
                        System.out.print("Enter Account Number: ");
                        long balAcc = sc.nextLong();
                        float balance = bank.getAccountBalance(balAcc);
                        System.out.println("Current Balance: " + balance);
                    	 } catch (InvalidAccountException e) {
                    	        System.out.println(e.getMessage());
                    	    } catch (NullPointerException e) {
                    	    	e.printStackTrace();
                    	        System.out.println("System error: Unexpected null encountered.");
                    	    }
                    	break;

                    case 5:
                    	try {
                        System.out.print("Enter From Account Number: ");
                        long from = sc.nextLong();
                        System.out.print("Enter To Account Number: ");
                        int to = sc.nextInt();
                        System.out.print("Enter Amount to Transfer: ");
                        float amt = sc.nextFloat();
                        boolean success = bank.transfer(from, to, amt);
                        if (success) {
                        System.out.println("Transfer completed successfully");
                        }
                    	} catch (InvalidAccountException e) {
                            System.out.println("Invalid account: " + e.getMessage());
                        } catch (InsufficientFundException e) {
                            System.out.println("Insufficient balance: " + e.getMessage());
                        } catch (OverDraftLimitExceededException e) {
                            System.out.println("Overdraft limit exceeded: " + e.getMessage());
                        } catch (InvalidAmountException e) {
                            System.out.println("Invalid amount: " + e.getMessage());
                        }
                        break;
                        
                    case 6:
                    	try {
                        System.out.print("Enter Account Number: ");
                        long detAcc = sc.nextLong();
                        Account account = bank.getAccountDetails(detAcc);
                        System.out.println("\n--- Account Details ---");
                        System.out.println("Account ID: " + account.getAccountNumber());
                        System.out.println("Account Type: " + account.getAccountType());
                        System.out.println("Balance: " + account.getAccountBalance());

                        Customer cust = account.getCustomer();
                        System.out.println("Customer ID: " + cust.getCustomerID());
                        System.out.println("Name: " + cust.getFirstName() + " " + cust.getLastName());
                        System.out.println("DOB: " + cust.getDob());
                        System.out.println("Email: " + cust.getEmailAddress());
                        System.out.println("Phone: " + cust.getPhoneNumber());
                        System.out.println("Address: " + cust.getAddress());
                    	 } catch (InvalidAccountException e) {
                    	        System.out.println("Invalid account: " + e.getMessage());
                    	    }catch (SQLException e) {
                    	        System.out.println("Database error: " + e.getMessage());
                    	    }catch (NullPointerException e) {
                    	    	e.printStackTrace();
                    	        System.out.println("System error: Null value encountered.");
                    	    }
                        break;
                        
                    case 7:
                    	try {
                        Account[] allAccounts = bank.listAccounts();
                        System.out.println("Listing All Accounts:");
                        for (Account acc : allAccounts) {
                            acc.accountDetails();
                            System.out.println("-----------------------------");
                        }  
                        } catch (NullPointerException e) {
                        	e.printStackTrace();
                            System.out.println("Error: Account list is empty or uninitialized.");
                        } catch (Exception e) {
                        	e.printStackTrace();
                            System.out.println("Unexpected error while retrieving account list: " + e.getMessage());
                        } break;
                        
                    case 8:
                    	try {
                    		System.out.print("Enter Account Number: ");
                    		long accNo = sc.nextLong();
                    		System.out.print("Enter Interest Rate (%): ");
                            float rate = sc.nextFloat();
                    		float updatedBalance = bank.calculateInterest(accNo, rate);
                    		if (updatedBalance != -1) {
                    		    System.out.println("Interest applied. New Balance: " + updatedBalance);
                    		}
                    		else
                                System.out.println("Interest can only be applied to savings accounts.");
                    	 } catch (InvalidAccountException e) {
                    	        System.out.println(e.getMessage());  
                    	 }catch (NullPointerException e) {
                    		 e.printStackTrace();
                    	        System.out.println("Error: No accounts found or data missing.");
                    	 } catch (Exception e) {
                    	        e.printStackTrace();
                    	    }
                        break;


                    case 9:
                    	try {
                            System.out.print("Enter Account Number: ");
                            long accNum = sc.nextLong();
                            sc.nextLine(); 
                            System.out.print("Enter From Date (yyyy-MM-dd): ");
                            String from = sc.nextLine();
                            System.out.print("Enter To Date (yyyy-MM-dd): ");
                            String to = sc.nextLine();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            java.util.Date fromUtilDate = sdf.parse(from);
                            java.util.Date toUtilDate = sdf.parse(to);

                            java.sql.Date fromDate = new java.sql.Date(fromUtilDate.getTime());
                            java.sql.Date toDate = new java.sql.Date(toUtilDate.getTime());

                            List<Transaction> txnList = bank.getTransactions(accNum, fromDate, toDate);
                            if (txnList.isEmpty()) {
                                System.out.println("No transactions found for this period.");
                            } else {
                                for (Transaction t : txnList) {
                                    t.printTransactionDetails();
                                    System.out.println("-----------------------------");
                                }
                            }
                        } catch (InvalidAccountException e) {
                            System.out.println("Invalid account: " + e.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error fetching transactions.");
                        }
                        break;
                        
                    case 10:
                        System.out.println("Thank you for banking with us!");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
            	e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
                sc.nextLine(); 
            }
        }
    }
}
