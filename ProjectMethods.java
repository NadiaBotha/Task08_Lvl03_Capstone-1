import java.util.Scanner;
import java.sql.*;
import java.text.SimpleDateFormat;  
import java.util.Date; 

/**
 * This class groups all methods used to create and modify Project objects.
 * 
 * @author Nadia Botha
 * @version 1.0
 */
public class ProjectMethods {
	
	/**
	 * This method checks if a project number provided by the user is unique.
	 * <p>
	 * The method returns an integer 0 or 1. 0 means that the project number provided is unique, 
	 * and 1 means that the number provided is already in the system.
	 * 
	 * @param userDefinedProjectNumber integer that represents the project number in the database.
	 * 
	 * @return returns an integer, where 0 means the project number is unique and 1 that it is a duplicate.
	 */
	private static int checkIfProjectNumberIsUnique(int userDefinedProjectNumber) {
		/*Define an integer counter that counts the instances of a project number in the database.
		 * Initialize the variable.*/
		int isProjectNumberUnique = 0;
		
		/*Try block connects to the database poisepms and performs queries and/or updates.*/
        try {
            
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
                    "otheruser",
                    "swordfish"
                    );
          
            Statement statement = connection.createStatement();
            ResultSet results;
            
            /*Define an integer variable to temporarily store the project number for each record in projects.*/
            int currentProjectNumber;
            
            /*Define a string which is used as input to the SQL. Select all project_numbers from the projects table.*/
            String inputSQL = "SELECT project_number FROM projects";
            results = statement.executeQuery(inputSQL);
            
            /*Loop through the records and check if the project number in the database is equal to the one provided by the user.
             * If they are equal index the counter with 1.*/
            while (results.next()) {
            	currentProjectNumber = results.getInt("project_number");
            	if (userDefinedProjectNumber == currentProjectNumber) {
            		isProjectNumberUnique ++;
            	}
            }
            
            /*Close all SQL objects.*/
            results.close();
            statement.close();
            connection.close();
      
        } 
        /*Catch block is executed if any errors occur when trying to connect to the database or when any queries/updates are executed.*/
        catch (SQLException e) {
            e.printStackTrace();
        }
        /*Return the integer counter value - instances of how many times the project number was found in the database.*/
        return isProjectNumberUnique;
	}

	/**
	 * This method checks if a project number provided by the user is a valid integer.
	 * <p>
	 * The input from the user is saved in a string. The string is then parsed to an integer. If it cannot be
	 * converted to an integer, an error message is displayed and the user needs to provide an different input.
	 * The string is returned as an integer data type.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param newProjNum String value that represents the project number to be converted to an integer.
	 * 
	 * @return returns the integer value of the user string for the project number.
	 */
	private static int chekcIfProjNumberInt(Scanner userInput, String newProjNum) {
		/*Define an integer variable to store the converted string an initialize it.*/
		int projNumInt = 0;
		
		/*Define a boolean variable, which is equal to true if the string can be converted to
		 * an integer. It is false for vice versa.*/
		boolean isProjNumInt = false;
		
		/*The while loop will keep executing as long as the user input cannot be converted to an integer.*/
		while (isProjNumInt == false) {
			/*If the string can be converted to an integer, save the parsed value in the integer variable projNumInt
			 * and set the boolean variable to true so that the while loop is exited.*/
			try {
				projNumInt = Integer.parseInt(newProjNum);
				isProjNumInt = true;
			}
			/*If the string value cannot be converted to an integer, display an error message and request the user
			 * to input a different value. Execute the while loop again.*/
			catch (Exception e) {
				System.out.println("The value entered is not an integer, please enter a valid integer");
				System.out.print("Project Number: ");
				newProjNum = userInput.nextLine();
			}
		}
		/*Return the converted string value as an integer*/
		return projNumInt;
	}
	
	/**
	 * This method displays all existing project numbers that are currently in the database, along with the project name.
	 */
	private static void displayExistingProjectNumbers() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
                    "otheruser",
                    "swordfish"
                    );

            Statement statement = connection.createStatement();
            ResultSet results;
           
            /*Define a string that is used as input to a SQL query. Select all project numbers and project names from the projects table.*/
            String inputSQL = "SELECT project_number, project_name FROM projects";
            results = statement.executeQuery(inputSQL);
            
            /*Loop through the query results and print each record's number and name on a different line.*/
            System.out.println("Project Number  Project Name");
            while (results.next()) {
                System.out.println("     " + results.getString("project_number") + "          " + results.getString("project_name"));
            }
            
            statement.close();
            connection.close();
            
        }  catch (SQLException e) {
        	e.printStackTrace();
        }
	}

	/**
	 * This method requests the user to enter all project details and creates a new Project object.
	 * <p>
	 * The method also checks the validity of the user input, for example if the project number entered is valid
	 * and unique. It also automatically generates a name form the building type and customer surname is no 
	 * name is provided.
	 * 
	 * @see ProjectMethods #displayExistingProjectNumbers()
	 * @see ProjectMethods #chekcIfProjNumberInt(Scanner, String)
	 * @see ProjectMethods #checkIfProjectNumberIsUnique(int)
	 * @see ProjectMethods #checkIfAddressIsNew(String)
	 * @see ProjectMethods #getERFForAddress(String)
	 * @see PersonMethods #determineProjectArchitectID(Scanner)
	 * @see PersonMethods #determineProjectContractorID(Scanner)
	 * @see PersonMethods #determineProjectCustomerID(Scanner)
	 * @see PersonMethods #createArchitectPersonObject(int)
	 * @see PersonMethods #createContractorPersonObject(int)
	 * @see PersonMethods #createCustomerPersonObject(int)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * 
	 * @return returns the Project object created from the user input.
	 */
	public static Project createNewProject(Scanner userInput) {
		/*Requests the user to input the details for the new project.*/
		System.out.println("");
		System.out.println("Please enter the details for the project");
		System.out.println("");
		
		/*Request the user to input a project number.*/
		System.out.print("Project number(must be unique): ");
		String newProjNum = userInput.nextLine();
		/*Check if the project number is an integer by calling method chekcIfProjNumberInt. Method checks it and requests the 
		 * user to input a different value if it is not an integer.*/
		int validNewProjNum = chekcIfProjNumberInt(userInput, newProjNum);
		/*Checks if the input project number is unique. If it is, the integer will be zero, otherwise it will be 1.*/
		int isProjNumUnique = checkIfProjectNumberIsUnique(validNewProjNum);
		
		/*If the project number provided is not unique, the while loop will be executed. It requests the user to provide a different value,
		 * unitl the value is a valid integer and is unique.*/
		while (isProjNumUnique != 0) {
			System.out.println("");
			System.out.println("The project number entered is not unique, please enter a different number.");
			System.out.println("");
			System.out.println("Would you like to see existing projects? (y/n)");
			String viewExistingProjects = userInput.nextLine();
			
			/*If the value provided by the user is not unique, provide the user to option to view existing projects.
			 * Will be easier to see which project numbers have been taken.*/
			if (viewExistingProjects.equals("y")) {
				System.out.println("-------------------------------------");
				displayExistingProjectNumbers();
				System.out.println("-------------------------------------");
			}
			
			System.out.println("");
			System.out.print("Please enter the unique project number here: ");
			newProjNum = userInput.nextLine();
			validNewProjNum = chekcIfProjNumberInt(userInput, newProjNum);
			isProjNumUnique = checkIfProjectNumberIsUnique(validNewProjNum);
		}
		
	    /* Rest of field values are requested from the user.*/
        System.out.print("Enter project name (optional): ");
        String userProjectName = userInput.nextLine();

        System.out.print("Enter building type: ");
        String userBuildingType = userInput.nextLine();

        /*Once the address has been provided, a check is done to see if it already exists in the database.
         * If it does, the ERF number is pulled from the erf table. Otherwise an ERF number is requested from the user.
         * There cannot be duplicate physical addresses in the erf table, because the physical address is a primary key.*/
        System.out.print("Enter the physical address: ");
        String userPhysicalAddress = userInput.nextLine();
        
        /*An integer variable is defined and set equal to the checkIfAddressIsNew method. It is equal to 0 if
         * the address is new, otherwise it is equal to 1.*/
        int isProjectAddressExisting = checkIfAddressIsNew(userPhysicalAddress);
        
        /*String is defined and initialized to store the value of the ERF number.*/
        String userERFNumber = "";
        
        /*If the address is already in the database, call the getERFForAddress, which obtains the ERF number 
         * for the specific address in the database and stores the value in the string erf variable defined above.*/
        if(isProjectAddressExisting > 0) {
        	userERFNumber = getERFForAddress(userPhysicalAddress);
        } 
        /*If the address is new, request the user to enter a new ERF number. It is assumed that every address can only
         * have 1 erf number and vice versa.*/
        else {
            System.out.print("Enter the ERF Number: ");
            userERFNumber = userInput.nextLine();
        }

        System.out.print("Enter the total fee payable: ");
        String userPayable = userInput.nextLine();

        System.out.print("Enter the total paid to date: ");
        String userPaid = userInput.nextLine();

        System.out.print("Enter the project deadline (yyyy-mm-dd): ");
        String userDeadline = userInput.nextLine();
        
        /*Requests the user to input the name and number for the project architect. The methods below are called, which checks 
         * if the architect is already in the system. If it is, the details are obtained from the database and a new Person object is created
         * form it. This Person object is later used in the Project constructor.*/
        int projectArchitectID = PersonMethods.determineProjectArchitectID(userInput);
        Person projectArchitect = PersonMethods.createArchitectPersonObject(projectArchitectID);
         
        /*The same code and logic for the architect is applied to the contractor and customer.*/
        int projectContractorID = PersonMethods.determineProjectContractorID(userInput);
        Person projectContractor = PersonMethods.createContractorPersonObject(projectContractorID);
       
        int projectCustomerID = PersonMethods.determineProjectCustomerID(userInput);
        Person projectCustomer = PersonMethods.createCustomerPersonObject(projectCustomerID);
        
        /*The Project constructor only takes strings as inputs. First convert int to Integer and then 
         * convert the Integer to a String.*/
        Integer projectNumberInteger = new Integer(validNewProjNum);
        String projectNumberToString = projectNumberInteger.toString();
        
        /*If no project name was provided, generate one from the building type and the customer surname.*/
        int projectNameGiven = userProjectName.length();
        
        if (projectNameGiven == 0) {
	        String customerNameAndSurname = projectCustomer.getPersonName();
	        String [] customerNameArray = customerNameAndSurname.split(" ");
	        userProjectName = userBuildingType + " " + customerNameArray[1];
        }
        
        /*Create a Project object by calling the first constructor. Use all of the variables above to create the object.*/
        Project newProject = new Project(projectNumberToString, userProjectName, userBuildingType, 
        		userPhysicalAddress, userERFNumber, userPayable, userPaid, userDeadline,  projectArchitect, 
        		projectContractor, projectCustomer, "Incomplete", "NULL");
        
        /*Return the Project object created above.*/
        return newProject;
	}
	
	/**
	 * This method takes a Project object as input and writes it to the database.
	 * 
	 * @see Project #getProjectNumber()
	 * @see Project #getProjectName()
	 * @see Project #getBuildingType()
	 * @see Project #getProjectFee()
	 * @see Project #getProjectTotalPaid()
	 * @see Project #getAddress()
	 * @see Project #getERFNumber()
	 * @see Project #getProjectArchtect()
	 * @see Project #getProjectContractor()
	 * @see Project #getProjectCustomer()
	 * @see Project #getProjectDeadline()
	 * @see Person #getPersonName()
	 * @see Person #getTelephoneNumber()
	 * @see PersonMethods #getExitingArchitectID(String, String)
	 * @see PersonMethods #getExitingContractorID(String, String)
	 * @see PersonMethods #getExitingCustomerID(String, String)
	 * @see Project #getProjectStatus()
	 * @see Project #setprojectCompletionDate(String)
	 * @see ProjectMethods #checkIfAddressIsNew(String)
	 * 
	 * 
	 * @param newProject Project object with all the fields needed to define a project.
	 */
	public static void createAndWriteNewProjectToDatabase(Project newProject){
		
		/*Call getter methods for each field and save the values in a string variable.*/
		String projectNumber = newProject.getProjectNumber();
		String projectName = newProject.getProjectName();
		String projectBuildingType = newProject.getBuildingType();
		String projectAddress = newProject.getAddress();
		String ERFNumber = newProject.getERFNumber();
		String totalFee = newProject.getProjectFee();
		String totalPaid = newProject.getProjectTotalPaid();
		String projectDeadline = newProject.getProjectDeadline();
		String projectArchitectName = newProject.getProjectArchtect().getPersonName();
		String projectArchitectNumber = newProject.getProjectArchtect().getTelephoneNumber();
		
		/*Use the architect name and number obtained in the step above to find the ID in the database.
		 *The getExitingArchitectID method will create a new ID if the architect is new. */
		int projectArchitectID = PersonMethods.getExitingArchitectID(projectArchitectName, projectArchitectNumber);
	
		String projectContractorName = newProject.getProjectContractor().getPersonName();
		String projectContractorNumber = newProject.getProjectContractor().getTelephoneNumber();
		
		/*The same steps are followed for the contractor and customer than the architect.*/
		int projectContractorID = PersonMethods.getExitingContractorID(projectContractorName, projectContractorNumber);
		
		String projectCustomerName = newProject.getProjectCustomer().getPersonName();
		String projectCutomerNumber = newProject.getProjectCustomer().getTelephoneNumber();
		
		int projectCustomerID = PersonMethods.getExitingCustomerID(projectCustomerName, projectCutomerNumber);
		
		String projectStatus = newProject.getProjectStatus();
		String projectCompletionDate = newProject.getProjectCompletionDate();

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();

	        int rowsAffected;
	        
	        /*Define an integer variable to store the amount of time an address is counted in the database.*/
	        int isAddressNew =  checkIfAddressIsNew(projectAddress);
	        
	        /*If the address is not new, display a message that the ERF number is already obtained.*/
	        if (isAddressNew > 0) {
	        	System.out.println("Address is already in the system and ERF number is allocated.");
	        } 
	        /*If the address is new, write the address and erf number to the erf table first.*/
	        else{
	        	/*Define a string that is used as input to a SQL update query. Insert the new address and erf number into erf table.*/
		        String SQLinputERF = "INSERT INTO erf VALUES ('" + projectAddress + "','"+ ERFNumber + "' )";
		        rowsAffected = statement.executeUpdate(SQLinputERF);
		        
		        /*Display a message that the update was successful*/
		        System.out.println("***" + rowsAffected + " new ERF added.***");
	        }
	        
	        /*Input string to SQL update query. Select the fields as seen below and write the new project to projects table.*/
	        String SQLinputProjects = "INSERT INTO projects VALUES (" + projectNumber + ", '" + projectName + "', '" + projectBuildingType + "', '" + projectAddress + "', " + totalFee + ", " + totalPaid
	        		+ ", " + projectArchitectID + ", " + projectContractorID + ", " + projectCustomerID +  ", '" + projectDeadline + "', '" + projectStatus+ "', " + projectCompletionDate + ")"  ;	    	
	        rowsAffected = statement.executeUpdate(SQLinputProjects);
	        
	        /*Display a message that the table has been succesfully updated.*/
	        System.out.println("***" + rowsAffected + " new project added.***");
	        System.out.println(""); 

	        statement.close();
	        connection.close();

		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * Method checks if a physical address is unique or is already existing in the system.
	 * <p>
	 * The physical address is a primary key in the erf table and therefore cannot be added to the database more than
	 * once.
	 * 
	 * @param projectAddress String representing the physical address of the project.
	 * 
	 * @return returns an integer 0 if the address is not in the database and 1 if it is.
	 */
	private static int checkIfAddressIsNew(String projectAddress) {
		 /*Refer to the comments in checkIfProjectNumberIsUnique as both methods use the same logic and code.*/
		int isAddressNew = 0;

        try {
            
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
                    "otheruser",
                    "swordfish"
                    );
          
            Statement statement = connection.createStatement();
            ResultSet results;
            
            String currentProjectAddress;
            	
            String inputSQL = "SELECT physical_address FROM erf";
            results = statement.executeQuery(inputSQL);
 
            while (results.next()) {
            	currentProjectAddress= results.getString("physical_address");
            	if (projectAddress.equals(currentProjectAddress)) {
            		isAddressNew ++;
            	}
            }
            
            results.close();
            statement.close();
            connection.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAddressNew;
	}
	
	/**
	 * This method obtains the ERF Number for an existing physical address.
	 * 
	 * @param projectAddress String representing the physical address of the project.
	 * 
	 * @return returns a string for the ERF Number.
	 */
	private static String getERFForAddress(String projectAddress) {
		/*Define a string to store the ERF number for a specific physical address*/
		String existingAddressERF = "";
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;
	        /*Define a string used for input into SQL query. Select the ERF for a specific physical address.*/
	        String inputSQL = "SELECT ERF_Number FROM erf WHERE physical_address = '" + projectAddress + "'";
	        results = statement.executeQuery(inputSQL);
	        
	        /*Assign the result equal to the empty string variable existingAddressERF*/
	        if (results.next()){
	        	existingAddressERF = results.getString("ERF_Number");
	        }
	        
            results.close();
            statement.close();
            connection.close();
	
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
	    /*Return the ERF string value.*/
	    return existingAddressERF;
	}
	
	/** 
	 * This method enables the user to edit a existing projects and/or people.
	 * <p>
	 * The user can update the project fee, project total paid, project deadline as well as the
	 * contact details for the architect, customer and contractor. 
	 * 
	 * @see ProjectMethods #editProjectFeeCharged(Scanner)
	 * @see ProjectMethods #editProjectPaidToDate(Scanner)
	 * @see ProjectMethods #changeProjectDeadline(Scanner)
	 * @see PersonMethods #changeArchitectContactDetails(Scanner)
	 * @see PersonMethods #changeContractorContactDetails(Scanner)
	 * @see PersonMethods #changeCustomerContactDetails(Scanner)
	 * @see UserInterfaceMethods #getEditOption(Scanner)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	public static void editProjectDetails(Scanner userInput){
		/*Display which options to edit and assign a valid integer value to int variable.*/
		int optionToEdit = UserInterfaceMethods.getEditOption(userInput);
		
		/*If the user chose option 1 - the editProjectFeeCharged method is called to update the project fee*/
		if (optionToEdit == 1)  {
			editProjectFeeCharged(userInput);
		} 
		/*If the user chose option 2 - the editProjectPaidToDate method is called to update the project paid to date*/
		else if (optionToEdit == 2) {
			editProjectPaidToDate(userInput);
		} 
		/*If the user chose option 3 - the changeProjectDeadline method is called to update the project deadline*/
		else if (optionToEdit == 3) {
			changeProjectDeadline(userInput);
		} 
		/*If the user chose option 4 - the changeArchitectContactDetails method is called to update an architect's details*/
		else if (optionToEdit == 4) {
			PersonMethods.changeArchitectContactDetails(userInput);
		} 
		/*If the user chose option 5 - the changeContractorContactDetails method is called to update a contractor's details*/
		else if (optionToEdit == 5) {
			PersonMethods.changeContractorContactDetails(userInput);
		} 
		/*If the user chose option 6 - the changeCustomerContactDetails method is called to update a customer's details*/
		else {
			PersonMethods.changeCustomerContactDetails(userInput);
		}
	}
	
	/**
	 * This method edits an existing project's fee charged.
	 * <p>
	 * The specific project is found based on the project number which is entered by the user. The new
	 * fee is requested and the record is updated in the database.
	 * 
	 * @see UserInterfaceMethods #displayProjectSummary()
	 * @see ProjectMethods #checkIfProjectNumberIsUnique(int)
	 * @see ProjectMethods #chekcIfProjNumberInt(Scanner, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	private static void editProjectFeeCharged(Scanner userInput) {
		/*A project summary is printed to the user which shows every project number and project name within the database.*/
		UserInterfaceMethods.displayProjectSummary();
		
		/*Requests the user to provide the project number that they would like to update.*/
		System.out.println("Please enter the project number you want to edit.");
		System.out.print("Project Number: ");
		String userProjectNumberString = userInput.nextLine();
		
		/*Check if the input the user provided is valid (is an integer and an existing project number)
		 * If the input is invalid, the user is requested to input another value.*/
		int userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
		
		int doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);

		while (doesProjectExist == 0) {
			System.out.println("The project number entered does not exist. Please enter an existing project number.");
			UserInterfaceMethods.displayProjectSummary();
			System.out.print("Project Number: ");
			userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
			doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);
		}

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );

	        Statement statement = connection.createStatement();
	        ResultSet results;
	        int rowsAffected;

	        /*Define string used as SQL query input. Select project total fee for a specific project number.*/
	        String inputSQL = "SELECT project_total_fee FROM projects WHERE project_number =" + userProjectNumberInt;
	        results = statement.executeQuery(inputSQL);
	        
	        /*Display the current total fee value, obtained from the database.*/
	        System.out.println("");
	        System.out.print("The current total fee for the project is: R");
	        if (results.next()){
	        	System.out.print(results.getString("project_total_fee"));
	        }
	        
	        /*Request the user to input the new value.*/
	        System.out.println("");
	        System.out.print("Please enter the updated project total fee: R ");
	        String updatedProjectFee = userInput.nextLine();
	        System.out.println("");
	        
	        /*Check if the value for the updated fee is valid by parsing the input string to a double.*/
	        boolean isFeeDouble = false;
	        Double updatedFeeDouble = 0.00;
	        
	        while (isFeeDouble == false) {
		        try {
		        	updatedFeeDouble = Double.parseDouble(updatedProjectFee);
		        	isFeeDouble = true;
		        } 
		        /*If the string cannot be parsed, request the user to input another value.*/
		        catch (Exception e) {
		        	System.out.println("Value entered is not valid, please enter a valid monetary value");
		        	System.out.print("R: ");
			        updatedProjectFee = userInput.nextLine();
		        }
	        }
	        
            /*SQL input string for an update query. Update the total fee to the new value for the specific project number.*/
	        inputSQL = "UPDATE projects SET project_total_fee =" + updatedFeeDouble + " WHERE project_number = " + userProjectNumberInt;
            rowsAffected = statement.executeUpdate(inputSQL);
	        System.out.println("***" + rowsAffected + " project updated.***");
	        
            /*Select all the field values for the specific project number and display the updated project*/
            inputSQL = "SELECT * FROM projects WHERE project_number = " + userProjectNumberInt;
            results = statement.executeQuery(inputSQL);
            
            System.out.println("");
            System.out.println("*****************************************************************************");
            System.out.println("");
            System.out.println("Updated project:");
            System.out.println("");
	        if (results.next()){
	        	System.out.println("Project Number: " + results.getString("project_number"));
	        	System.out.println("Project Name: " + results.getString("project_name"));
	        	System.out.println("Building type: " + results.getString("building_type"));
	        	System.out.println("Physical address: " + results.getString("physical_address"));
	        	System.out.println("Project Total Fee: " + results.getString("project_total_fee"));
	        	System.out.println("Project Total Paid: " + results.getString("project_total_paid"));
	        	System.out.println("Project Deadline: " + results.getString("project_deadline"));
	        	System.out.println("Project Status: " + results.getString("project_status"));
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
	 * This method edits an existing project's total paid.
	 * <p>
	 * The specific project is found based on the project number which is entered by the user. The new
	 * total paid is requested and the record is updated in the database.
	 * 
	 * @see UserInterfaceMethods #displayProjectSummary()
	 * @see ProjectMethods #checkIfProjectNumberIsUnique(int)
	 * @see ProjectMethods #chekcIfProjNumberInt(Scanner, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	private static void editProjectPaidToDate(Scanner userInput) {
		/*Please refer to the editProjectFeeCharged for comments as the methods are the same.*/
		UserInterfaceMethods.displayProjectSummary();
		
		System.out.println("Please enter the project number you want to edit.");
		System.out.print("Project Number: ");
		String userProjectNumberString = userInput.nextLine();
		
		int userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
		
		int doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);

		while (doesProjectExist == 0) {
			System.out.println("The project number entered does not exist. Please enter an existing project number.");
			UserInterfaceMethods.displayProjectSummary();
			System.out.print("Project Number: ");
			userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
			doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);
		}

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	    
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        int rowsAffected;

	        String inputSQL = "SELECT project_total_paid FROM projects WHERE project_number =" + userProjectNumberInt;
	        results = statement.executeQuery(inputSQL);
	        
	        System.out.println("");
	        System.out.print("The current total paid for the project is: R ");
	        if (results.next()) {
	        	System.out.print(results.getString("project_total_paid"));
	        }
	        
	        System.out.println("");
	        System.out.print("Please enter the updated project total paid: R ");
	        String updatedProjectFee = userInput.nextLine();
	        System.out.println("");
	        
	        boolean isFeeDouble = false;
	        Double updatedFeeDouble = 0.00;
	        
	        while (isFeeDouble == false) {
		        try {
		        	updatedFeeDouble = Double.parseDouble(updatedProjectFee);
		        	isFeeDouble = true;
		        } catch (Exception e) {
		        	System.out.println("Value entered is not valid, please enter a valid monetary value");
		        	System.out.print("R: ");
			        updatedProjectFee = userInput.nextLine();
		        }
	        }
	        
	        inputSQL = "UPDATE projects SET project_total_paid =" + updatedFeeDouble + " WHERE project_number = " + userProjectNumberInt;
            rowsAffected = statement.executeUpdate(inputSQL);
            
	        System.out.println("***" + rowsAffected + " project updated.***");

            inputSQL = "SELECT * FROM projects WHERE project_number = " + userProjectNumberInt;
            results = statement.executeQuery(inputSQL);
            
            System.out.println("");
            System.out.println("*****************************************************************************");
            System.out.println("");
            System.out.println("Updated project:");
            System.out.println("");
	        if (results.next()) {
	        	System.out.println("Project Number: " + results.getString("project_number"));
	        	System.out.println("Project Name: " + results.getString("project_name"));
	        	System.out.println("Building type: " + results.getString("building_type"));
	        	System.out.println("Physical address: " + results.getString("physical_address"));
	        	System.out.println("Project Total Fee: " + results.getString("project_total_fee"));
	        	System.out.println("Project Total Paid: " + results.getString("project_total_paid"));
	        	System.out.println("Project Deadline: " + results.getString("project_deadline"));
	        	System.out.println("Project Status: " + results.getString("project_status"));
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
	 * This method edits an existing project's deadline.
	 * <p>
	 * The specific project is found based on the project number which is entered by the user. The new
	 * deadline is requested and the record is updated in the database.
	 * 
	 * @see UserInterfaceMethods #displayProjectSummary()
	 * @see ProjectMethods #checkIfProjectNumberIsUnique(int)
	 * @see ProjectMethods #chekcIfProjNumberInt(Scanner, String)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	private static void changeProjectDeadline(Scanner userInput) {
		/*Please refer to the editProjectFeeCharged for comments as the methods are the same.*/
		UserInterfaceMethods.displayProjectSummary();
		
		System.out.println("Please enter the project number you want to edit.");
		System.out.print("Project Number: ");
		String userProjectNumberString = userInput.nextLine();
		
		int userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
		
		int doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);

		while (doesProjectExist == 0) {
			System.out.println("The project number entered does not exist. Please enter an existing project number.");
			UserInterfaceMethods.displayProjectSummary();
			System.out.print("Project Number: ");
			userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
			doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);
		}

	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        int rowsAffected;

	        String inputSQL = "SELECT project_deadline FROM projects WHERE project_number =" + userProjectNumberInt;
	        results = statement.executeQuery(inputSQL);
	        
	        System.out.println("");
	        System.out.print("The current project deadline is: ");
	        if (results.next()){
	        	System.out.print(results.getString("project_deadline"));
	        }

	        System.out.println("");
	        System.out.print("Please enter the updated deadline: ");
	        String updatedProjectDeadline = userInput.nextLine();
	        System.out.println("");
	           
            
	        inputSQL = "UPDATE projects SET project_deadline ='" + updatedProjectDeadline + "' WHERE project_number = " + userProjectNumberInt;
            rowsAffected = statement.executeUpdate(inputSQL);
            
	        System.out.println("***" + rowsAffected + " project updated.***");
	        
                       
            inputSQL = "SELECT * FROM projects WHERE project_number = " + userProjectNumberInt;
            results = statement.executeQuery(inputSQL);
            
            System.out.println("");
            System.out.println("*****************************************************************************");
            System.out.println("");
            System.out.println("Updated project:");
            System.out.println("");
	        if (results.next()){
	           	System.out.println("Project Number: " + results.getString("project_number"));
	        	System.out.println("Project Name: " + results.getString("project_name"));
	        	System.out.println("Building type: " + results.getString("building_type"));
	        	System.out.println("Physical address: " + results.getString("physical_address"));
	        	System.out.println("Project Total Fee: " + results.getString("project_total_fee"));
	        	System.out.println("Project Total Paid: " + results.getString("project_total_paid"));
	        	System.out.println("Project Deadline: " + results.getString("project_deadline"));
	        	System.out.println("Project Status: " + results.getString("project_status"));
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
	 * This method enables a user to finalize a project.
	 * <p>
	 * The method changes the project status to complete and generates an invoice if the total fee
	 * has not been paid.
	 * 
	 * @see UserInterfaceMethods #displayProjectSummary()
	 * @see ProjectMethods #checkIfProjectNumberIsUnique(int)
	 * @see ProjectMethods #chekcIfProjNumberInt(Scanner, String)
	 * @see ProjectMethods #determineProjectCustomer(int)
	 * @see UserInterfaceMethods #generateInvoice(Person, Double)
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	public static void finalizeProject(Scanner userInput) {
		/*Print a summary of existing projects.*/
		UserInterfaceMethods.displayProjectSummary();
		
		/*Request the user to provide a project number that needs to be finalized.*/
		System.out.println("Please enter the project number you want to finalize.");
		System.out.print("Project Number: ");
		String userProjectNumberString = userInput.nextLine();
		
		/*Refer to editProjectFeeCharged for comments on the code below*/
		int userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
		
		int doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);
		
		
		while (doesProjectExist == 0) {
			System.out.println("The project number entered does not exist. Please enter an existing project number.");
			UserInterfaceMethods.displayProjectSummary();
			System.out.print("Project Number: ");
			userProjectNumberInt = chekcIfProjNumberInt(userInput, userProjectNumberString);
			doesProjectExist = checkIfProjectNumberIsUnique(userProjectNumberInt);
		}
		
		/*Once the project number has been obtained, change the project status to Finalized.*/
		changeProjectStatus(userInput, userProjectNumberInt);
		
		/*Calculate the outstanding amount payable on the project by calling the isProjectPaidInFull method.*/
		Double amountPayable = isProjectPaidInFull(userProjectNumberInt);
		
		/*If the outstanding amount is not 0, then get the customer details for the project and generate and print an invoice.*/
		if (amountPayable != 0){
			Person customerToInvoice = determineProjectCustomer(userProjectNumberInt);
			UserInterfaceMethods.generateInvoice (customerToInvoice,  amountPayable);
		} else {
			System.out.println("");
			System.out.println("*****************************************************************************");
			System.out.println("                  Customer has paid in full");
			System.out.println("*****************************************************************************");
			System.out.println("");
		}
	}
	
	/**
	 * This method returns the outstanding amount payable for a project based on the project number.
	 * 
	 * @param projectNumber integer value that represents the project number..
	 * 
	 * @return returns a double value which is the amount payable for the project.
	 */
	private static Double isProjectPaidInFull(int projectNumber){  
		/*Define a double variable to store the amount payable and initialize it.*/
		Double amountPayable = 0.00;
			
	    try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Define a string used as input to SQL query. Select the project fee and project paid to date for the specific project number.*/
	        String inputSQL = "SELECT project_total_paid, project_total_fee FROM projects WHERE project_number =" + projectNumber ;
	        /*Define double variables for the two fields obtained from the database.*/
	        Double projectFee = 0.00;
	        Double projectPaid = 0.00;
	        results = statement.executeQuery(inputSQL);
	        
	        /*Set the results equal to the two Double variables.*/
		    if (results.next()){
		        projectFee = results.getDouble("project_total_fee");
		        projectPaid = results.getDouble("project_total_paid");
		        /*Calculate the amount payable by subtracting tge amount paid from the total fee.*/
		        amountPayable = projectFee - projectPaid;
		    }

	        results.close();
	        statement.close();
	        connection.close();
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		/*Return the amount that must still be paid.*/
		return amountPayable;
	}
	
	/**
	 * This method obtains the customer information for a customer assigned to a specific project number.
	 * 
	 * @param projectNumber integer value that represents the project number.
	 * 
	 * @return returns a Person object - customer of the project.
	 */
	private static Person determineProjectCustomer(int projectNumber){
		/*Define strings for the project fields and initialize them.*/
		String customerName = "";
		String customerNumber = "";
		String customerEmail = "";
		String customerAddress = "";
		Person projectCustomer = new Person();
		
		try {
	        Connection connection = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                "otheruser",
	                "swordfish"
	                );
	          
	        Statement statement = connection.createStatement();
	        ResultSet results;
	        
	        /*Define string input for the SQL query. Select the customer details for a specific project.
	         * A temporary table alias is used to store information from both the projects and customers table.*/
	        String inputSQL = "select customer_name, customer_number, customer_email, customer_address" + 
            		" from" + 
            		" (select projects.project_number, customers.customer_name, customers.customer_number,"
            			+ " customers.customer_email, customers.customer_address"+
            		" from projects" + 
            		" inner join customers on projects.project_customer_ID = customers.customer_ID)"+
            		" AS invoive_customer" + 
            		" where project_number =" + projectNumber;    
            results = statement.executeQuery(inputSQL);
              
            System.out.println(""); 
            /*Set the values obtained from the database equal to the string variables defined above.*/
            if (results.next()){
	        	customerName = results.getString("customer_name");
	        	customerNumber = results.getString("customer_number");
	        	customerEmail = results.getString("customer_email");
	        	customerAddress = results.getString("customer_address");
	        }    
	        
            /*Create a new Person object with the information obtained from the database.*/
	        projectCustomer = new Person(customerName, customerNumber, customerEmail, customerAddress);
	            
	        results.close();
	        statement.close();
	        connection.close();
 
	        }catch (SQLException e) {
	            // We only want to catch a SQLException - anything else is off-limits for now.
	            e.printStackTrace();
	        }
			/*Return the Person object created.*/
	        return projectCustomer;
	}
	
	/**
	 * This method updates the project status to finalized.
	 * <p>
	 * The specific project is found based on the project number which is entered by the user. The status is set to Finalized
	 * and the record is updated in the database.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 * @param projectNumber integer value that represents the project number.
	 */
	private static void changeProjectStatus(Scanner userInput, int projectNumber){
		/*Refer to the editProjectFeeCharged method for comments as methods are the same.*/
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;
	            int rowsAffected;
	            
	            System.out.print("Please enter the completion date (yyyy-mm-dd): ");
	            String completionDate = userInput.nextLine();
		        
		        String inputSQL = "UPDATE projects SET project_status = 'Finalized', project_completion_date='" + completionDate + "' WHERE project_number =" + projectNumber;
	            rowsAffected = statement.executeUpdate(inputSQL);
	            System.out.println("");
	            
	            System.out.println("***" + rowsAffected + " project details updated.***");
	            
	            inputSQL = "SELECT * FROM projects WHERE project_number = " + projectNumber;
	            results = statement.executeQuery(inputSQL);
	            
	            System.out.println("");
	            System.out.println("*****************************************************************************");
	            System.out.println("");
	            System.out.println("Updated project:");
	            System.out.println("");
		        if (results.next()){
		           	System.out.println("Project Number: " + results.getString("project_number"));
		        	System.out.println("Project Name: " + results.getString("project_name"));
		        	System.out.println("Building type: " + results.getString("building_type"));
		        	System.out.println("Physical address: " + results.getString("physical_address"));
		        	System.out.println("Project Total Fee: " + results.getString("project_total_fee"));
		        	System.out.println("Project Total Paid: " + results.getString("project_total_paid"));
		        	System.out.println("Project Deadline: " + results.getString("project_deadline"));
		        	System.out.println("Project Status: " + results.getString("project_status"));
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
	 * This methods loops through all the projects in the database and displays all incompleted projects.
	 */
	public static void displayIncompleteProjects(){
	       try {      
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;

	            /*Input string for SQL query. Select the project number and name from the projects table that have an incomplete status.*/
	            String inputSQL = "SELECT project_number, project_name FROM projects WHERE project_status = 'Incomplete'"; 
	            results = statement.executeQuery(inputSQL);
	            
	            /*Loop through the results and display each incomplete project on a separate line.*/
	            System.out.println("");
	            System.out.println("                    INCOMPLETE PROJECTS");
	            System.out.println("");
	            System.out.println("Project Number		Project Name");
		        while (results.next()){
		        	System.out.println(results.getString("project_number") + "			" + results.getString("project_name"));
		        }
	                       
	            System.out.println("****************************************************************");
	            
	            results.close();
	            statement.close();
	            connection.close();
	            
	        } catch (SQLException e) {
	             e.printStackTrace();
	        }
	}
	
	/**
	 * This method loops through all projects and displays all incomplete projects that are past their due dates.
	 */
	public static void displayOutOfDateProjects(){
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;

	            /*Select the project number, name and deadline for all projects that have a status equal to incomplete.*/
	            String inputSQL = "SELECT project_number, project_name, project_deadline FROM projects WHERE project_status = 'Incomplete'"; 	
	            results = statement.executeQuery(inputSQL);
	            /*Define a Date object equal to today's date.*/
	            Date currentDate = new Date();
	            
	            /*Create a SimpleDateFormat object and specify the format the same as SQL date format.*/
	            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	            
	            /*Loop through all incomplete projects, check which ones are overdue and display them.*/
	            System.out.println("");
	            System.out.println("                    OVERDUE PROJECTS");
	            System.out.println("");
	            System.out.println("Project Number		Project Name		Project Deadline");
		        while (results.next()){
		        	/*Temporarily store each project deadline in the string variable.*/
		        	String currentProjectDeadline = results.getString("project_deadline");
		        	/*Try and convert the string to the date object.*/
		        	try{
		        		Date projectDeadlineDate = formatter1.parse(currentProjectDeadline);
		        		/*If conversion is successful compare the dates.*/
		        		int dateComparison = projectDeadlineDate.compareTo(currentDate);
		        		/*If the deadline is earlier than the current date the project is overdue and is displayed.*/
		        		if (dateComparison < 0){
		        			System.out.println(results.getInt("project_number") + "			" + results.getString("project_name") + "		" + results.getString("project_deadline"));
		        		}
		        	} 
		        	/*Display an error message if the string could not be converted to a Date.*/
		        	catch (Exception e) {
		        		System.out.println("There was an error!");
		        	}
		        }
	                       
	            System.out.println("****************************************************************");
	            
	            results.close();
	            statement.close();
	            connection.close();
 
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * This method searches for a project in the database based on a name or number and displays it to the user.
	 * 
	 * @param userInput Scanner object to enable the user to provide input.
	 */
	public static void findProjectByNumberOrName(Scanner userInput){
		/*Request the user to input a string - project number or name*/
		System.out.print("Please enter the project name or number: ");
		String userSearchValue = userInput.nextLine();
		
	       try {
	            Connection connection = DriverManager.getConnection(
	                    "jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	                    "otheruser",
	                    "swordfish"
	                    );
	          
	            Statement statement = connection.createStatement();
	            ResultSet results;

	            /*Select all project fields for either the project name or number provided.*/
	            String inputSQL = "SELECT * FROM projects WHERE project_number = '" + userSearchValue + "' OR project_name = '" 
	            + userSearchValue +"'" ;
	            results = statement.executeQuery(inputSQL);
	            
	            /*Displays the project if found.*/
	            System.out.println("");
		        if (results.next()){
		        	System.out.println("Project Number: " + results.getString("project_number"));
		        	System.out.println("Project Name: " + results.getString("project_name"));
		        	System.out.println("Project Deadline: " + results.getString("project_deadline"));
		        	System.out.println("Project Total Fee: " + results.getString("project_total_fee"));
		        	System.out.println("Project Total Paid: " + results.getString("project_total_paid"));
		        	System.out.println("Project Status: " + results.getString("project_status"));
		        } 
		        /*Displays a message if the project was not found.*/
		        else {
		        	System.out.println("Project does not exist.");
		        }
		        
		        System.out.println("******************************************************************");

	            results.close();
	            statement.close();
	            connection.close();

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
}
