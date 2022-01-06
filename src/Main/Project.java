package Main;

import java.sql.Date;
import java.time.LocalDate; // class for dealing with dates. Source: https://www.w3schools.com/java/java_date.asp

/**
 * This class handles project objects
 * 
 * @version 1.00, 02 Dec 2021
 * @author Robin Titus
 *
 */
public class Project {
	
	//attributes
	private int number;
	private String name;
	private String buildingType;
	private String address;
	private int erfNumber;
	private double totalFee;
	private double totalPaid;
	private LocalDate deadline;
	private Person architect;
	private Person contractor;
	private Person customer;
	private LocalDate completionDate;
	private boolean finalisedProject = false;
	


	/**
	 *The project constructor that instantiates project objetcs
	 * 
	 * @param number A unique int per project object 
	 * @param name What the project object is called
	 * @param buildingType Classification of the object
	 * @param address Location of the object
	 * @param erfNumber Number relative to map
	 * @param totalFee Amount due by customer
	 * @param totalPaid Amount paid by customer
	 * @param deadline Date project should be completed by
	 * @param architect Person object
	 * @param contractor Person object
	 * @param customer Person object
	 */
	public Project(int number, String name, String buildingType, String address, int erfNumber, double totalFee, double totalPaid, LocalDate deadline, Person architect, Person contractor, Person customer) {
		this.number = number;
		this.name = name;
		this.buildingType = buildingType;
		this.address = address;
		this.erfNumber = erfNumber;
		this.totalFee = totalFee;
		this.totalPaid = totalPaid;
		this.deadline = deadline;
		this.architect = architect;
		this.contractor = contractor;
		this.customer = customer;
	}
	
	
	/**
	 * getter method - get number
	 * 
	 * @return number unique identifier
	 */
	public int getNumber() {
		return number;
	}
	
	
	/**
	 * getter method - returns name
	 * 
	 * @return name project name
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * getter method - returns deadline
	 * 
	 * @return deadline project deadline
	 */
	public LocalDate getDeadline() {
		return deadline;
	}
	
	
	/**
	 *getter method - get total fee 
	 * @return totalFee amount customer needs to pay 
	 */
	public double getTotalFee() {
		return totalFee;
	}
	
	
	/**
	 * getter method - get total paid
	 * @return totalPaid amount customer has paid
	 */
	public double getTotalPaid() {
		return totalPaid;
	}
	
	
	/**
	 * getter method - get contractor
	 * @return contractor person object
	 */
	public Person getContractor() {
		return contractor;
	}
	
	
	/**
	 * Return finalised status if called
	 * @return finalisedProject boolean variable confirmation
	 */
	public boolean getFinalisedStatus() {
		return finalisedProject;
	}
	
	
	/**
	 * Change completion date
	 * @param newCompletionDate date of completion
	 */
	public void setCompletionDate(LocalDate newCompletionDate) {
		completionDate = newCompletionDate;
	}
	
	
	/**
	 * Change finalised status
	 * @param newStatus
	 */
	public void setFinalisedStatus(boolean newStatus) {
		finalisedProject = newStatus;
	}
	
	
	/**
	 * Allow project deadline to be updated
	 * @param newDeadline
	 */
	public void setDeadline(LocalDate newDeadline) {
		deadline = newDeadline;
	}
	
	
	/**
	 * Allow total paid amount to be updated
	 * @param newTotalPaid updated amount paid
	 */
	public void setTotalPaid(double newTotalPaid) {
		totalPaid = newTotalPaid;
	}
	
	
	/**
	 * Setter method used to update 
	 * the details of a contractor per project
	 * 
	 * @param id unique number per contractor object
	 * @param newName updated name of contractor object
	 * @param newTelephone updated telphone number
	 * @param newEmail updated email
	 * @param newAddress updated address
	 */
	public void setContractor(int id, String newName, String newTelephone, String newEmail, String newAddress) {
		
		//call set method in person class since a contractor is a person
		contractor.setId(id);
		contractor.setName(newName);
		contractor.setTelephone(newTelephone);
		contractor.setEmail(newEmail);
		contractor.setAddress(newAddress);
		
	}
	
	
	/**
	 * method that allows projects to be finalised
	 * @return output invoice string
	 */
	public String finaliseProject() {
		
		//declare variables
		String output ="";
		
		//calculate amount due
		double amountDue = totalFee - totalPaid;
		
		//round amount due to 2 decimal places
		amountDue = Math.round(amountDue * 100.0) / 100.0;
		
		//print invoice if amount due > 0
		if (amountDue>0) {
			
			//set completion date and finalise
			completionDate = LocalDate.now();
			finalisedProject = true;
			
			//add projet to output variable
			//output += "Customer - Contact Details: \n"+customer;
			output += "___INVOICE___\n"+customer;
			output += "\nAmount Due:\tR"+amountDue+"\n";
			output += "-------------------------------";
			
		}
		
		//return output to user
		return output;
	}
	
	//toString method
	/**
	 * toString method
	 * Returns object in string format
	 * 
	 * @return output string format of object
	 */
	public String toString() {
		String output = "Project Number:\t "+number+"\n";
		output += "Name:\t "+name+"\n";
		output += "Building Type:\t "+buildingType+"\n";
		output += "Address:\t "+address+"\n";
		output += "Erf Number:\t "+erfNumber+"\n";
		output += "Total Fee:\t "+totalFee+"\n";
		output += "Total Paid:\t "+totalPaid+"\n";
		output += "Deadline:\t "+deadline+"\n";
		output += "\nArchitect: \n"+architect+"\n";
		output += "Contractor: \n"+contractor+"\n";
		output += "Customer: \n"+customer+"\n";
		
		return output;
	}
	
	
	/**
	 * Method that creates a comma seperated value string
	 * of project object for project text file
	 * 
	 * @return output string value returned
	 */
	public String getProjectSqlQuery() {
		
		String output = "";
		
		// Differentiation is necessary
		// This method could be called by projects that dont yet have a completion date
		if (completionDate != null) {
			output = "INSERT INTO projects values("+number+", '"+name+"', '"+buildingType+"', '"+address+"', "+erfNumber+", "+totalFee+", "+totalPaid+", '"+Date.valueOf(deadline)+"', "+architect.getId()+", "+contractor.getId()+", "+customer.getId()+", '"+Date.valueOf(completionDate)+"', "+finalisedProject+")";
		}
		else {
			output = "INSERT INTO projects values("+number+", '"+name+"', '"+buildingType+"', '"+address+"', "+erfNumber+", "+totalFee+", "+totalPaid+", '"+Date.valueOf(deadline)+"', "+architect.getId()+", "+contractor.getId()+", "+customer.getId()+", null, "+finalisedProject+")";
		}
		
		return output;
	}
	
	
}