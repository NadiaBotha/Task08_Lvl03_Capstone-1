/**
 * This method is used as a user defined object called Project.
 * <p>
 * Fields for this object includes, project number, project name, building type, physical address, 
 * ERF Number, total fee, total paid, project deadline, project status, project completion date, project
 * architect, project contractor and project customer.
 * @author Nadia Botha
 * @version 1.0
 *
 */
public class Project {
	/**
	 * String value for the Project object project number.
	 */
    private String projectNumber;
	/**
	 * String value for the Project object project name.
	 */
    private String projectName;
	/**
	 * String value for the Project object project building type.
	 */
    private String buildingType;
	/**
	 * String value for the Project object physical address.
	 */
    private String physicalAddress;
	/**
	 * String value for the Project object ERF Number.
	 */
    private String ERFNumber;
	/**
	 * String value for the Project object total fee.
	 */
    private String totalFee;
	/**
	 * String value for the Project object total paid.
	 */
    private String totalPaid;
	/**
	 * String value for the Project object project deadline.
	 */
    private String projectDeadline;
	/**
	 * String value for the Project object project status.
	 */
    private String projectStatus;
	/**
	 * String value for the Project object project completion date.
	 */
    private String projectCompletionDate;
	/**
	 * Person value for the Project object architect.
	 */
    private Person projectArchitect;
	/**
	 * Person value for the Project object contractor.
	 */
    private Person projectContractor;
	/**
	 * Person value for the Project object customer.
	 */
    private Person projectCustomer;
    
