/**
 * This method is used as a user defined object called Person.
 * <p>
 * Fields for this object include, name, number, email address and person type (architect, contractor, customer).
 * 
 * @author Nadia Botha
 * @version 1.0
 *
 */

public class Person {
    /**
     * String value for the Person object name and surname.
     */
    private String personName;
    /**
     * String value for the Person object telephone number.
     */
    private String telephoneNumber;
    /**
     * String value for the Person object email address.
     */
    private String emailAddress;
    /**
     * String value for the Person object physical address.
     */
    private String physicalAddress;

    /**
     * Main constructor which includes all the fields to define a Person object.
     * 
     * @param personName String for person name and surname.
     * @param telephoneNumber String containing digits for person telephone number.
     * @param emailAddress String for person email address.
     * @param physicalAddress String for person physical address.
     */
    public Person(String personName, String telephoneNumber, String emailAddress, String physicalAddress) {
        this.personName = personName;
        this.telephoneNumber = telephoneNumber;
        this.emailAddress = emailAddress;
        this.physicalAddress = physicalAddress;
    }
    
    /**
     * This is the secondary constructor which has no parameters.
     *  
     */
    public Person() {
    }
    
    /**
     * This method displays Person object fields in a readable manner.
     * @return returns a String which displays person details in a readable manner.
     */
    public String toString() {
        String output = "Name: " + this.personName;
        output += "\nTelephone Number: " + this.telephoneNumber;
        output += "\nEmail Address: " + this.emailAddress;
        output += "\nPhysical Address: " + this.physicalAddress;

        return output;
    }

    /**
     * This method obtains a specific Person object's name.
     * 
     * @return returns a String for the person name and surname.
     */
    public String getPersonName() {
        return this.personName;
    }
    
    /**
     * This method obtains a specific Person object's number.
     * 
     * @return returns a String for the person's telephone number.
     */
    public String getTelephoneNumber() {
        return this.telephoneNumber;
    }
    
    /**
     * This method obtains a specific Person object's email address.
     * 
     * @return returns a String for the person's email address.
     */
    public String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * This method obtains a specific Person object's physical address.
     * 
     * @return returns a String for the person's physical address. 
     */
    public String getPhysicalAddress() {
        return this.physicalAddress;
    }
    
    /**
     * This method sets the field personName to the String value passed as a parameter.
     * 
     * @param personName String for person name and surname.
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    /**
     * This method sets the field telephoneNumber to the String value passed as a parameter.
     * 
     * @param telephoneNumber String containing digits for person telephone number.
     */
    public void setPersonNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
    
    /**
     * This method sets the field emailAddress to the String value passed as a parameter.
     * 
     * @param emailAddress String for person email address.
     */
    public void setPersonEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    
    /**
     * This method sets the field physicalAddress to the String value passed as a parameter.
     * 
     * @param physicalAddress String for person physical address.
     */
    public void setPersonPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }
}
