import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/**
 * This class groups all methods used to create and modify Person objects.
 * <p>
 * This class enables the user to create new Persons - architects, contractor and customers.
 * It also writes any new entries and changes to the database.
 * 
 * @author Nadia Botha
 * @version 1.0
 */
public class PersonMethods {

/**************************************************************************ARCHITECT METHODS*********************************************************************************************/
	
	/**
	 * This method returns an architect ID based on a name and number.
	 * <p>
	 * The user provides an architect name and number which is then used to obtain
	 * the ID of the architect from the architects table. If the name and number provided,
	 * cannot be found, a new architect is created an written to the database.
	 * 
	 * @see PersonMethods #checkIfArchitectExists(String, String)
	 * @see PersonMethods #getExitingArchitectID(String, String)
	 * @see PersonMethods #nextUniquearchitectID()
	 * @see PersonMethods #createNewArchitectAndWriteToDatabase(Scanner, int, String, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @return returns an integer for the architect's ID in architects table.
	 */
	public static int determineProjectArchitectID(Scanner userInput) {
		/* Define an integer that stores the architect's ID and initialize the value.*/
		int architectID = 0;
		/*Request the user to input the architect's details - name, surname and telephone number.
		 * These three fields should make the architect unique.*/
		System.out.print("Architect's first name: ");
		String architectName = userInput.nextLine(); 

		System.out.print("Architect's surname: ");
		String architectSurname = userInput.nextLine(); 

		System.out.print("Architect's number: ");
		String architectNumber = userInput.nextLine();
		
		/*Generate the architect's full name by creating a concatenated string with the first name and surname.*/
		String architectFullName = architectName + " " + architectSurname;
		
		/*Define an integer that counts the instances of the specific architect in the database.
		 * If the result is 0, then the architect is already in the system, otherwise it is a new architect
		 * that has to be created.*/
		int isArchitectNew = checkIfArchitectExists(architectFullName, architectNumber);
		
		/*Code will execute if the architect was counted in the database and already exists.*/
		if (isArchitectNew > 0) {
			System.out.println("***Architect is already in the system and has been assigned to the project.***");
			/*Calls the getExistingArchitectID method which obtains the architects ID based on the name and number proveded, 
			 * sets it equal to the architectID int variable.*/
			architectID = getExitingArchitectID(architectFullName, architectNumber);
		} 
		/*Code will execute if the architect is new and was not found in the database.*/
		else {
			System.out.println("***The architect does not exist in the system and needs to be created.***");
			System.out.println("Please provide the additional information requested below.");
			/*The architectID variable is set equal to the next available unique ID by calling the nextUniqueID method.*/
			architectID = nextUniquearchitectID();
			/*Because the architect does not exists, it needs to be created and written to the database. This is done by calling 
			 * the createNewArchitectAndWriteToDatabase*/
			createNewArchitectAndWriteToDatabase(userInput, architectID, architectNumber, architectFullName);
		}
		/*Return the architect's ID based on the name and number for an existing architect.
		 * Or return the next available ID if the architect is new.*/
		return architectID;
	}
	
	/**
	 * This method creates a new Person object called projectArchitect and writes the new architect to the architects table.
	 * <p>
	 * The method calls separate methods to create a Person object and to write it to the database.
	 * 
	 * @see PersonMethods #createNewArchitect(String, String, Scanner)
	 * @see PersonMethods #writeNewArchitectToDatabase(int, String, String, String, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param architectID integer that represents the architect's ID in the architects table.
	 * @param architectNumber String that represents the architect's number in the architects table.
	 * @param architectFullName String that represents the architect's name in the architects table.
	 */
	private static void createNewArchitectAndWriteToDatabase(Scanner userInput, int architectID, String architectNumber,
			String architectFullName) {
		/*Define a new Person object called projectArchitect and set it equal to the result of createNewArchitect method.
		 * This will create a new Person object and save it in the projectArchitect variable.*/
		Person projectArchitect = createNewArchitect(architectFullName, architectNumber, userInput);
		
		/*Obtain strings for all the Person fields - name, number, email and physical address*/
		String architectNameToDatabase = projectArchitect.getPersonName();
		String architectNumberToDatabase = projectArchitect.getTelephoneNumber();
		String architectEmailToDatabase = projectArchitect.getEmailAddress();
		String architectAddressToDatabase = projectArchitect.getPhysicalAddress();
		
		/*Use the strings obtained above as input to the writeNewArchitectToDatabase, which inserts the architect as a new record into the database - table architects.*/
		writeNewArchitectToDatabase(architectID, architectNameToDatabase, architectNumberToDatabase, architectEmailToDatabase, architectAddressToDatabase);
	}
	
	/**
	 * This method takes an architect name and number as input and checks if the architect is in the database.
	 * <p>
	 * If the architect is in the database the integer 1 is returned, otherwise 0 is returned.
	 * 
	 * @param userArchitectFullName String that represents the architect's full name.
	 * @param userArchitectNumber String that represents the arhitect's number.
	 *  
	 * @return returns an integer, where 1 represents that the architect is in the system and 0 represents that the architect does not exist.
	 */
	private static int checkIfArchitectExists(String userArchitectFullName, String userArchitectNumber) {
		// Define an integer that counts the instances of an architect.
		int isArchitectNew = 0;
		
		/*Try block connects to the database poisepms and performs queries and/or updates.*/
	    try {
	        
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Strings defined to store the architectName and Number as the code loops through the database.*/
	        String currectArchitectFullName;
	        String currentArchitectNumber;
	        
	        /*String used as input to execute a database query - select the all architect names and numbers from the architects table*/
	        String inputSQL = "SELECT architect_name, architect_number FROM architects";
	        results = statement.executeQuery(inputSQL);
	        
	        /* Loop through the results and check if the name and number is equal to the name and number provided by the user.*/
	        while (results.next()) {
	        	/*Temporarily store the name and number of each architect in the database.*/
	        	currectArchitectFullName = results.getString("architect_name");
	        	currentArchitectNumber = results.getString("architect_number");
	        	/*If the name and number is the same as the user's input, index the counter isArchitectNew*/
	        	if (userArchitectFullName.equals(currectArchitectFullName) && userArchitectNumber.equals(currentArchitectNumber)) {
	        		isArchitectNew ++;
	        	}
	        }
	        
	        /*Close objects*/
	        results.close();
	        statement.close();
	        connection.close();
	    } 
	    /*Catch block is executed if any errors occur when trying to connect to the database or when any queries/updates are executed.*/
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
	    /*Returns the numbers of times that the architect was counted. This value will only be 0 or 1.
	     * 0 - that the architect is not found in the system.
	     * 1 - means that the architect is already existing.*/
	    return isArchitectNew;
	}
	
