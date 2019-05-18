import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TaskBean {
	

	public static int choice;
	
	
	public static boolean getContinue() {
	     System.out.println("Continue? (y/n) ");
	     Scanner scan = new Scanner(System.in);
	     
	     return (scan.next().charAt(0) == 'y');
	} 
	
	public static int menu() {
		System.out.println("Menu: ");
        System.out.println("1. Add a new contact ");
        System.out.println("2. Delete a contact");
        System.out.println("3. Update an existing contact ");
        System.out.println("4. List all added contacts in sorted order");
        System.out.println("5. Search for a given contact");
        Scanner scan = new Scanner(System.in);
        return scan.nextInt();
	}
	
	
	

	public static void main(String[] args) throws Exception {
		//Establish connection
		AddressBook ab = new AddressBook();
		ab.connectToDB();
		
		
		//Menu Choices
		do {
	        choice = menu();
	        switch (choice) {
	            case 1:	 
	                ab.insertContact();
	                break;
	            case 2:
	            	ab.deleteContact(); 
	                break;
	            case 3:
	            	ab.updateContact();
	                break;
	            case 4:
	            	ab.sortContact();
	                break;
	            case 5:
	            	ab.searchContact();
	                break;
	           
	        }
	
	    } while (getContinue());

	}
}