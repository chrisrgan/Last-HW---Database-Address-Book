import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

class AddressBook {
	 
	
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;
	
	
	public void connectToDB() throws Exception {
		try{
			// Step 1. This will load the MySQL driver. Each DB has its own driver.
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Step 2. Setup the connection with the DB
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/notesdb?"
							+ "useLegacyDatetimeCode=false&serverTimezone=UTC" +
							"&user=root&password=test1234"); 
		} 
		catch(Exception e) {
			throw e;
		}
	}
	
	public void insertContact() throws SQLException {
		try {
			 //Person objects not necessary...
			 Person p = new Person();
	    	 Scanner scan = new Scanner(System.in);
	    	 System.out.println("Enter First Name");
	    	 p.firstName = scan.nextLine();
	    	 System.out.println("Enter Last Name");
	    	 p.lastName = scan.nextLine();
	    	 System.out.println("Enter Phone Number");
	    	 p.phoneNumber = scan.nextLine();	    	 
	    	 
	    	 
	    	 preparedStatement =  connect.prepareStatement("insert into notesdb.contacts (first,last,phone) values (?,?,?)");
	    	 preparedStatement.setString(1, p.firstName);
	    	 preparedStatement.setString(2, p.lastName);
			 preparedStatement.setString(3, p.phoneNumber);
			 preparedStatement.executeUpdate();

			 
			 
			 //Showing everything in Database
			 showAllData();
			 
		} catch (Exception e) {
			throw e;
		}
	}
	

	public void deleteContact() throws Exception {
		try {
	
			System.out.println("Please enter the phone num of person you want to delete from the Database");
			Scanner scan = new Scanner(System.in);
			String phn = scan.nextLine();
			
			//Exception handling
			checkPhone(phn);
			
			//removed straight from the database
			preparedStatement =  connect.prepareStatement("delete from notesdb.contacts where phone=?");
			preparedStatement.setString(1, phn);
			preparedStatement.executeUpdate();
			
			//Showing everything in Database
			showAllData();
		} 
		catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		
	}
	
	
	public void updateContact() throws SQLException {
		try{
	    	System.out.println("Enter the number of the person you want to update");
	    	Scanner scan = new Scanner(System.in);
	    	String phn = scan.nextLine();
	    	
	    	//Exception Handling
			checkPhone(phn);
	    	
			//Adding person objects not necessary...
	    	Person z = new Person();
	    	System.out.println("Enter the new First Name");
	    	z.firstName = scan.nextLine();
	    	System.out.println("Enter the new Last Name");
	    	z.lastName = scan.nextLine();
	    	System.out.println("Enter the new Phone Number");
	    	z.phoneNumber = scan.nextLine();
			 
	    	//Finds the ID of the input phone number, and then updates the row using the ID
	    	preparedStatement = connect.prepareStatement("select id from notesdb.contacts where phone =?");
	    	preparedStatement.setString(1, phn);
	    	resultSet = preparedStatement.executeQuery();
	    	while (resultSet.next()){
				String id = resultSet.getString("id");
				preparedStatement =  connect.prepareStatement("update notesdb.contacts set first = ?, last = ?, phone = ? where id =?");
				preparedStatement.setString(1, z.firstName);
				preparedStatement.setString(2, z.lastName);
				preparedStatement.setString(3, z.phoneNumber);
				preparedStatement.setString(4, id);
				preparedStatement.executeUpdate();

	    	}


	    	//Showing the whole database
	    	showAllData(); 
		 }
		
		 catch(IllegalArgumentException e){
					e.printStackTrace();
		 }
	 }
	
	 
	 public void sortContact() throws Exception {
		 try {
			 //Select * and then put it into an arrayList is another way to do...
			 
			 //cannot use prepared statement and execute update for select *, must use execute query....
			 System.out.println("These contacts will now be sorted by last name");
			 String sql = "select id, first, last, phone from notesdb.contacts order by last ASC";
			 statement = connect.createStatement();
			 resultSet = statement.executeQuery(sql);
			 
			 //Using showData instead of showAllData because we're not manipulating the database here, so need to use resultSet
			 //as a argument... Another way to do this where I can still use showAllData?
			 showData(resultSet);
		 }
		 
		 catch (Exception e) {
			 throw e;
		 }
	 }
	
	 
	 
	 public void searchContact() throws SQLException{
		 try {
	    	System.out.println("Please enter the phone number of the user that you want to search for");
	    	Scanner scan = new Scanner(System.in);
	    	String phn = scan.nextLine();
	    	
	    	//Exception Handling
	    	checkPhone(phn);
	    	
	    	//Showing all rows that contain the input phn
	    	preparedStatement = connect.prepareStatement("select * from notesdb.contacts where phone =?");
	    	preparedStatement.setString(1, phn);
	    	resultSet = preparedStatement.executeQuery();
	    	System.out.println("These are the contacts with phone number that matches " + phn);
	    	
	    	//Same thing as sortContact here, not actually manipulating data in the DB
	    	showData(resultSet);
	    	
		 }
		 
		 catch (IllegalArgumentException e){
			e.printStackTrace();
		 }
	 }
	 
	 
	 
	 
	 
	 
	 
	//These below are the non-menu methods that add functionality to the address book.
	 
	 
	//Used as Exception to check if a phone input is valid in the DB
	public void checkPhone(String phoneinput) throws SQLException {
		preparedStatement = connect.prepareStatement("select ID from notesdb.contacts where phone =?");
    	preparedStatement.setString(1, phoneinput);
    	resultSet = preparedStatement.executeQuery();
    	
    	//Checks if resultSet will iterate, meaning there is a phone number found
    	if (resultSet.next()){
    		System.out.println("");
    	}
    	//If not it means that phone num doesn't exist in DB
    	else{
    		throw new IllegalArgumentException("This phone number doesn't exist in the database, please press continue and"
    				+ " enter a correct phone number"); 
    	}    	

	}
	 
	 
	//Implementing this method so I don't have to copy paste all this code when I want to show the whole DB
	private void showAllData() throws SQLException {
    	System.out.println("This is the new updated Database");
		preparedStatement = connect.prepareStatement("select * from notesdb.contacts");
    	resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			String id = resultSet.getString("id");
			String firstnam = resultSet.getString("first");
			String lastnam = resultSet.getString("last");
			String phon = resultSet.getString("phone");

			System.out.print("ID : "+id);
			System.out.print("\t--> " + "First Name: " + firstnam + "\n" );
			System.out.print("\t--> " + "Last Name: " + lastnam + "\n");
			System.out.print("\t--> " + "Phone Number: " + phon + "\n");
		}
	}
	
	private void showData(ResultSet resultSet) throws SQLException {
		while(resultSet.next()) {
			String id = resultSet.getString("id");
			String firstnam = resultSet.getString("first");
			String lastnam = resultSet.getString("last");
			String phon = resultSet.getString("phone");

			System.out.print("ID : "+id);
			System.out.print("\t--> " + "First Name: " + firstnam + "\n" );
			System.out.print("\t--> " + "Last Name: " + lastnam + "\n");
			System.out.print("\t--> " + "Phone Number: " + phon + "\n");
		}
	}
	
	//Just in case we want to close the connection we can implement in the TaskBean
	public void close() {
		try {
			if(connect != null) {
				connect.close();
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	 
	
}