package entity;
import java.sql.Date;

public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private Date dob;
    private String emailAddress;
    private String phoneNumber;
    private String address;

    public Customer() {}

    public Customer(int customerID, String firstName, String lastName, Date dob, String emailAddress, String phoneNumber, String address) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        setEmailAddress(emailAddress); // validation
        setPhoneNumber(phoneNumber);  
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		if (emailAddress != null && emailAddress.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
            this.emailAddress = emailAddress;
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber != null && phoneNumber.matches("\\d{10}")) {
            this.phoneNumber = phoneNumber;
        } else {
            throw new IllegalArgumentException("Phone number must be 10 digits.");
        }
    }

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void customerDetails() {
        System.out.println("Customer ID: " + customerID);
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("DOB: " + dob);
        System.out.println("Email: " + emailAddress);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Address: " + address);
    }
}