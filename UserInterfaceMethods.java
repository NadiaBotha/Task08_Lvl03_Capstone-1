import java.sql.Connection
;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
/**
 * This class groups all methods that are required to run the system, without creating or editing Person or Project objects.
 * <p>
 * The methods in this class consists of repeating or lengthy display options as well as checking if user input is valid or not.
 * The methods do pull information form the database, but do not create or modify it.
 * 
 * @author Nadia Botha
 * @version 1.0  
 *
 */
public class UserInterfaceMethods {
	/**
	 * This method displays the options of the main menu. 
	 * <p>
	 * The options include creating a new object, editing an existing project, display projects that still have to be completed, dispay
	 * projects that are overdue and to exit the program.
	 */
	public static void displayOptions() {
		// When the method is called the options below are displayed.
		System.out.println("           MAIN MENU             ");
		System.out.println("");
		System.out.println("Please select one of the following options");
		System.out.println("");
		System.out.println("1. Create a new project.");
		System.out.println("2. Update an existing project.");
		System.out.println("3. Finalize an existing project");
		System.out.println("4. Display projects that still have to be completed.");
		System.out.println("5. Display projects that are overdue.");
		System.out.println("6. Search for a project by name/number");
		System.out.println("7. Exit the program");
			
	}
	
	/**
	 * This method requests the user to input an integer to execute an option from the main menu. 
	 * <p>
	 * The method request the user to input an integer form 1-7. After receiving the input, it checks
	 * if the input is valid. If not, it requests the user to try again.
	 * 
	 * @see UserInterfaceMethods #displayEditOptions()
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @return returns the integer for the main menu option chosen
	 */
	public static int getUserChoice(Scanner userInput) {
		
		// Create a string variable an initialize it - this will store the user input.
		String userChoice = "";
		// Create an integer variable and initialize it - will store the user input if it can be converted to an intger.
		int userChoiceNumber = 0;
		
		// Create a boolean variable and initialize it to false.
		boolean isUserChoiceValid = false;
		
		// The while loop will keep executing as long as the isUserChoiceValid is false.
		while (isUserChoiceValid == false) {
			System.out.println("");
			// Gets a string input from the user.
			System.out.print("Please enter your option here: ");
			userChoice = userInput.nextLine();
			
			// Try to convert the string value to an integer value.
			try {
				userChoiceNumber = Integer.parseInt(userChoice);
				/* If the string value can be converted to an integer value, still need to check if it is within range.
				 * If the integer value is smaller than 1 or bigger than 6, an error message is displayed and isUserChoiceValid is
				 * set to false so that the while loop is executed again.*/
				if (userChoiceNumber < 1 || userChoiceNumber > 7) {
					System.out.println("Integer entered is not valid, please select a valid option.");
					isUserChoiceValid = false;
				}
				/* If the string value can be converted to an integer and is within range, the isUserChoiceValid is set to true.
				 * The while loop wil be exited.*/
				else {
					isUserChoiceValid = true;
				}		
			}	
			// If the user input string cannot be converted to an integer an error message is dipslayed and the while loop is executed again.
			catch (Exception e) {
				System.out.println("Incorrect input, please enter an integer.");
				isUserChoiceValid = false;
			}		
		}
		// Returns an integer between 1-6.
		return userChoiceNumber;				
	}
	
	/**
	 * This method displays all the project and person options which can be edited.
	 * <p>
	 * The options include to update the project fee, the total paid to date, the project deadline, an architect's contact
	 * details, a contractor's contact details and a customer's contact details.
	 */
	private static void displayEditOptions() {
		System.out.println("1. Total fee charged");
		System.out.println("2. Total paid to date");
		System.out.println("3. Update the deadline");
		System.out.println("4. Update an architect's contact details");
		System.out.println("5. Update a contractor's contact details");
		System.out.println("6. Update a customer's contact details");
	}
	
