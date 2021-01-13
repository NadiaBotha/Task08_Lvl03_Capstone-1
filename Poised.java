import java.util.Scanner;

/**
 * This code runs the project management system for Poised.
 * <p>
 * It displays all the options a user can select to create, find or edit a project.
 * Requests the user to select an option and calls the relevant method to carry out
 * the option chosen. All data is written to the PoisPMS database and relevant tables.
 * 
 * @see ProjectMethods #createNewProject(Scanner)
 * @see ProjectMethods #createAndWriteNewProjectToDatabase(Project)
 * @see ProjectMethods #editProjectDetails(Scanner)
 * @see ProjectMethods #finalizeProject(Scanner)
 * @see ProjectMethods #displayIncompleteProjects()
 * @see ProjectMethods #displayOutOfDateProjects()
 * @see ProjectMethods #findProjectByNumberOrName(Scanner)
 * 
 * @author Nadia Botha
 * @version 1.0
 */

public class Poised {
	
	public static void main(String[] args) {
	    // Calls the displayOption method, which prints out the list of options the user can select.
	    UserInterfaceMethods.displayOptions();
	
	    Scanner userInput = new Scanner(System.in);
	
	    // The user's choice is stored as an integer (getUserChoice checks if the user input is valid).
	    int userChoice = UserInterfaceMethods.getUserChoice(userInput);
	    
	    /*The while loop will keep executing as long as the use does not select option 7 on the main menu.
	     * Option 7  = exit the program.*/
	    while (userChoice != 7) {
	    	/*If the user selects option 1, a new Project object will be created by calling the createNewProjectMethod.
	    	 * The Project object will be used as input to the createAndWriteNewProjectToDatabase method, which writes the
	    	 * new Project to the poisepms database - project table.*/
	    	if (userChoice == 1) {
	    		Project newProject = ProjectMethods.createNewProject(userInput);
	    		ProjectMethods.createAndWriteNewProjectToDatabase(newProject);	
	    	} 
	    	/*If the user selects option 2 - the editProjectDetails method is called to edit a project/person.*/
	    	else if (userChoice == 2) {
	    		ProjectMethods.editProjectDetails(userInput);
	    	} 
	    	/*If the user selects option 3 - the finalizeProject method is called, to mark a project as finalised and
	    	 * to display an invoice if a payment is outstanding.*/
	    	else if (userChoice == 3) {
	    		ProjectMethods.finalizeProject(userInput);
	    	} 
	    	/*If the user selects option 4 - the  displayIncompleteProjects method is called. It will display
	    	 * all incompleted projects to the user.*/
	    	else if (userChoice == 4) {
	    		ProjectMethods.displayIncompleteProjects();
	    	} 
	    	/*If the user selects option 5 - the displayOutOfDateProjects method is called. It will display all projects
	    	 * that are incomplete and past their due dates.*/
	    	else if (userChoice == 5) {
	    		ProjectMethods.displayOutOfDateProjects();
	    	} 
	    	/*If the user selects option 6 - the findProjectByNumberOrName method is called which enables the
	    	 * user to look for a project based on the project name or number.*/
	    	else {
	    		ProjectMethods.findProjectByNumberOrName(userInput);
	    	}
	    	/*Before exiting the while loop, the main menu is displayed and the user has to select an option.
	    	 * If the option is not equal to 7, the above code will be executed again. Otherwise the while loop
	    	 * and program is exited and is done running.*/
	    	UserInterfaceMethods.displayOptions();
	    	userChoice = UserInterfaceMethods.getUserChoice(userInput);
	    }
	    
	    /*Display a message that the program is done executing and closes the Scanner object used for getting user input.*/
	    System.out.println("Exiting the program...");
	    userInput.close();
	   
	}
}