	/**
	 * This method returns the architect ID for an existing architect in the system.
	 * <p>
	 * The method uses an architect name and number to look for the ID in the database. 
	 * 
	 * @param architectName String that represents the architect's name.
	 * @param architectNumber String the represents the architect's number.
	 * 
	 * @return returns an integer that represents the architect's ID within the database.
	 */
	public static int getExitingArchitectID(String architectName, String architectNumber) {
		/*Define an integer which will store the architect ID once found in the system.
		 * Initialize it.*/
		int existingArchitectID = 0;
		
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Define a string used as input to execute a query. Will provide an architect ID for a specific architect name and number.*/
	        String inputSQL = "SELECT architect_ID FROM architects WHERE architect_name = '" + architectName+ "'" + "AND architect_number = "
	        		+ "'" + architectNumber + "'";
	        results = statement.executeQuery(inputSQL);
	        
	        /*If the record is found, set the result equal to the existingArchitectID integer variable.*/
	        if (results.next()){
	        	existingArchitectID = results.getInt("architect_ID");
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
	        
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    /*Return the integer for the architect ID found in the database for the given name and number.*/
	    return existingArchitectID;
	}
	
	/**
	 * This method creates a new Person project architect.
	 * <p>
	 * It takes the architect name and number as input, additionally requests the email and physical address to create a new
	 * Person object. This is used to create a new architect Person object.
	 * 
	 * @param architectName String that represents the architect's name.
	 * @param architectNumber String the represents the architect's number
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @see Person #Person(String, String, String, String)
	 * 
	 * @return returns a Person object - architect.
	 */
	private static Person createNewArchitect(String architectName, String architectNumber, Scanner userInput) {
		/*When this method is called the name and number has already been provided - now the rest of the information
		 * is requested. The email address and the physical address is requested by the user.*/
		System.out.print("Architect email address: ");
		String architectEmail = userInput.nextLine();
		
		System.out.print("Architect physical address: ");
		String architectAddress = userInput.nextLine();
		
		/*Create a new Person object, with the method inputs as well as the newly defined strings.*/
		Person newArchitect = new Person(architectName, architectNumber, architectEmail, architectAddress);
		
		/*Return the Person object created above.*/
		return newArchitect;
	}
	
	/**
	 * This method writes a new architect record into the architects table.
	 * <p>
	 * The fields for the architect are provided as input to the methods, the database is accessed and 
	 * an update query is performed to write the new architect to the database.
	 * 
	 * @param architectID integer that represents the architect's ID in the architects table.
	 * @param architectName String that represents the architect's name in the architects table.
	 * @param architectNumber String that represents the architect's number in the architects table.
	 * @param architectEmail String that represents the architect's email in the database.
	 * @param architectAddress String that represents the architect's address in the database.
	 */
	private static void writeNewArchitectToDatabase(int architectID, String architectName, String architectNumber, String architectEmail, String architectAddress) {
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	        
	        Statement statement = connection.createStatement();
	        int rowsAffected;
	        
	        /*Define a string used as input to execute an update. Insert a new architect in the architects table.*/
	        String SQLinput = "INSERT INTO architects VALUES ("+ architectID + ", '" + architectName+"','"+architectNumber+"','"+architectEmail+"','"+architectAddress+"')";
	        rowsAffected = statement.executeUpdate(SQLinput);
	        
	        /*Display a message that the architect has been successfully added.*/
	        System.out.println("***" + rowsAffected + " new architect added.***");

	        statement.close();
	        connection.close();
		
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * This method loops through the existing architect IDs and returns the integer for the next available unique ID.
	 * 
	 * @return returns the integer for the next available unique architect ID.
	 */
	private static int nextUniquearchitectID() {
		/*Define an integer variable that will store the next available unique architect ID*/
		int uniqueID = 0;
		/*Create an arraylist that will store all the existing architect IDs*/
		ArrayList<Integer> projectNumberArray = new ArrayList<Integer>();
		
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	        
	        Statement statement = connection.createStatement();
	        ResultSet results;
	              
	        /*Define a string used as input to execute a query. Select all architect_IDs from the architects table and order in ascending order.*/
	        String inputSQL = "SELECT architect_ID FROM architects ORDER BY architect_ID";
	        results = statement.executeQuery(inputSQL);
	
	        
	        while (results.next()) {
	        	/*Add each architect ID to the arraylist. This will be added in ascending order.*/
	            projectNumberArray.add(results.getInt("architect_ID"));
	        }
	        
	        /*Assign a unique ID as the maximum existing ID +1.*/
	        uniqueID = Collections.max(projectNumberArray) + 1;
	        
	        results.close();
	        statement.close();
	        connection.close();

		} catch (SQLException e) {
	        // We only want to catch a SQLException - anything else is off-limits for now.
	        e.printStackTrace();
	    }
	    /*Return the unique ID, which will be one bigger than the maximum existing ID.*/
	    return uniqueID;
	}
	
	/**
	 * This method creates a Person object using the architect ID.
	 * <p>
	 * This method uses an architect ID as input, reads all the field values from the architects table for the ID and 
	 * creates a new Person object.
	 * 
	 * @see Person #Person(String, String, String, String)
	 * 
	 * @param architectID integer that represents the architect's ID in the architects table.
	 * 
	 * @return returns a Person object (architect) created from architect ID.
	 */
	public static Person createArchitectPersonObject(int architectID){
		/*Create a Person object and initailize it. Used to store a Person read from the database.*/
		 Person projectArchitect = new Person();
		 
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	        
	        Statement statement = connection.createStatement();
	        ResultSet results;

	        /*Define a string used as input to execute a query. Select all fields from the architects table, except the architect ID, for a specific ID.*/
	        String inputSQL = "SELECT architect_name, architect_number, architect_email, architect_address FROM architects WHERE architect_ID = " + architectID;
	        results = statement.executeQuery(inputSQL);
	        
	        /*Once the fields have been obtained for a specific ID, set the field values equal to strings as seen belwo.*/
	        if (results.next()) {
	            String architectName = results.getString("architect_name");
	            String architectNumber = results.getString("architect_number");
	            String architectEmail = results.getString("architect_email");
	            String architectAddress = results.getString("architect_address");
	            
	            /*Create a Person with the strings as input to the Person constructor.*/
	            projectArchitect = new Person(architectName, architectNumber, architectEmail, architectAddress);
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
	        
		} catch (SQLException e) {
	        // We only want to catch a SQLException - anything else is off-limits for now.
	        e.printStackTrace();
	    }
	    /*Return the Person object*/
	    return projectArchitect;
	}
	
	/**
	 * This method enables the user to edit the number of an existing architect.
	 * <p>
	 * The method uses the architect ID to find the record. Requests the user to input the updated number
	 * and updates the number for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param architectID integer that represents the architect's ID in the architects table.
	 */
	private static void changeArchitectNumber(Scanner userInput, int architectID) {
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            /*Define a string used as input to execute a query. Select the architect number for a specific architect.*/
	            String inputSQL = "SELECT architect_number FROM architects WHERE architect_ID = " + architectID;
	            results = statement.executeQuery(inputSQL);
	            
	            /*Display the result obtained - the current value for the architect's number.*/
	            System.out.println("");
	            System.out.print("The architect's current number is: ");
		        if (results.next()){
		        	System.out.print(results.getString("architect_number"));
		        }
	            
		        /*Request the user to input the new value for the architect's number.*/
		        System.out.println("");
		        System.out.print("Please enter the updated number: ");
		        String updatedNumber = userInput.nextLine();
		        
		        /*Define a string used as input to execute an update. Set the value for the architect's number equal to the new value.*/
		        inputSQL = "UPDATE architects SET architect_number = '" + updatedNumber + "' WHERE architect_ID =" + architectID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            /*Display a message that shows the record has been succesully updated.*/
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " architect details updated.***");
	            
	            /*Define a string used as input to execute a query. Select all the field values for a specific architect ID.*/
	            inputSQL = "SELECT * FROM architects WHERE architect_ID = " + architectID;
	            results = statement.executeQuery(inputSQL);
	            
	            /*Display the updated architect details*/
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("Updated architect:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Architect ID: " + results.getInt("architect_ID"));
		        	System.out.println("Architect Name: " + results.getString("architect_name"));
		        	System.out.println("Architect Number: " + results.getString("architect_number"));
		        	System.out.println("Architect Email: " + results.getString("architect_email"));
		        	System.out.println("Architect Physical Address: " + results.getString("architect_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();   
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to edit the email of an existing architect.
	 * <p>
	 * The method uses the architect ID to find the record. Requests the user to input the updated email
	 * and updates the email for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param architectID integer that represents the architect's ID in the architects table.
	 */
	private static void changeArchitectEmail(Scanner userInput, int architectID) {
		/*Logic is the same as the changeArchitectNumber method. Please refer to this method for comments.*/
	       try {
	            
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT architect_email FROM architects WHERE architect_ID = " + architectID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The architect's current email is: ");
		        if (results.next()){
		        	System.out.print(results.getString("architect_email"));
		        }
	            
		        System.out.println("");
		        System.out.print("Please enter the updated email: ");
		        String updatedEmail = userInput.nextLine();
		        
		        inputSQL = "UPDATE architects SET architect_email = '" + updatedEmail + "' WHERE architect_ID =" + architectID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " architect details updated.***");
	            
	            inputSQL = "SELECT * FROM architects WHERE architect_ID = " + architectID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("Updated architect:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Architect ID: " + results.getInt("architect_ID"));
		        	System.out.println("Architect Name: " + results.getString("architect_name"));
		        	System.out.println("Architect Number: " + results.getString("architect_number"));
		        	System.out.println("Architect Email: " + results.getString("architect_email"));
		        	System.out.println("Architect Physical Address: " + results.getString("architect_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();
	            
	        } catch (SQLException e) {
	            // We only want to catch a SQLException - anything else is off-limits for now.
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to edit the physical address of an existing architect.
	 * <p>
	 * The method uses the architect ID to find the record. Requests the user to input the updated address
	 * and updates the address for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param architectID integer that represents the architect's ID in the architects table.
	 */
	private static void changeArchitectAddress(Scanner userInput, int architectID) {
		/*Logic is the same as the changeArchitectNumber method. Please refer to this method for comments.*/
	       try { 
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT architect_address FROM architects WHERE architect_ID = " + architectID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The architect's current physical address is: ");
		        if (results.next()){
		        	System.out.print(results.getString("architect_address"));
		        }
	            
		        System.out.println("");
		        System.out.print("Please enter the updated physical address: ");
		        String updatedEmail = userInput.nextLine();
		        
		        inputSQL = "UPDATE architects SET architect_address = '" + updatedEmail + "' WHERE architect_ID =" + architectID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " architect details updated.***");
	            
	            inputSQL = "SELECT * FROM architects WHERE architect_ID = " + architectID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated architect:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Architect ID: " + results.getInt("architect_ID"));
		        	System.out.println("Architect Name: " + results.getString("architect_name"));
		        	System.out.println("Architect Number: " + results.getString("architect_number"));
		        	System.out.println("Architect Email: " + results.getString("architect_email"));
		        	System.out.println("Architect Physical Address: " + results.getString("architect_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}

	/**
	 * This method enables the user to user to update the contact details of an architect.
	 * <p>
	 * The user chooses if he/she wants to update the architect's number, email or physical address.
	 * Depending on which option he/she chooses, the relevant method is called, which requires an architect ID to
	 * find and update the record.
	 * 
	 * @see UserInterfaceMethods #displayArchitectSummary()
	 * @see PersonMethods #checkIDInt(Scanner, String)
	 * @see PersonMethods #checkIfArchitectIDExists(int)
	 * @see UserInterfaceMethods #architectDetailsToUpdate()
	 * @see PersonMethods #changeArchitectNumber(Scanner, int)
	 * @see PersonMethods #changeArchitectEmail(Scanner, int)
	 * @see PersonMethods #changeArchitectAddress(Scanner, int)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	public static void changeArchitectContactDetails(Scanner userInput){
		/*Call the method displayArchitectSummary in the UserIntefaceMethods class to display a summary of all architects on the system.
		 * This will make it easier for a user to enter which architect ID to edit.*/
		UserInterfaceMethods.displayArchitectSummary();
		System.out.println("");
		
		/*Request the user to enter an architect ID to edit (change number, email address or physical address)*/
		System.out.println("Please enter the ID of the architect you want to edit.");
		System.out.print("Architect ID: ");
		/*Save the user input as a string*/
		String userArchitectID = userInput.nextLine();
		
		/*Check if the input is an integer by calling the checkIDInt method, with the string provided by the user as input to the method.
		 * This method will keep asking the user for an input until the user provides an integer.*/
		int userArchitectIDInt = checkIDInt(userInput, userArchitectID);
		/*Check if the user input an existing project ID, by calling the checkIfArchitectIDExists method. Will return 0 if it doesn't and 1 
		 * if it does.*/
		int doesArchitectIDExists = checkIfArchitectIDExists(userArchitectIDInt);
		
		/*While loop will keep executing as long as the user provides an architect ID that doesn't exist.*/
		while (doesArchitectIDExists == 0) {
			System.out.println("The architect ID entered does not exist. Please enter an existing ID.");
			/*Display the architects again - makes it easier for the user to select an architect ID.*/
			UserInterfaceMethods.displayArchitectSummary();
			System.out.print("Architect ID: ");
			/*Requests input again from the user for an architect ID*/
			userArchitectID = userInput.nextLine();
			/*Checks if the user input is an integer and if the ID exists.*/
			userArchitectIDInt = checkIDInt(userInput, userArchitectID);
			doesArchitectIDExists =  checkIfArchitectIDExists(userArchitectIDInt);
		}
		
		/*Once the architect ID is valid, the different details that can be edited is displayed to the user.*/
		System.out.println("");
		UserInterfaceMethods.architectDetailsToUpdate();
		/*The user is requested to select which contact details should be edited.*/
		System.out.print("Option: ");
		String architectDetailsToEdit = userInput.nextLine();
		/*Boolean variable used to check if the user selected a valid contact detail to edit.*/
		boolean isEditOptionValid = false;
		
		/*Will keep executing as long as the user selects an invalid option to edit*/
		while (isEditOptionValid == false) {
			/*If the user selected option 1 - to edit the architect's number, the changeArchitectNumber method is called
			 * and the boolean variable is set to true. While loop will not be executed again.*/
			if (architectDetailsToEdit.equals("1")) {
				isEditOptionValid = true;
				changeArchitectNumber(userInput, userArchitectIDInt);
			} 
			/*If the user selected option 2 - to edit the architect's email, the changeArchitectEmail method is called
			 * and the boolean variable is set to true. While loop will not be executed again.*/
			else if (architectDetailsToEdit.equals("2")) {
				isEditOptionValid = true;
				changeArchitectEmail(userInput, userArchitectIDInt);
			} 
			/*If the user selected option 3 - to edit the architect's physical address, the changeArchitectAddress method is called
			 * and the boolean variable is set to true. While loop will not be executed again.*/
			else if (architectDetailsToEdit.equals("3")) {
				isEditOptionValid = true;
				changeArchitectAddress(userInput, userArchitectIDInt);
			} 
			/*If any other value other than 1,2 and 3 is entered, an error message is displayed and the user needs to 
			 * input another value. The boolean is kept as false and the while loop is executed again.*/
			else {
				System.out.println("Invalid option, please select a valid option.");
				System.out.println("");
				UserInterfaceMethods.architectDetailsToUpdate();
				System.out.print("Option: ");
				architectDetailsToEdit = userInput.nextLine();
			}
		}	
	}
	
	/**
	 * This methods checks if an architect exists in the database using an architect ID.
	 * <p>
	 * The method will return 1 if the architect is found in the database and 0 if not.
	 * 
	 * @param userDefinedArchitectID integer value that represents the architect's ID.
	 * 
	 * @return returns an integer value, where anything more than zero indicates that the architect is in the database.
	 */
	private static int checkIfArchitectIDExists(int userDefinedArchitectID){
		/*Define an integer value that counts the instances of the architect ID in the database.*/
		int isArchitectIDUnique = 0;

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Define an integer variable where the architect ID will be stored temporarily*/
	        int currentArchitectID;
	        
	        /*Define a string that is used as input to query. Selects all architect IDs form the architects table.*/
	        String inputSQL = "SELECT architect_ID FROM architects";
	        results = statement.executeQuery(inputSQL);
	        
	        /*Loops over all the architects IDs and checks if it is equal to the user defined ID.
	         * If it is, then the integer counter is indexed.*/
	        while (results.next()) {
	        	currentArchitectID = results.getInt("architect_ID");
	        	if (userDefinedArchitectID == currentArchitectID) {
	        		isArchitectIDUnique ++;
	        	}
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
  
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    /*Returns the integer which counts the instances of a specific ID.*/
	    return isArchitectIDUnique;
	}
	/*******************************************************************END OF ARCHITECT METHODS********************************************************************************************/
	
	
	
	
	/*********************************************************************CONTRACTOR METHODS*********************************************************************************************/
	
	/**
	 * This method returns a contractor ID based on a name and number.
	 * <p>
	 * The user provides a contractor name and number which is then used to obtain
	 * the ID of the contractor from the contractors table. If the name and number provided,
	 * cannot be found, a new contractor is created an written to the database.
	 * 
	 * @see PersonMethods #checkIfContractorExists(String, String)
	 * @see PersonMethods #getExitingContractorID(String, String)
	 * @see PersonMethods #nextUniqueContractorID()
	 * @see PersonMethods #createNewContractorAndWriteToDatabase(Scanner, int, String, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @return returns an integer for the contractor's ID in contractors table.
	 */
	public static int determineProjectContractorID(Scanner userInput) {
		/*Please refer to determineProjectArchitectID method for comments*/
		int contractorID = 0;
		
		System.out.print("Contractor's first name: ");
		String contractorName = userInput.nextLine(); 
		
		System.out.print("Contractor's surname: ");
		String contractorSurname = userInput.nextLine(); 
		
		System.out.print("Contractor's number: ");
		String contractorNumber = userInput.nextLine();
		
		String contractorFullName = contractorName + " " + contractorSurname;
		
		int isContractorNew = PersonMethods.checkIfContractorExists(contractorFullName, contractorNumber);
		
		if (isContractorNew > 0) {
			System.out.println("***Contractor is already in the system and has been assigned to the project.***");
			contractorID = PersonMethods.getExitingContractorID(contractorFullName, contractorNumber);
		} else {
			System.out.println("***The contractor does not exist and needs to be created.***");
			System.out.println("Please provide the additional information requested below.");
			contractorID = PersonMethods.nextUniqueContractorID();
			
			PersonMethods.createNewContractorAndWriteToDatabase(userInput, contractorID, contractorNumber, contractorFullName);
		}
		return contractorID;
	}
	
	/**
	 * This method creates a new Person object called projectContractor and writes the new contractor to the contractors table.
	 * <p>
	 * The method calls separate methods to create a Person object and to write it to the database.
	 * 
	 * @see PersonMethods #createNewContractor(String, String, Scanner)
	 * @see PersonMethods #writeNewContractorToDatabase(int, String, String, String, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param contractorID integer that represents the contractor's ID in the contractors table.
	 * @param contractorNumber String that represents the contractor's number in the contractors table.
	 * @param contractorFullName String that represents the contractor's name in the contractors table.
	 */
	private static void createNewContractorAndWriteToDatabase(Scanner userInput, int contractorID, String contractorNumber,
			String contractorFullName) {
		/*Please refer to createNewArchitectAndWriteToDatabase for comments as methods are the same.*/
		Person projectContractor;
		projectContractor = PersonMethods.createNewContractor(contractorFullName, contractorNumber, userInput);
		
		String contractorNameToDatabase = projectContractor.getPersonName();
		String contractorNumberToDatabase = projectContractor.getTelephoneNumber();
		String contractorEmailToDatabase = projectContractor.getEmailAddress();
		String contractorAddressToDatabase = projectContractor.getPhysicalAddress();
		
		PersonMethods.writeNewContractorToDatabase(contractorID, contractorNameToDatabase, contractorNumberToDatabase, contractorEmailToDatabase, contractorAddressToDatabase);
	}

	/**
	 * This method takes a contractor name and number as input and checks if the contractor is in the database.
	 * <p>
	 * If the contractor is in the database the integer 1 is returned, otherwise 0 is returned.
	 * 
	 * @param userContractorFullName String that represents the contractor's full name.
	 * @param userContractorNumber String that represents the contractor's number.
	 *  
	 * @return returns an integer, where 1 represents that the contractor is in the system and 0 represents that the contractor does not exist.
	 */
	private static int checkIfContractorExists(String userContractorFullName, String userContractorNumber) {
		/*Please refer to checkIfArchitectExists for comments as methods are the same.*/
		int isContractorNew = 0;

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String currectContractorFullName;
	        String currentContractorNumber;

	        String inputSQL = "SELECT contractor_name, contractor_number FROM contractors";
	        results = statement.executeQuery(inputSQL);

	        while (results.next()) {
	        	currectContractorFullName = results.getString("contractor_name");
	        	currentContractorNumber = results.getString("contractor_number");
	        	
	        	if (userContractorFullName.equals(currectContractorFullName) && userContractorNumber.equals(currentContractorNumber)) {
	        		isContractorNew ++;
	        	}
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return isContractorNew;
	}
	
	/**
	 * This method returns the contractor ID for an existing contractor in the system.
	 * <p>
	 * The method uses a contractor name and number to look for the ID in the database. 
	 * 
	 * @param contractorName String that represents the contractor's name.
	 * @param contractorNumber String the represents the contractor's number.
	 * 
	 * @return returns an integer that represents the contractor's ID within the database.
	 */
	public static int getExitingContractorID(String contractorName, String contractorNumber) {
		/*Please refer to getExistingArchitectID for comments as the methods are the same.*/
		int existingContractorID = 0;
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "SELECT contractor_ID FROM contractors WHERE contractor_name = '" + contractorName+ "'" + "AND contractor_number = "
	        		+ "'" + contractorNumber + "'";
	        results = statement.executeQuery(inputSQL);
	        
	        if (results.next()){
	        	existingContractorID = results.getInt("contractor_ID");
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
	        
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return existingContractorID;
	}
	
	/**
	 * This method creates a new Person project contractor.
	 * <p>
	 * It takes the contractor name and number as input, additionally requests the email and physical address to create a new
	 * Person object. This is used to create a new contractor Person object.
	 * 
	 * @param contractorName String that represents the contractor's name.
	 * @param contractorNumber String the represents the contractor's number
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @see Person #Person(String, String, String, String)
	 * 
	 * @return returns a Person object - contractor.
	 */
	private static Person createNewContractor(String contractorName, String contractorNumber, Scanner userInput) {
		/*Refer to createNewArchitect for comments as methods are the same*/
		System.out.print("Contractor email address: ");
		String contractorEmail = userInput.nextLine();

		System.out.print("Contractor physical address: ");
		String contractorAddress = userInput.nextLine();
		
		Person newContractor = new Person(contractorName, contractorNumber, contractorEmail, contractorAddress);
		
		return newContractor;
	}
	
	/**
	 * This method writes a new contractor record into the contractors table.
	 * <p>
	 * The fields for the contractor are provided as input to the methods, the database is accessed and 
	 * an update query is performed to write the new contractor to the database.
	 * 
	 * @param contractorID integer that represents the contractor's ID in the contractors table.
	 * @param contractorName String that represents the contractor's name in the contractors table.
	 * @param contractorNumber String that represents the contractor's number in the contractors table.
	 * @param contractorEmail String that represents the contractor's email in the database.
	 * @param contractorAddress String that represents the contractor's address in the database.
	 */
	private static void writeNewContractorToDatabase(int contractorID, String contractorName, String contractorNumber, String contractorEmail, String contractorAddress) {
		/*Refer to writeNewArchitectToDatabase for comments as methods are the same.*/
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        int rowsAffected;
	        
	        String SQLinput = "INSERT INTO contractors VALUES ("+ contractorID + ", '" + contractorName + "','" + contractorNumber + "','" + contractorEmail + "','" + contractorAddress+"')";
	        rowsAffected = statement.executeUpdate(SQLinput);
	        
	        System.out.println("***" + rowsAffected + " new contractor added.***");
	
	        statement.close();
	        connection.close();
	        
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * This method loops through the existing contractors IDs and returns the integer for the next available unique ID.
	 * 
	 * @return returns the integer for the next available unique contractors ID.
	 */
	private static int nextUniqueContractorID() {
		/*Please refer to nextUniquearchitectID for comments as methods are the same.*/
		int uniqueID = 0;
		ArrayList<Integer> projectNumberArray = new ArrayList<Integer>();
		
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "SELECT contractor_ID FROM contractors ORDER BY contractor_ID";     
	        results = statement.executeQuery(inputSQL);
	   
	        while (results.next()) {
	            projectNumberArray.add(results.getInt("contractor_ID"));
	        }
	        
	        uniqueID = Collections.max(projectNumberArray) + 1;
	             
	        results.close();
	        statement.close();
	        connection.close();

		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return uniqueID;
	}
	
	/**
	 * This method creates a Person object using the contractor ID.
	 * <p>
	 * This method uses a contractor ID as input, reads all the field values from the contractors table for the ID and 
	 * creates a new Person object.
	 * 
	 * @see Person #Person(String, String, String, String)
	 * 
	 * @param contractorID integer that represents the contractor's ID in the contractors table.
	 * 
	 * @return returns a Person object (contractor) created from contractor ID.
	 */
	public static Person createContractorPersonObject(int contractorID) {
		/*Please refer to createArchitectPersonObject for comments as methods are the same.*/
		 Person projectContractor = new Person();
		 
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "SELECT contractor_name, contractor_number, contractor_email, contractor_address FROM contractors WHERE contractor_ID = " + contractorID;
	        results = statement.executeQuery(inputSQL);
	
	        if (results.next()) {
	            String contractorName = results.getString("contractor_name");
	            String contractorNumber = results.getString("contractor_number");
	            String contractorEmail = results.getString("contractor_email");
	            String contractorAddress = results.getString("contractor_address");
	            
	            projectContractor = new Person(contractorName, contractorNumber, contractorEmail, contractorAddress);
	        }
	          
	        results.close();
	        statement.close();
	        connection.close();
	        
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return projectContractor;
	}
	
	/**
	 * This method enables the user to edit the number of an existing contractor.
	 * <p>
	 * The method uses the contractor ID to find the record. Requests the user to input the updated number
	 * and updates the number for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param contractorID integer that represents the contractor's ID in the contractor table.
	 */
	private static void changeContractorNumber(Scanner userInput, int contractorID) {
		/*Please refer to changeArchitectNumber for comments as methods are the same.*/
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT contractor_number FROM contractors WHERE contractor_ID = " + contractorID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The contractor's current number is: ");
		        if (results.next()){
		        	System.out.print(results.getString("contractor_number"));
		        }
	            
		        System.out.println("");
		        System.out.print("Please enter the updated number: ");
		        String updatedNumber = userInput.nextLine();
		        
		        inputSQL = "UPDATE contractors SET contractor_number = '" + updatedNumber + "' WHERE contractor_ID =" + contractorID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " contractor details updated.***");
	            
	            inputSQL = "SELECT * FROM contractors WHERE contractor_ID = " + contractorID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated contractor:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Contractor ID: " + results.getInt("contractor_ID"));
		        	System.out.println("Contractor Name: " + results.getString("contractor_name"));
		        	System.out.println("Contractor Number: " + results.getString("contractor_number"));
		        	System.out.println("Contractor Email: " + results.getString("contractor_email"));
		        	System.out.println("Contractor Physical Address: " + results.getString("contractor_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");
	
	            results.close();
	            statement.close();
	            connection.close();
	
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to edit the email of an existing contractor.
	 * <p>
	 * The method uses the contractor ID to find the record. Requests the user to input the updated email
	 * and updates the email for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param contractorID integer that represents the contractor's ID in the contractors table.
	 */
	private static void changeContractorEmail(Scanner userInput, int contractorID) {
		/*Please refer to changeArchitectEmail for comments as methods are the same.*/
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT contractor_email FROM contractors WHERE contractor_ID = " + contractorID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The contractor's current email is: ");
		        if (results.next()){
		        	System.out.print(results.getString("contractor_email"));
		        }

		        System.out.println("");
		        System.out.print("Please enter the updated email: ");
		        String updatedEmail = userInput.nextLine();
		        
		        inputSQL = "UPDATE contractors SET contractor_email = '" + updatedEmail + "' WHERE contractor_ID =" + contractorID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " contractor details updated.***");
	            
	            inputSQL = "SELECT * FROM contractors WHERE contractor_ID = " + contractorID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated contractor:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Contractor ID: " + results.getInt("contractor_ID"));
		        	System.out.println("Contractor Name: " + results.getString("contractor_name"));
		        	System.out.println("Contractor Number: " + results.getString("contractor_number"));
		        	System.out.println("Contractor Email: " + results.getString("contractor_email"));
		        	System.out.println("Contractor Physical Address: " + results.getString("contractor_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to edit the physical address of an existing contractor.
	 * <p>
	 * The method uses the contractor ID to find the record. Requests the user to input the updated address
	 * and updates the address for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param contractorID integer that represents the contractor's ID in the architects table.
	 */
	private static void changeContractorAddress(Scanner userInput, int contractorID) {
		/*Please refer to changeArchitectAddress for comments as methods are the same.*/
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT contractor_address FROM contractors WHERE contractor_ID = " + contractorID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The contractor's current physical address is: ");
		        if (results.next()){
		        	System.out.print(results.getString("contractor_address"));
		        }

		        System.out.println("");
		        System.out.print("Please enter the updated physical address: ");
		        String updatedEmail = userInput.nextLine();
		        
		        inputSQL = "UPDATE contractors SET contractor_address = '" + updatedEmail + "' WHERE contractor_ID =" + contractorID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " contractor details updated.***");
	            
	            inputSQL = "SELECT * FROM contractors WHERE contractor_ID = " + contractorID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated contractor:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Contractor ID: " + results.getInt("contractor_ID"));
		        	System.out.println("Contractor Name: " + results.getString("contractor_name"));
		        	System.out.println("Contractor Number: " + results.getString("contractor_number"));
		        	System.out.println("Contractor Email: " + results.getString("contractor_email"));
		        	System.out.println("Contractor Physical Address: " + results.getString("contractor_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();
   
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to user to update the contact details of a contractor.
	 * <p>
	 * The user chooses if he/she wants to update the contractor's number, email or physical address.
	 * Depending on which option he/she chooses, the relevant method is called, which requires a contractor ID to
	 * find and update the record.
	 * 
	 * @see UserInterfaceMethods #displayContractorSummary()
	 * @see PersonMethods #checkIDInt(Scanner, String)
	 * @see PersonMethods #checkIfContractorIDExists(int)
	 * @see UserInterfaceMethods #contractorDetailsToUpdate()
	 * @see PersonMethods #changeContractorNumber(Scanner, int)
	 * @see PersonMethods #changeContractorEmail(Scanner, int)
	 * @see PersonMethods #changeContractorAddress(Scanner, int)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	public static void changeContractorContactDetails(Scanner userInput){
		/*Please refer to changeArchitectContactDetails for comments as methods are the same.*/
		UserInterfaceMethods.displayContractorSummary();
		System.out.println("");
		
		System.out.println("Please enter the ID of the contractor you want to edit.");
		System.out.print("Contractor ID: ");
		String userContractorID = userInput.nextLine();
		
		int userContractorIDInt = checkIDInt(userInput, userContractorID);
		int doesContractorIDExists =  checkIfContractorIDExists(userContractorIDInt);
		
		while (doesContractorIDExists == 0) {
			System.out.println("The contractor ID entered does not exist. Please enter an existing ID.");
			UserInterfaceMethods.displayContractorSummary();
			System.out.print("Contractor ID: ");
			userContractorID = userInput.nextLine();
			userContractorIDInt = checkIDInt(userInput, userContractorID);
			doesContractorIDExists = checkIfContractorIDExists(userContractorIDInt);
		}
		
		System.out.println("");
		UserInterfaceMethods.contractortDetailsToUpdate();
		System.out.print("Option: ");
		String contractorDetailsToEdit = userInput.nextLine();
		boolean isEditOptionValid = false;
		
		while (isEditOptionValid == false) {
			if (contractorDetailsToEdit.equals("1")) {
				isEditOptionValid = true;
				changeContractorNumber(userInput, userContractorIDInt);
			} else if (contractorDetailsToEdit.equals("2")) {
				isEditOptionValid = true;
				changeContractorEmail(userInput, userContractorIDInt);
			} else if (contractorDetailsToEdit.equals("3")) {
				isEditOptionValid = true;
				changeContractorAddress(userInput, userContractorIDInt);
			} else {
				System.out.println("Invalid option, please select a valid option.");
				System.out.println("");
				UserInterfaceMethods.contractortDetailsToUpdate();
				System.out.print("Option: ");
				contractorDetailsToEdit = userInput.nextLine();
			}
		}
	}
	
	/**
	 * This methods checks if a contractor exists in the database using a contractor ID.
	 * <p>
	 * The method will return 1 if the contractor is found in the database and 0 if not.
	 * 
	 * @param userDefinedContractorID integer value that represents the contractor's ID.
	 * 
	 * @return returns an integer value, where anything more than zero indicates that the contractor is in the database.
	 */
	private static int checkIfContractorIDExists(int userDefinedContractorID){
		/*Please refer to checkIfArchitectIDExists for comments as methods are the same.*/
		int isContractorIDUnique = 0;

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;

	        int currentContractorID;
	        	
	      	String inputSQL = "SELECT contractor_ID FROM contractors";
	        results = statement.executeQuery(inputSQL);

	        while (results.next()) {
	        	currentContractorID = results.getInt("contractor_ID");
	        	if (userDefinedContractorID == currentContractorID) {
	        		isContractorIDUnique ++;
	        	}
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return isContractorIDUnique;
	}
	
	/****************************************************************END OF CONTRACTOR METHODS********************************************************************************/
	
	
	
	
	/**************************************************************************CUSTOMER METHODS******************************************************************************/
	
	/**
	 * This method returns a customer ID based on a name and number.
	 * <p>
	 * The user provides a customer name and number which is then used to obtain
	 * the ID of the customer from the customers table. If the name and number provided,
	 * cannot be found, a new customer is created an written to the database.
	 * 
	 * @see PersonMethods #checkIfCustomerExists(String, String)
	 * @see PersonMethods #getExitingCustomerID(String, String)
	 * @see PersonMethods #nextUniqueCustomerID()
	 * @see PersonMethods #createNewCustomerAndWriteToDatabase(Scanner, int, String, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @return returns an integer for the customerr's ID in customers table.
	 */
	public static int determineProjectCustomerID(Scanner userInput) {
		/*Please refer to determineProjectArchitectID as the methods are the same*/
		int customerID = 0;
		
		System.out.print("Customer's first name: ");
		String customerName = userInput.nextLine(); 
		
		System.out.print("Customer's surname: ");
		String customerSurname = userInput.nextLine(); 

		System.out.print("Customer's number: ");
		String customerNumber = userInput.nextLine();
		
		String customerFullName = customerName + " " + customerSurname;
		
		int isCustomerNew = checkIfCustomerExists(customerFullName, customerNumber);
		
		if (isCustomerNew > 0) {
			System.out.println("***Customer is already in the system and has been assigned to the project.***");
			customerID = PersonMethods.getExitingCustomerID(customerFullName, customerNumber);
		} else {
			System.out.println("***The customer does not exist and needs to be created.***");
			System.out.println("Please provide the additional information requested below.");
			customerID = PersonMethods.nextUniqueCustomerID();
			
			PersonMethods.createNewCustomerAndWriteToDatabase(userInput, customerID, customerNumber, customerFullName);
		}
		return customerID;
	}
	
	/**
	 * This method creates a new Person object called projectCustomer and writes the new customer to the customers table.
	 * <p>
	 * The method calls separate methods to create a Person object and to write it to the database.
	 * 
	 * @see PersonMethods #createNewCustomer(String, String, Scanner)
	 * @see PersonMethods #writeNewCustomerToDatabase(int, String, String, String, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param customerID integer that represents the customer's ID in the customers table.
	 * @param customerNumber String that represents the customer's number in the customers table.
	 * @param customerFullName String that represents the customer's name in the customers table.
	 */
	private static void createNewCustomerAndWriteToDatabase(Scanner userInput, int customerID, String customerNumber,
			String customerFullName) {
		/*Please refer to createNewArchitectAndWriteToDatabase for comments as the methods are the same.*/
		Person projectCustomer;
		projectCustomer = createNewCustomer(customerFullName, customerNumber, userInput);
		
		String customerNameToDatabase = projectCustomer.getPersonName();
		String customerNumberToDatabase = projectCustomer.getTelephoneNumber();
		String customerEmailToDatabase = projectCustomer.getEmailAddress();
		String customerAddressToDatabase = projectCustomer.getPhysicalAddress();
		
		PersonMethods.writeNewCustomerToDatabase(customerID, customerNameToDatabase, customerNumberToDatabase, customerEmailToDatabase, customerAddressToDatabase);
	}

	/**
	 * This method takes a customer name and number as input and checks if the customer is in the database.
	 * <p>
	 * If the customer is in the database the integer 1 is returned, otherwise 0 is returned.
	 * 
	 * @param userCustomerFullName String that represents the customer's full name.
	 * @param userCustomerNumber String that represents the customer's number.
	 *  
	 * @return returns an integer, where 1 represents that the customer is in the system and 0 represents that the customer does not exist.
	 */
	private static int checkIfCustomerExists(String userCustomerFullName, String userCustomerNumber) {
		/*Please refer to checkIfArchitectExists for comments as methods are the same.*/
		int isCustomerNew = 0;

	    try {	        
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String currectCustomerFullName;
	        String currentCustomerNumber;
	        	
	        String inputSQL = "SELECT customer_name, customer_number FROM customers";
	        results = statement.executeQuery(inputSQL);
	        
	        while (results.next()) {
	        	currectCustomerFullName = results.getString("customer_name");
	        	currentCustomerNumber = results.getString("customer_number");
	        	
	        	if (userCustomerFullName.equals(currectCustomerFullName) && userCustomerNumber.equals(currentCustomerNumber)) {
	        		isCustomerNew ++;
	        	}
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return isCustomerNew;
	}
	
	/**
	 * This method returns the customer ID for an existing customer in the system.
	 * <p>
	 * The method uses a customer name and number to look for the ID in the database. 
	 * 
	 * @param customerName String that represents the customer's name.
	 * @param customerNumber String the represents the customer's number.
	 * 
	 * @return returns an integer that represents the customer's ID within the database.
	 */
	public static int getExitingCustomerID(String customerName, String customerNumber) {
		/*Please refer to getExitingArchitectID for comments as methods are the same. */
		int existingCustomerID = 0;
		
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "SELECT customer_ID FROM customers WHERE customer_name = '" + customerName+ "'" + "AND customer_number = "
	        		+ "'" + customerNumber + "'";
	        
	        results = statement.executeQuery(inputSQL);
	        
	        if (results.next()){
	        	existingCustomerID = results.getInt("customer_ID");
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();
	        
		} catch (SQLException e) {
	        // We only want to catch a SQLException - anything else is off-limits for now.
	        e.printStackTrace();
	    }
	    return existingCustomerID;
	}
	
	/**
	 * This method creates a new Person project customer.
	 * <p>
	 * It takes the customer name and number as input, additionally requests the email and physical address to create a new
	 * Person object. This is used to create a new customer Person object.
	 * 
	 * @param customerName String that represents the customer's name.
	 * @param customerNumber String the represents the customer's number
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @see Person #Person(String, String, String, String)
	 * 
	 * @return returns a Person object - customer.
	 */
	private static Person createNewCustomer(String customerName, String customerNumber, Scanner userInput) {
		/*Please refer createNewArchitect for comments as methods are the same.*/
		System.out.print("Customer email address: ");
		String customerEmail = userInput.nextLine();
		
		System.out.print("Customer physical address: ");
		String customerAddress = userInput.nextLine();
		
		Person newCustomer = new Person(customerName, customerNumber, customerEmail, customerAddress);
		
		return newCustomer;
	}
	
	/**
	 * This method writes a new customer record into the customers table.
	 * <p>
	 * The fields for the customer are provided as input to the methods, the database is accessed and 
	 * an update query is performed to write the new customer to the database.
	 * 
	 * @param customerID integer that represents the customer's ID in the customers table.
	 * @param customerName String that represents the customer's name in the customers table.
	 * @param customerNumber String that represents the customer's number in the customers table.
	 * @param customerEmail String that represents the customer's email in the database.
	 * @param customerAddress String that represents the customer's address in the database.
	 */
	private static void writeNewCustomerToDatabase(int customerID, String customerName, String customerNumber, String customerEmail, String customerAddress) {
		/*Please refer to writeNewArchitectToDatabase for comments as methods are the same.*/
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        int rowsAffected;
	        
	        String SQLinput = "INSERT INTO customers VALUES ("+ customerID + ", '" + customerName + "','" + customerNumber + "','" + customerEmail + "','" + customerAddress+"')";
	        rowsAffected = statement.executeUpdate(SQLinput);

	        System.out.println("***" + rowsAffected + " new customer added.***");

	        statement.close();
	        connection.close();
	
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * This method loops through the existing customers IDs and returns the integer for the next available unique ID.
	 * 
	 * @return returns the integer for the next available unique customers ID.
	 */
	private static int nextUniqueCustomerID() {
		/*Please refer to nextUniquearchitectID for comments as methods are the same.*/
		int uniqueID = 0;
		ArrayList<Integer> projectNumberArray = new ArrayList<Integer>();
		
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "SELECT customer_ID FROM customers ORDER BY customer_ID";
	        results = statement.executeQuery(inputSQL);

	        while (results.next()) {
	            projectNumberArray.add(results.getInt("customer_ID"));
	        }
	        
	        uniqueID = Collections.max(projectNumberArray) + 1;
	        
	        results.close();
	        statement.close();
	        connection.close();

		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return uniqueID;
	}
	
	/**
	 * This method creates a Person object using the customer ID.
	 * <p>
	 * This method uses a customer ID as input, reads all the field values from the customers table for the ID and 
	 * creates a new Person object.
	 * 
	 * @see Person #Person(String, String, String, String)
	 * 
	 * @param customerID integer that represents the customer's ID in the customers table.
	 * 
	 * @return returns a Person object (customer) created from customer ID.
	 */
	public static Person createCustomerPersonObject(int customerID) {
		/*Please refer to createArchitectPersonObject for comments as methods are the same.*/
		 Person projectCustomer = new Person();
		 
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;

	        String inputSQL = "SELECT customer_name, customer_number, customer_email, customer_address FROM customers WHERE customer_ID = " + customerID;
	        results = statement.executeQuery(inputSQL);
	
	        if (results.next()) {
	            String customerName = results.getString("customer_name");
	            String customerNumber = results.getString("customer_number");
	            String customerEmail = results.getString("customer_email");
	            String customerAddress = results.getString("customer_address");
	            
	            projectCustomer = new Person(customerName, customerNumber, customerEmail, customerAddress);
	        }

		} catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    return projectCustomer;
	}
	
	/**
	 * This method enables the user to edit the number of an existing customer.
	 * <p>
	 * The method uses the customer ID to find the record. Requests the user to input the updated number
	 * and updates the number for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param customerID integer that represents the customer's ID in the customer table.
	 */
	private static void changeCustomerNumber(Scanner userInput, int customerID) {
		/*Please refer to changeArchitectNumber for comments as methods are the same.*/
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT customer_number FROM customers WHERE customer_ID = " + customerID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The customer's current number is: ");
		        if (results.next()){
		        	System.out.print(results.getString("customer_number"));
		        }

		        System.out.println("");
		        System.out.print("Please enter the updated number: ");
		        String updatedNumber = userInput.nextLine();
		        
		        inputSQL = "UPDATE customers SET customer_number = '" + updatedNumber + "' WHERE customer_ID =" + customerID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " customer details updated.***");
	            
	            inputSQL = "SELECT * FROM customers WHERE customer_ID = " + customerID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated customer:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Customer ID: " + results.getInt("customer_ID"));
		        	System.out.println("Customer Name: " + results.getString("customer_name"));
		        	System.out.println("Customer Number: " + results.getString("customer_number"));
		        	System.out.println("Customer Email: " + results.getString("customer_email"));
		        	System.out.println("Customer Physical Address: " + results.getString("customer_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to edit the email of an existing customer.
	 * <p>
	 * The method uses the customer ID to find the record. Requests the user to input the updated email
	 * and updates the email for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param customerID integer that represents the customer's ID in the customers table.
	 */
	private static void changeCustomerEmail(Scanner userInput, int customerID){
		/*Please refer changeArchitectEmail for comments as methods are the same.*/
	       try {
	            
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT customer_email FROM customers WHERE customer_ID = " + customerID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The customer's current email is: ");
		        if (results.next()){
		        	System.out.print(results.getString("customer_email"));
		        }
	            
		        System.out.println("");
		        System.out.print("Please enter the updated email: ");
		        String updatedEmail = userInput.nextLine();
		        
		        inputSQL = "UPDATE customers SET customer_email = '" + updatedEmail + "' WHERE customer_ID =" + customerID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " customer details updated.***");
	            
	            inputSQL = "SELECT * FROM customers WHERE customer_ID = " + customerID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated customer:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Customer ID: " + results.getInt("customer_ID"));
		        	System.out.println("Customer Name: " + results.getString("customer_name"));
		        	System.out.println("Customer Number: " + results.getString("customer_number"));
		        	System.out.println("Customer Email: " + results.getString("customer_email"));
		        	System.out.println("Customer Physical Address: " + results.getString("customer_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to edit the physical address of an existing customer.
	 * <p>
	 * The method uses the customer ID to find the record. Requests the user to input the updated address
	 * and updates the address for the specific ID.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param customerID integer that represents the customer's ID in the architects table.
	 */
	private static void changeCustomerAddress(Scanner userInput, int customerID){
	    /*Please refer to changeArchitectAddress for comments as methods are the same.*/   
		try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            String inputSQL = "SELECT customer_address FROM customers WHERE customer_ID = " + customerID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.print("The customer's current physical address is: ");
		        if (results.next()){
		        	System.out.print(results.getString("customer_address"));
		        }
	            
		        System.out.println("");
		        System.out.print("Please enter the updated physical address: ");
		        String updatedEmail = userInput.nextLine();
		        
		        inputSQL = "UPDATE customers SET customer_address = '" + updatedEmail + "' WHERE customer_ID =" + customerID;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            
	            System.out.println("");
	            System.out.println("***" + rowsAffected + " customer details updated.***");
	            
	            inputSQL = "SELECT * FROM customers WHERE customer_ID = " + customerID;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated customer:");
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Customer ID: " + results.getInt("customer_ID"));
		        	System.out.println("Customer Name: " + results.getString("customer_name"));
		        	System.out.println("Customer Number: " + results.getString("customer_number"));
		        	System.out.println("Customer Email: " + results.getString("customer_email"));
		        	System.out.println("Customer Physical Address: " + results.getString("customer_address"));
		        }
		        System.out.println("");
		        System.out.println("*****************************************************************************");

	            results.close();
	            statement.close();
	            connection.close();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method enables the user to user to update the contact details of a customer.
	 * <p>
	 * The user chooses if he/she wants to update the customer's number, email or physical address.
	 * Depending on which option he/she chooses, the relevant method is called, which requires a customer ID to
	 * find and update the record.
	 * 
	 * @see UserInterfaceMethods #displayCustomerSummary()
	 * @see PersonMethods #checkIDInt(Scanner, String)
	 * @see PersonMethods #checkIfCustomerIDExists(int)
	 * @see UserInterfaceMethods #customerDetailsToUpdate()
	 * @see PersonMethods #changeCustomerNumber(Scanner, int)
	 * @see PersonMethods #changeCustomerEmail(Scanner, int)
	 * @see PersonMethods #changeCustomerAddress(Scanner, int)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	public static void changeCustomerContactDetails(Scanner userInput){
		/*Please refer to changeArchitectContactDetails for comments as methods are the sames*/
		UserInterfaceMethods.displayCustomerSummary();
		System.out.println("");
		
		System.out.println("Please enter the ID of the customer you want to edit.");
		System.out.print("Customer ID: ");
		String userCustomerID = userInput.nextLine();
		
		int userCustomerIDInt = checkIDInt(userInput, userCustomerID);
		int doesCustomerIDExists =  checkIfCustomerIDExists(userCustomerIDInt);
		
		while (doesCustomerIDExists == 0) {
			System.out.println("The customer ID entered does not exist. Please enter an existing ID.");
			UserInterfaceMethods.displayCustomerSummary();
			System.out.print("Coustomer ID: ");
			userCustomerID = userInput.nextLine();
			userCustomerIDInt  = checkIDInt(userInput, userCustomerID);
			doesCustomerIDExists = checkIfCustomerIDExists(userCustomerIDInt);
		}
		
		System.out.println("");
		UserInterfaceMethods.customertDetailsToUpdate();
		System.out.print("Option: ");
		String customerDetailsToEdit = userInput.nextLine();
		boolean isEditOptionValid = false;
		
		while (isEditOptionValid == false) {
			if (customerDetailsToEdit.equals("1")) {
				isEditOptionValid = true;
				changeCustomerNumber(userInput, userCustomerIDInt);
			} else if (customerDetailsToEdit.equals("2")) {
				isEditOptionValid = true;
				changeCustomerEmail(userInput, userCustomerIDInt);
			} else if (customerDetailsToEdit.equals("3")) {
				isEditOptionValid = true;
				changeCustomerAddress(userInput, userCustomerIDInt);
			} else {
				System.out.println("Invalid option, please select a valid option.");
				System.out.println("");
				UserInterfaceMethods.customertDetailsToUpdate();
				System.out.print("Option: ");
				customerDetailsToEdit = userInput.nextLine();
			}
		}
	}

	/**
	 * This methods checks if a contractor exists in the database using a customer ID.
	 * <p>
	 * The method will return 1 if the customer is found in the database and 0 if not.
	 * 
	 * @param userDefinedCustomerID integer value that represents the customer's ID.
	 * 
	 * @return returns an integer value, where anything more than zero indicates that the customer is in the database.
	 */
	public static int checkIfCustomerIDExists(int userDefinedCustomerID){
		/*Please refer to checkIfArchitectIDExists for comments as methods are the same.*/
		int isCustomerIDUnique = 0;

	    try {    
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	      
	        Statement statement = connection.createStatement();
	        ResultSet results;

	        int currentCustomerID;
	        
	        String inputSQL = "SELECT customer_ID FROM customers";
	        results = statement.executeQuery(inputSQL);

	        while (results.next()) {
	        	currentCustomerID = results.getInt("customer_ID");
	        	if (userDefinedCustomerID == currentCustomerID) {
	        		isCustomerIDUnique ++;
	        	}
	        }
	        
	        results.close();
	        statement.close();
	        connection.close();

	    } catch (SQLException e) {
	        // We only want to catch a SQLException - anything else is off-limits for now.
	        e.printStackTrace();
	    }
	    return isCustomerIDUnique;
	}
/**************************************************************************END OF CUSTOMER METHODS*******************************************************************/


/**************************************************************************GENERAL METHODS*******************************************************************/
	/**
	 * This method checks if the string that the user provided can be used as a valid ID integer.
	 * <p>
	 * The method uses parsing to check if the user provided a valid integer. If it is not a valid integer, 
	 * the user is requested to provide a different input.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param personID String that represents the ID of a person (architect, contractor, customer)
	 * 
	 * @return returns the valid integer for the person ID.
	 */
	
	public static int checkIDInt(Scanner userInput, String personID) {
		/*Define an integer variable and initialize it. The converted string integer will be stored in this variable.*/
		int IDInt = 0;
		
		/*Define a boolean that will be set to true if the string can successfully be converted to an integer.*/
		boolean isIDInt = false;
		
		/*The while loop will keep executing as long as the string cannot be converted to an integer.*/
		while (isIDInt == false){
			/*If the string can be converted to an integer, save the integer in the int variable and set the boolean to true.
			 * The while loop will exited.*/
			try{
				IDInt = Integer.parseInt(personID);
				isIDInt = true;
			}
			/*If the string cannot be converted to an integer, an error message is displayed and input is requested from the user.*/
			catch (Exception e){
				System.out.println("The value entered is not an integer, please enter a valid integer");
				System.out.print("ID: ");
				personID = userInput.nextLine();
			}
		}
		/*Return the string value as an integer.*/
		return IDInt;
	}
	
	/*************************************************************************END OF GENERAL METHODS********************************************************************/
}
