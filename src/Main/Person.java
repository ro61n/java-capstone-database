package Main;

/**
 * This class handles person objects
 * 
 * @version 1.00, 02 Dec 2021
 * @author Robin Titus
 *
 */
public class Person {
	
	//attributes
	private int id;
	private String name;
	private String telephone;
	private String email;
	private String address;
	private String personType;
	
	
	/**
	 * Person constructor, used to instantiate
	 * person objects
	 * 
	 * @param id unique identifier of person object
	 * @param name person's name
	 * @param telephone person phone number
	 * @param email person's email
	 * @param address location of the person
	 * @param personType (contractor, customer, architect)
	 */
	public Person(int id, String name, String telephone, String email, String address, String personType) {
		this.id = id;
		this.name = name;
		this.telephone = telephone;
		this.email = email;
		this.address = address;
		this.personType = personType;
	}
	
	
	/**
	 * Getter method used to get ID of person object
	 * @return id unique identifier of person
	 */
	public int getId() {
		return id;
	}
	
	
	/**
	 * Getter method used to get the 
	 * name of the person object
	 * 
	 * @return name person object's name
	 */
	public String getName() {
		return name;
	}
	
	//	Return email
	/**
	 * Method that returns the 
	 * email address of the person
	 * 
	 * @return email email address of person
	 */
	public String getEmail() {
		return email;
	}
		

	/**
	 * Method that returns the
	 * phone number of the person object
	 * @return telephone phone number of the person
	 */
	public String getTelephone() {
		return telephone;
	}
	
	//	Return address
	/**
	 * Return the address of a
	 * person object
	 * 
	 * @return address location of person
	 */
	public String getAddress() {
		return address;
	}
	
	
	/**
	 * Return the type characteristic
	 * of a person object
	 * 
	 * @return personType (architect, contractor, customer)
	 */
	public String getType() {
		return personType;
	}
	
	
	/**
	 * Change the value of the Id Variable
	 * @param newId new value of id variable
	 */
	public void setId(int newId) {
		id = newId;
	}
	
	
	/**
	 *setter method - allows name to be updated 
	 * @param newName new name of person
	 */
	public void setName(String newName) {
		name = newName;
	}
	
	
	/**
	 * setter method - allows telephone to be updated
	 * @param newTelephone updated telephone number
	 */
	public void setTelephone(String newTelephone) {
		telephone = newTelephone;
	}
	
	
	/**
	 * setter method - allows email to be updated
	 * @param newEmail updated email address
	 */
	public void setEmail(String newEmail) {
		email = newEmail;
	}
	
	
	/**
	 * setter method - allows address to be updated
	 * @param newAddress updated address of person
	 */
	public void setAddress(String newAddress) {
		address = newAddress;
	}
	
	
	/**
	 * toString Method - return person object
	 * in a string format
	 * @return output string format of person object
	 */
	public String toString() {
		String output = "Name:\t\t"+name+"\n";
		output += "Telephone:\t"+telephone+"\n";
		output += "Email:\t\t"+email+"\n";
		output += "Address:\t"+address+"\n";
		output += "Category:\t"+personType+"\n";
		
		return output;
	}
	
	
	/**
	 * Method that puts all the variables
	 * in a sql query
	 * 
	 * @return output object in string format
	 */
	public String getPersonSqlQuery() {
		
		// Return SQL query with correct variables in
		String output = "INSERT INTO people values("+id+", '"+name+"', '"+email+"', '"+address+"', '"+telephone+"', '"+personType+"')";
		
		return output;
	}

}