	/**
	 * This method requests the user to input an integer to execute an option from the displayEditOptions. 
	 * <p>
	 * The method request the user to input an integer form 1-6. After receiving the input, it checks
	 * if the input is valid. If not, it requests the user to try again.
	 * 
	 * @see UserInterfaceMethods #displayEditOptions()
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @return returns the integer for the displayEditOption menu
	 */
	public static int getEditOption(Scanner userInput) {
		/*Define an integer variable to store the valid user choice.*/
		int userChoiceInt = 0;
		
		/*Define a boolean value that represents whether the user choice is valid or not.
		 * Initialize to false.*/
		boolean isChoiceValid = false;
		
		/*Request the user to input an option*/
		System.out.println("");
		System.out.println("Please select an update option below: ");
		displayEditOptions();
		System.out.print("Option: ");
		String userChoice = userInput.nextLine();
		
		/*The while loop will keep executing as long as the choice is invalid. 
		 * The user is requested to input a different value.*/
		while (isChoiceValid == false) {
			/*Tru to conert the user input to an integer.
			 * If this is successful change the boolean value to true.*/
			try {
				userChoiceInt = Integer.parseInt(userChoice);
				isChoiceValid = true;
				/*Check if the value is within in range, if not change the boolean 
				 * back to false so that the while loop executes again.
				 * If it is within range, the boolean value is true and the while loop
				 * is exited.*/
				if (userChoiceInt < 1 || userChoiceInt > 6) {
					System.out.println("");
					System.out.println("The number entered is not valid.");
					System.out.println("");
					System.out.println("Please select one of the options below: ");
					displayEditOptions();
					System.out.print("Option: ");
					userChoice = userInput.nextLine();
					isChoiceValid = false;
				}
			} 
			/*Exception that displays an error message if the value cannot be converted to
			 * an integer.*/
			catch (Exception e) {
				System.out.println("");
				System.out.println("The option entered is not an integer, please enter a valid integer.");
				System.out.println("");
				System.out.println("Please select one of the options below: ");
				displayEditOptions();
				System.out.print("Option: ");
				userChoice = userInput.nextLine();
			}
		}
		/*Returns a valid user choice*/
		return userChoiceInt;
	}
	