    /**
     * This is the first constructor which contains all of the fields.
     * @param projectNumber String for the project number.
     * @param projectName String for the project name.
     * @param buildingType String for the building type.
     * @param physicalAddress String for the project physical address.
     * @param ERFNumber String for the project ERF number.
     * @param totalFee String for the total fee.
     * @param totalPaid String for the total paid.
     * @param projectDeadline String for the project deadline.
     * @param projectArchitect Person for the project architect.
     * @param projectContractor Person for the project contractor.
     * @param projectCustomer Person for the project customer.
     * @param projectStatus String for the project status.
     * @param projectCompletionDate String for the project completion date.
     */
    public Project(String projectNumber, String projectName, String buildingType,
                   String physicalAddress, String ERFNumber, String totalFee, String totalPaid,
                   String projectDeadline, Person projectArchitect, Person projectContractor,
                   Person projectCustomer, String projectStatus, String projectCompletionDate) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.buildingType = buildingType;
        this.physicalAddress = physicalAddress;
        this.ERFNumber = ERFNumber;
        this.totalFee = totalFee;
        this.totalPaid = totalPaid;
        this.projectDeadline = projectDeadline;
        this.projectArchitect = projectArchitect;
        this.projectContractor = projectContractor;
        this.projectCustomer = projectCustomer;
        this.projectStatus = projectStatus;
        this.projectCompletionDate = projectCompletionDate;
    }

    /**
     * This is the second constructor which excludes the projectStatus and projectCompletion date fields.
     * @param projectNumber String for the project number.
     * @param projectName String for the project name.
     * @param buildingType String for the building type.
     * @param physicalAddress String for the project physical address.
     * @param ERFNumber String for the project ERF number.
     * @param totalFee String for the total fee.
     * @param totalPaid String for the total paid.
     * @param projectDeadline String for the project deadline.
     * @param projectArchitect Person for the project architect.
     * @param projectContractor Person for the project contractor.
     * @param projectCustomer Person for the project customer.
     */
    public Project(String projectNumber, String projectName, String buildingType,
                   String physicalAddress, String ERFNumber, String totalFee, String totalPaid,
                   String projectDeadline, Person projectArchitect, Person projectContractor,
                   Person projectCustomer) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.buildingType = buildingType;
        this.physicalAddress = physicalAddress;
        this.ERFNumber = ERFNumber;
        this.totalFee = totalFee;
        this.totalPaid = totalPaid;
        this.projectDeadline = projectDeadline;
        this.projectArchitect = projectArchitect;
        this.projectContractor = projectContractor;
        this.projectCustomer = projectCustomer;
    }
    
    /**
     * This is the third constructor which excludes buildingType, physicalAddress, ERFNumber, totalFee, totalPaid,
     * projectDeadline, projectArchitect, projectContractor, projectCustomer.
     * 
     * @param projectNumber projectNumber String for the project number.
     * @param projectName projectName String for the project name.
     * @param projectStatus String for the project status.
     * @param projectCompletionDate String for the project completion date.
     */
    public Project(String projectNumber, String projectName,String projectStatus, String projectCompletionDate) {
        this.projectNumber = projectNumber;
        this.projectName = projectName;
        this.projectStatus = projectStatus;
        this.projectCompletionDate = projectCompletionDate;
    }
    
    /**
     * This is the fourth constructor which does not contain any fields.
     */
    public Project(){
    }
    
    /**
     * This method displays Project object fields in a readable manner.
     * @return returns a String which displays project details in a readable manner.
     */
    public String toString(){
        String output = "Project Number: " + this.projectNumber;
        output += "\nProject Name: " + this.projectName;
        output += "\nBuilding type to design: " + this.buildingType;
        output += "\nPhysical address of project: " + this.physicalAddress;
        output += "\nERF Number: " + this.ERFNumber;
        output += "\nTotal Fee: " + this.totalFee;
        output += "\nTotal paid to date: " + this.totalPaid;
        output += "\nProject Deadline: " + this.projectDeadline;
        output += "\n";
        output += "\nArchitect Details";
        output += "\n" + this.projectArchitect;
        output += "\n";
        output += "\nContractor Details ";
        output += "\n" + this.projectContractor;
        output += "\n";
        output += "\nCustomer Details ";
        output += "\n" + this.projectCustomer;

        return output;
    }
    
    /**
     * This method displays certain Project object fields in a readable manner to serve as a summary. 
     * <p>
     * Fields displayed include projectNumber and projectName.
     * @return returns a String which displays certain project details in a readable manner.
     */
    public String printProjectSummary(){
    	String output = "Project Number: " + this.projectNumber;
    	output += "\nProject Name: " + this.projectName;
    	
    	return output;
    }

    /**
     * This method obtains a specific Project object's project number.
     * 
     * @return return a string for the project number.
     */
    public String getProjectNumber(){
        return this.projectNumber;
    }
    
    /**
     * This method obtains a specific Project object's project name.
     * 
     * @return returns string for the project name.
     */
    public String getProjectName(){
        return this.projectName;
    }
    
    /**
     * This method obtains the building type for a specific Project object.
     * 
     * @return returns a string for the project building type.
     */
    public String getBuildingType(){
        return this.buildingType;
    }
    
    /**
     * This method obtains the physical address for a specific Project object.
     * @return returns a string for the project's physical address.
     */
    public String getAddress(){
        return this.physicalAddress;
    }
    
    /**
     * This method obtains the ERF Number for a specific Project object.
     * @return returns a string for the project's ERF Number.
     */
    public String getERFNumber(){
        return this.ERFNumber;
    }
    
    /**
     * This method obtains the project architect for a specific Project object.
     * 
     * @return returns a Person object project architect.
     */
    public Person getProjectArchtect(){
        return this.projectArchitect;
    }
    
    /**
     * This method obtains the project contractor for a specific Project object.
     * 
     * @return returns a Person object project contractor.
     */
    public Person getProjectContractor(){
        return this.projectContractor;
    }
    
    /**
     * This method obtains the project customer for a specific Project object.
     * 
     * @return returns a Person object project customer.
     */
    public Person getProjectCustomer(){
        return this.projectCustomer;
    }
    
    /**
     * This method obtains the deadline for a specific Project object.
     * @return returns a string for the project's deadline.
     */
    public String getProjectDeadline(){
        return this.projectDeadline;
    }
    
    /**
     * This method obtains the total fee for a specific Project object.
     * @return returns a string for the project's total fee.
     */
    public String getProjectFee(){
        return this.totalFee;
    }
    
    /**
     * This method obtains the total paid for a specific Project object.
     * @return returns a string for the project's total paid.
     */
    public String getProjectTotalPaid(){
        return this.totalPaid;
    }

    /**
     * This method obtains the completion date for a specific Project object.
     * @return returns a string for the project's completion date.
     */
    public String getProjectCompletionDate(){
        return this.projectCompletionDate;
    }

    /**
     * This method obtains the status for a specific Project object.
     * @return returns a string for the project's status.
     */
    public String getProjectStatus(){
        return this.projectStatus;
    }
    
    /**
     * This method sets the field projectNumber to the String value passed as a parameter.
     * 
     * @param projectNumber String for the project number.
     */
    public void setProjectNumber(String projectNumber){
        this.projectNumber = projectNumber;
    }
    
   /**
    * This method sets the field projectName to the String value passed as a parameter.
    * 
    * @param projectName String for the project name.
    * 
    */
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }
    
    /**
     * This method sets the field buildingType to the String value passed as a parameter.
     * 
     * @param buildingType String for the building type.
     */
    public void setBuildingType(String buildingType){
        this.buildingType = buildingType;
    }
    
    /**
     * This method sets the field physicalAddress to the String value passed as a parameter.
     * 
     * @param physicalAddress String for the project's physical address.
     */
    public void setPhysicalAddress(String physicalAddress){
        this.physicalAddress = physicalAddress;
    }
    
    /**
     * This method sets the field ERFNumber to the String value passed as a parameter.
     * 
     * @param ERFNumber String for the project's ERF Number.
     */
    public void setERFNumber(String ERFNumber){
        this.ERFNumber = ERFNumber;
    }
    
    /**
     * This method sets the field totalFee to the String value passed as a parameter.
     * 
     * @param totalFee String for the project's total fee.
     */
    public void setTotalFee(String totalFee){
        this.totalFee = totalFee;
    }
    
    /**
     * This method sets the field projectDeadline to the String value passed as a parameter.
     * 
     * @param DueDate String for the project's deadline.
     */
    public void setDueDate(String DueDate){
        this.projectDeadline = DueDate;
    }
    
    /**
     * This method sets the field totalPaid to the String value passed as a parameter.
     * 
     * @param totalPaid String for total paid to date.
     */
    public void setTotalPaid(String totalPaid){
        this.totalPaid = totalPaid;
    }
    
    /**
     * This method sets the field projectStatus to the String value passed as a parameter.
     * 
     * @param projectStatus String for the project status.
     */
    public void setProjectStatus(String projectStatus){
        this.projectStatus = projectStatus;
    }
    
    /**
     * This method sets the field projectCompletionDate to the String value passed as a parameter.
     * 
     * @param projectCompletionDate String for the project completion date.
     */
    public void setprojectCompletionDate(String projectCompletionDate){
        this.projectCompletionDate= projectCompletionDate;
    }
    
    /**
     * This method sets the field projectArchitect to the Person value passed as a parameter.
     * 
     * @param projectArchitect Person object for the project Architect.
     */
    public void setProjectArchitect(Person projectArchitect){
        this.projectArchitect = projectArchitect;
    }
    
    /**
     * This method sets the field projectContractor to the Person value passed as a parameter.
     * 
     * @param projectContractor Person object for the project contractor.
     */
    public void setProjectContractor(Person projectContractor){
        this.projectContractor = projectContractor;
    }
    
    /**
     * This method sets the field projectCustomer to the Person value passed as a parameter.
     * 
     * @param projectCustomer Person object for the project customer.
     */
    public void setProjectCustomer(Person projectCustomer){
        this.projectCustomer = projectCustomer;
    }
}