	/**
	 * This methods displays a summary of all the projects in the database.
	 * <p>
	 * The project number, name, deadline, customer name and project status is displayed.
	 */
	public static void displayProjectSummary() {
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Define a string used as input to the SQL query. Select the project name, number, deadline and status
	         * from the projects table and the assigned customer name from the customers table.*/
	        String inputSQL = "select projects.project_number, projects.project_name, "
	        		+ "projects.project_deadline, customers.customer_name, "
	        		+ "projects.project_status "
	        		+ "FROM projects "
	        		+ "INNER JOIN customers ON projects.project_customer_ID = customers.customer_ID "
	        		+ "ORDER BY projects.project_number;";
	        results = statement.executeQuery(inputSQL);
	        
	        /*Display the project summary.*/
	        System.out.println("");
	        System.out.println("*******************EXISTING PROJECTS*********************");
	        System.out.println("");
	        System.out.println("Number, Name, Deadline, Customer, Status");
	        System.out.println("");
	        
	        /*Loop through the results and print each project on a new line.*/
	        while (results.next()) {
	        	 System.out.println(results.getInt("project_number") +", " +results.getString("project_name") + ", " 
	        	+ results.getString("project_deadline") + ", " + results.getString("customer_name") + ", " + results.getString("project_status"));
	        }
	        System.out.println("*********************************************************");
			
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Display a summary of all the architects in the database.
	 */
	public static void displayArchitectSummary() {
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	       
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Input string to SQL query - select all field values from the architects table.*/
	        String inputSQL = "select * from architects";
	        results = statement.executeQuery(inputSQL);
	        
	        /*Display the records each on a separate line.*/
	        System.out.println("");
	        System.out.println("****************************ARCHITECTS****************************************************");
	        System.out.println("");
	        System.out.println("ID, Name, Number, Email Adress, Physical Address");
	        System.out.println("");
	        
	        while (results.next()) {
	        	 System.out.println(results.getInt("architect_ID") +", " +results.getString("architect_name") + ", " 
	        	+ results.getString("architect_email") + ", " + results.getString("architect_address"));
	        }
	        System.out.println("*******************************************************************************************");
			
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Display the contact detail options that the user can update for an architect.
	 */
	public static void architectDetailsToUpdate() {
		System.out.println("Please select the option to update: ");
		System.out.println("1. Architect number");
		System.out.println("2. Architect email");
		System.out.println("3. Architect physical address");
	}
	
	/**
	 * Display a summary of all the contractors in the database.
	 */
	public static void displayContractorSummary() {
		/*Refer to the displayArchitectSummary method as the methods are the same.*/
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "select * from contractors"; 
	        results = statement.executeQuery(inputSQL);
	        
	        System.out.println("");
	        System.out.println("****************************CONTRACTORS****************************************************");
	        System.out.println("");
	        System.out.println("ID, Name, Number, Email Adress, Physical Address");
	        System.out.println("");
	        
	        while (results.next()) {
	        	 System.out.println(results.getInt("contractor_ID") +", " +results.getString("contractor_name") + ", " 
	        	+ results.getString("contractor_email") + ", " + results.getString("contractor_address"));
	        }
	        System.out.println("*******************************************************************************************");
			
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Display the contact detail options that the user can update for a contractor.
	 */

	public static void contractortDetailsToUpdate() {
		System.out.println("Please select the option to update: ");
		System.out.println("1. Contractor number");
		System.out.println("2. Contractor email");
		System.out.println("3. Contractor physical address");
	}
	
	/**
	 * Display a summary of all the customers in the database.
	 */
	public static void displayCustomerSummary() {
		/*Refer to the displayArchitectSummary method as the methods are the same.*/
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "select * from customers";
	        results = statement.executeQuery(inputSQL);
	        
	        System.out.println("");
	        System.out.println("****************************CUSTOMERS****************************************************");
	        System.out.println("");
	        System.out.println("ID, Name, Number, Email Adress, Physical Address");
	        System.out.println("");
	        
	        while (results.next()) {
	        	 System.out.println(results.getInt("customer_ID") +", " +results.getString("customer_name") + ", " 
	        	+ results.getString("customer_email") + ", " + results.getString("customer_address"));
	        }
	        System.out.println("*******************************************************************************************");
			
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Display the contact detail options that the user can update for a suctomer.
	 */
	public static void customertDetailsToUpdate() {
		System.out.println("Please select the option to update: ");
		System.out.println("1. Customer number");
		System.out.println("2. Customer email");
		System.out.println("3. Customer physical address");
	}
	
	/**
	 * This method displays an invoice when called.
	 * <p>
	 * The method is only called when there is an outstanding amount on the project.
	 * It displays all of the customer contact details as well as the amount payable.
	 * 
	 * @see Person #getPersonName()
	 * @see Person #getEmailAddress()
	 * @see Person #getPhysicalAddress()
	 * @see Person #getTelephoneNumber()
	 * 
	 * @param projectCustomer Person object which is the customer for the project.
	 * @param amountPayable Double value that represents the amount that is still payable on the project.
	 */
	public static void generateInvoice (Person projectCustomer, Double amountPayable) {
		
		// The invoice displayed to the user.
		System.out.println("");
		System.out.println("                             INVOICE");
		System.out.println("");
		System.out.println("Customer information: ");
		System.out.println("Name: "+ projectCustomer.getPersonName());
		System.out.println("Number: "+ projectCustomer.getTelephoneNumber());
		System.out.println("Email address: "+ projectCustomer.getEmailAddress());
		System.out.println("Physical address: "+ projectCustomer.getPhysicalAddress());
		
		System.out.println("");
		System.out.println("Outstanding amount payable within 30 days: R" +amountPayable );
		System.out.println("");
		System.out.println("******************************************************************************");
	
	
	}
}