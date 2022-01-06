package Main;

import java.io.FileWriter;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


/**
 * Main class
 * The class runs the whole poise application 
 *
 * @author Robin Titus
 * @version 1.00, 02 Dec 2021
 */
public class Main {
	
	//declare scanner variable
	static Scanner userInput = new Scanner(System.in);
	static int rowsAffected;
	static ResultSet results;
	static Statement statement;
	
	//declare project and person list arrays
	static List<Project> allProjects = new ArrayList<Project>();
	static List<Person> allPeople = new ArrayList<Person>();

	/**
	 * Main method that runs the app loop
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			// Connect to the ebookstore database, via the jdbc:mysql:
			Connection connection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"ro6ln",
					"cocomilo1"
			);
			
			// Create a direct line to the database for running queries
			statement = connection.createStatement();
			
			// Call functions to load projects and people from the poise sql database
			loadExistingPeople(statement);
			loadExistingProjects(statement);
			
			//app loop
			while (true) {	// boolean loop control variable
				
				//print message to user
				System.out.println("Please select an option: ");
				System.out.println("v\t-\tView active projects");
				System.out.println("o\t-\tView overdue projects");
				System.out.println("s\t-\tFind a specific project");
				System.out.println("c\t-\tCreate new project");
				System.out.println("d\t-\tChange due date of project");
				System.out.println("t\t-\tChange total amount of fee paid to date");
				System.out.println("u\t-\tUpdate contractor's details");
				System.out.println("f\t-\tFinalise a project");
				System.out.println("e\t-\tExit application");
				System.out.println("\nWhich option would you like to select:");
				
				//get user input
				char userOption = userInput.next().charAt(0);
				
				//run command (method) based on user input
				if (userOption=='s') {
					findProject();
				}
				else if (userOption=='o') {
					viewOverdueProjects();
				}
				else if (userOption=='v') {
					viewActiveProjects();
				}
				else if (userOption=='c') {
					createProject();
				}
				else if (userOption=='d') {
					changeDueDate();
				}
				else if (userOption=='t') {
					changeTotPaid();
				}
				else if (userOption=='u') {
					updateContractor();
				}
				else if (userOption=='f') {
					finaliseProject();
				}
				else if (userOption=='e') {
					
					//print exit message
					System.out.println("Exit. Application Closed.");
					
					break;	//	Replaced boolean loop handler with break
					
				}
				else {
					//	Else tell user to input a correct value
					System.out.println("\n"+userOption+" is not an option. Please look at the available options below:\n");
				}
				
			}	//	End of app loop
			

			// Close database connections when program loop ends
			results.close();
			statement.close();
			connection.close();
			
		} catch (SQLException e) {
			// Catch a SQLException error if necessary
			e.printStackTrace();
		}
		
	}
	

		
	/**
	 * Method that allows user to search for a project by typing in the name or project number
	 */
	public static void findProject() {
		System.out.println("\nSearch for a project:\n");
		
		userInput.nextLine();  // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
		
		//	Show user difference ways to search
		System.out.println("p\t-\tSearch by project name");
		System.out.println("n\t-\tSearch by project number");
		System.out.println("Selection:");
		
		//	Get user's selected option
		char selection = userInput.next().charAt(0);
		
		//	Based on selected search option, ask user to enter project name or number and retrieve search input
		String searchStr = "";
		if (selection == 'p') {
			System.out.println("Enter project name:");
			userInput.nextLine();
			searchStr = userInput.nextLine();
		}
		else if (selection == 'n') {
			System.out.println("Enter project number:");
			userInput.nextLine();
			searchStr = userInput.nextLine();
		}
		
		//	Declare iterator to loop through project Objects
		Iterator<Project> projIterator = allProjects.iterator();
		
		System.out.println("Resutls:");
		
		//	Loop through iterator & display project if input matches the project name or number
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			
			if ( ((selection=='p') && (p.getName().equals(searchStr))) || ((selection=='n') && (p.getNumber()==Integer.parseInt(searchStr))) ) {
				System.out.println(p.toString());
			}
		}
		
	}
	
	
	/**
	 * Method that displays project objects that are past their due date
	 */
	public static void viewOverdueProjects() {
		
		System.out.println("\nOverdue Projects:\n\n");
		
		LocalDate today = LocalDate.now();	//	Use this to compare with project due date
		
		Iterator<Project> projIterator = allProjects.iterator();
		
		//	Loop through iterator
		//	Find and display any project where the deadline has past the current date
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			
			if ((p.getDeadline().compareTo(today))<0) {
				System.out.println(p.toString());
				System.out.println();
				System.out.println("--------------------");
			}
		}
		
	}
	
	
	/**
	 * Method that displays projects that have not yet been finalised
	 */
	private static void viewActiveProjects() {
		
		System.out.println("\nProjects to be completed:\n\n");
		
		Iterator<Project> projIterator = allProjects.iterator();
		
		//	Loop through iterator
		//	Display project if finalised status is false
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			if (!p.getFinalisedStatus()) {
				System.out.println(p.toString());
				System.out.println();
				System.out.println("--------------------");
			}
		}
		
	}
	
	
	/**
	 * Method that finalises a project
	 */
	private static void finaliseProject() {
		
		//print message to user
		System.out.println("\nFinalise:");
		System.out.println("Projects listed below:");
		System.out.println("Project Number\tName\t\tTotal Fee\tTotal Paid");
		
		Iterator<Project> projIterator = allProjects.iterator();
		
		//	Loop through iterator to display projects
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			if (!p.getFinalisedStatus()) {
				System.out.println(p.getNumber()+"\t\t"+p.getName()+"\t\t"+p.getTotalFee()+"\t\t"+p.getTotalPaid());
			}
			
		}
		
		userInput.nextLine();  // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
		
		System.out.println("\nPlease select a project number or name:");
		
		//get user's selection
		String projectN = userInput.nextLine();
		
		//	Reset iterator variable for the next while loop
		projIterator = allProjects.iterator();
		
		//print new line
		System.out.println();		
		
		String output = "";
		
		//	Look for project that user selected and finalise that project
		while ( (projIterator.hasNext()) && (true)) {
			Project p = projIterator.next();
			if ((p.getName().equals(projectN)) || (projectN.matches("\\d+") && (p.getNumber()==Integer.parseInt(projectN)))) {
				
				output = p.finaliseProject(); // finaliseProject is a method in the project class
				
				System.out.println(output);
				
				break;
			}
		}
		
		//print success message
		System.out.println("\nProject Successfully Finalised\n");
		
		//	Write finalised project / Invoice to Complete project text file
		//	This is the only place where a text file is still used (for the invoices) 
		try {
			FileWriter fwAns = new FileWriter("./Complete project.txt",true);
			fwAns.write(output+"\r\n");
			fwAns.close();	
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		//	Run method to update project records
		try {
			updateProjectRecords(statement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Method for updating contractor
	 */
	private static void updateContractor() {
		
		//print messsage to user
		System.out.println("\nProject / Contractor details below:");
		
		Iterator<Project> projIterator = allProjects.iterator();
		
		//	Loop through iterator to display projects
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			
			System.out.println("Project Number: "+p.getNumber()+"\tName: "+p.getName());
			System.out.println("Contractor Details:");
			System.out.println(p.getContractor().toString());
		}
		
		userInput.nextLine();  // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
		
		System.out.println("\nPlease select a project number or name (to update that project's contractor):");
		
		//	Get user's selection
		String projectN = userInput.nextLine();
		
		//	Following lines get new information about contractor
		
		System.out.println("Enter Updated information below");
		
		System.out.println("Contractor's Name:");
		String newName = userInput.nextLine();
		
		System.out.println("Telephone:");
		String newTelephone = userInput.nextLine();
		
		System.out.println("Email:");
		String newEmail = userInput.nextLine();
		
		System.out.println("Address:");
		String newAddress = userInput.nextLine();
		
		//	Reset project iterator for new while loop
		projIterator = allProjects.iterator();
		
		//	Loop through iterator
		while ( (projIterator.hasNext()) && (true)) {
			Project p = projIterator.next();
			
			//	Find selected project (based on name/number) and update project's contractor
			if ((p.getName().equals(projectN)) || (projectN.matches("\\d+") && (p.getNumber()==Integer.parseInt(projectN)))) {
				
				int currentPersonID = 0;
				
				//	Also declare people iterator
				Iterator<Person> peopleIt = allPeople.iterator();
				
				//	Loop through people iterator to also update person's details
				while ( (peopleIt.hasNext()) && (true)) {
					Person currentPerson = peopleIt.next();
					
					//	If person associated with project is found
					if (currentPerson.getName().equals(p.getContractor().getName()) ) {
						
						currentPersonID = currentPerson.getId();  
						//	Remove this person from list array
						allPeople.remove(currentPerson);
						
						//	Then update details 
						
						currentPerson.setId(currentPersonID);
						currentPerson.setName(newName);
						currentPerson.setTelephone(newTelephone);
						currentPerson.setEmail(newEmail);
						currentPerson.setAddress(newAddress);
						
						//	Add person with updated details to list array
						allPeople.add(currentPerson);
						
						//	Run method that updates person text file
						try {
							updatePersonRecords(statement);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
				}
				
				
				//update this specific contractor
				p.setContractor(currentPersonID, newName, newTelephone, newEmail, newAddress);
				
				//print success message
				System.out.println("\nContractor's details successfully updated!\n");

				break;	//	Added break because not necessary to continue loop
			}
		}
		
		
		
	}
	
	
	/**
	 * Method that updates the total paid amount
	 */
	private static void changeTotPaid() {
		userInput.useLocale(Locale.US); //Change decimal system to . not , source: https://stackoverflow.com/questions/5929120/nextdouble-throws-an-inputmismatchexception-when-i-enter-a-double
		
		//print message to user
		System.out.println("\nChange Total Paid:");
		System.out.println("Projects listed below:");
		System.out.println("Project Number\tName\t\tTotal Fee\tTotal Paid");
		
		Iterator<Project> projIterator = allProjects.iterator();
		
		//	Loop throguh iterator to display all projects
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			System.out.println(p.getNumber()+"\t\t"+p.getName()+"\t\t"+p.getTotalFee()+"\t\t"+p.getTotalPaid());
		}
		
		userInput.nextLine();  // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
		
		System.out.println("\nPlease select a project number or name:");
		
		//	Get user's selection
		String projectN = userInput.nextLine();
		
		//	Reset iterator for next while loop
		projIterator = allProjects.iterator();
		
		//	Loop through iterator to find selected project
		while ( (projIterator.hasNext()) && (true)) {
			Project p = projIterator.next();
			
			//	Find selected project based on project name or project number input
			if ((p.getName().equals(projectN)) || (projectN.matches("\\d+") && (p.getNumber()==Integer.parseInt(projectN)))) {
				System.out.println("Enter updated paid amount:");
				
				// Keep asking user until correct value is received
				while(!userInput.hasNextDouble()) {
					System.out.println("\nEntered incorrect value");
					System.out.println("Please enter a price [double value]:");
					userInput.next();
				}
				
				//	Assign variable if input is double value
				double updatedPaid = 0;
				if (userInput.hasNextDouble()){
					updatedPaid = userInput.nextDouble();
				}
				
				//update paid amount
				p.setTotalPaid(updatedPaid);
				
				//display success message to user
				System.out.println("Total Paid has been successfully updated");
				System.out.println(p.getNumber()+"\t\t"+p.getName()+"\t\t"+p.getTotalFee()+"\t\t"+p.getTotalPaid()+"\n");
				
				//	Run method to update project text file
				try {
					updateProjectRecords(statement);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;	//	Added break because not necessary to continue loop
				
			}
		}
		

	}
	
	
	/**
	 * method that changes project due date
	 */
	private static void changeDueDate() {
		
		//display message to user
		System.out.println("\nProjects listed below:");
		System.out.println("Project Number\tName\t\tDue Date");
		
		Iterator<Project> projIterator = allProjects.iterator();
		
		//	Loop through iterator to display all projects
		while (projIterator.hasNext()) {
			Project p = projIterator.next();
			System.out.println(p.getNumber()+"\t\t"+p.getName()+"\t\t"+p.getDeadline());
		}
		
		userInput.nextLine(); // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo
		
		System.out.println("\nPlease select a project number or name:");
		
		//get user's selection
		String projectN = userInput.nextLine();
		
		//	Reset project iterator for next loop
		projIterator = allProjects.iterator();
		
		//	Loop through iterator to find selected project
		while ( (projIterator.hasNext()) && (true)) {
			Project p = projIterator.next();
			
			//	Find selected project based on project name or project number input
			if ((p.getName().equals(projectN)) || (projectN.matches("\\d+") && (p.getNumber()==Integer.parseInt(projectN)))) {
				
				//	Declare placeholder variables
				String newDeadlineStr = "";
				LocalDate newDeadline = LocalDate.now();
				
				//	Keep asking user until correct value is received
				while (true) {
					
					System.out.println("Enter new deadline [format: 'yyyy-mm-dd']");
					
					newDeadlineStr = userInput.nextLine();
					
					//	Assign deadline variable if in correct format and end loop   
					if (newDeadlineStr.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
						newDeadline = LocalDate.parse(newDeadlineStr);
						break;
					}
					else {	//	Else show error message and loop again
						System.out.println("\nEntered incorrect value");
						System.out.println("Please enter a date [format: 'yyyy-mm-dd']:");
					}
					
				}
				
				//update deadline
				p.setDeadline(newDeadline);
				
				//	Update project text file
				try {
					updateProjectRecords(statement);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//print success message
				System.out.println("Due date has successfully been changed!");
				
				System.out.println(p.getNumber()+"\t"+p.getName()+"\t"+p.getDeadline()+"\n");				
				
				break;	//	Added break because not necessary to continue loop
			}
		}
		
		
		
	}
	
	
	/**
	 * method to create a new project
	 */
	private static void createProject() {	
		
		userInput.useLocale(Locale.US); //Change decimal system to . not , source: https://stackoverflow.com/questions/5929120/nextdouble-throws-an-inputmismatchexception-when-i-enter-a-double
		
		//print message to user
		System.out.println("\nCreating a new project");
		System.out.println("Please enter the following details...");
		
		//The following lines ask user to input variables for new project
		
		System.out.println("Project Number:");
		
		// Keep asking user until correct value is received
		while(!userInput.hasNextInt()) {
			System.out.println("\nEntered incorrect value");
			System.out.println("Please enter an integer value:");
			userInput.next();
		}
		
		//	Assign variable if input is int value
		int projectNum = 0;
		if (userInput.hasNextInt()){
			projectNum = userInput.nextInt();
		}
		
		userInput.nextLine();  // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo 
		
		System.out.println("Project Name:");
		String projectName = userInput.nextLine();
		
		System.out.println("Building Type (House, Apartment etc.):");
		String buildingType = userInput.nextLine();
		
		System.out.println("Address:");
		String address = userInput.nextLine();
		
		System.out.println("Erf Number:");
		
		// Keep asking user until correct value is received
		while(!userInput.hasNextInt()) {
			System.out.println("\nEntered incorrect value");
			System.out.println("Please enter an integer value:");
			userInput.next();
		}
		
		//	Assign variable if input is int value
		int erfNum = 0;
		if (userInput.hasNextInt()){
			erfNum = userInput.nextInt();
		}
		
		System.out.println("Total Fee:");
		
		// Keep asking user until correct value is received
		while(!userInput.hasNextDouble()) {
			System.out.println("\nEntered incorrect value");
			System.out.println("Please enter a price [double value]:");
			userInput.next();
		}
		
		//	Assign variable if input is int value
		double totFee = 0;
		if (userInput.hasNextDouble()){
			totFee = userInput.nextDouble();
		}
		
		System.out.println("Total Paid:");
		
		// Keep asking user until correct value is received
		while(!userInput.hasNextDouble()) {
			System.out.println("\nEntered incorrect value");
			System.out.println("Please enter a price [double value]:");
			userInput.next();
		}
		
		//	Assign variable if input is int value
		double totPaid = 0;
		if (userInput.hasNextDouble()){
			totPaid = userInput.nextDouble();
		}
		
		userInput.nextLine();
		
		//	Declare placeholder variables
		String deadlineStr = ""; 
		LocalDate deadline = LocalDate.now();
		
		// Keep asking user until correct value is received
		while (true) {
			
			System.out.println("Deadline [format: 'yyyy-mm-dd']:");
			
			deadlineStr = userInput.nextLine();
			
			//	Only assign LocalDate variable if in correct format (and end loop)
			if (deadlineStr.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$")) {
				deadline = LocalDate.parse(deadlineStr);
				break;
			}
			else {	//	Else continue loop
				System.out.println("\nEntered incorrect value");
				System.out.println("Please enter a date [format: 'yyyy-mm-dd']:");
			}
			
		}
		
		//display architect options to user
		System.out.println("\nArchitect options:");
		System.out.println("a\t-\tAdd an existing architect");
		System.out.println("n\t-\tNew architect");
		System.out.println("Please select an option:");
		
		//get user's selected option
		char archSelection = userInput.next().charAt(0);
		
		//declare architect variable
		Person architect = null;
		
		//	Either add existing or create new person and assign as architect variable
		if (archSelection=='a') {
			System.out.println("Add existing Architect...\n");
			
			//	Run method to get existing architect person
			architect = getExistingPerson("architect");
			
		}
		else {
			
			System.out.println("Create New Architect...");
			
			//	Run method to create new architect person
			architect = createPerson("architect");
		}
		
		//display contractor options
		System.out.println("\nContractor options:");
		System.out.println("a\t-\tAdd an existing contractor");//I will make this option available when we use text files for storage
		System.out.println("n\t-\tNew contractor");
		System.out.println("Please select an option:");
		
		//get user's selection
		char conSelection = userInput.next().charAt(0);
		
		//declare person variable
		Person contractor = null;
		
		//	Either add existing or create new person and assign as contractor variable
		if (conSelection=='a') {
			System.out.println("Add existing Contractor...\n");
			
			//	Run method to get existing contractor person
			contractor = getExistingPerson("contractor");
			
		}
		else {
			
			System.out.println("Create New Contractor...");
			
			//	Run method to create new contractor person
			contractor = createPerson("contractor");
		}
		
		//display customer options
		System.out.println("\nCustomer options:");
		System.out.println("a\t-\tAdd an existing customer");
		System.out.println("n\t-\tNew customer");
		System.out.println("Please select an option:");
		
		//get user's selection
		char custSelection= userInput.next().charAt(0);
		
		//declare person variable
		Person customer = null;
		
		//	Either add existing or create new person and assign as customer variable
		if (custSelection == 'a') {
			System.out.println("Add existing Customer...\n");
			
			//	Run method to get existing customer person
			customer = getExistingPerson("customer");
			
		}
		else {
			
			System.out.println("Create New Customer...");
			
			//	Run method to create new customer person
			customer = createPerson("customer");
		}
		
		//additional condition based on instructions
		if (projectName=="") {
			
			String customerName = customer.getName();
			
			//split name by space
			String[] names = customerName.split(" ");
			
			//display project name with instructed changes
			projectName = buildingType +" "+names[1];
		}

		//create new project
		Project newProject = new Project(projectNum, projectName, buildingType, address, erfNum, totFee, totPaid, deadline, architect, contractor, customer); 
		
		//add new project to project array
		allProjects.add(newProject);
		
		//	Update project records text file
		try {
			updateProjectRecords(statement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("\nNew project successfully created.");
		
	}
	

	/**
	 * Method used to get existing person 
	 * (to assign as architect, contractor or customer)
	 * 
	 * @param type passed so that method can specify specific people objects
	 * @return returnedPerson person object that will be used
	 */
	private static Person getExistingPerson(String type) {
		
		userInput.nextLine();
		
		System.out.println("Available "+type+"s:");
		
		System.out.println("Name\t\tEmail\t\tTelephone\t\tAddress");
		
		Iterator<Person> pIterator = allPeople.iterator();
		
		//	Loop through iterator and display any person objects of the same type (architect, customer, contractor)
		while (pIterator.hasNext()) {
			Person p = pIterator.next();
			
			if (p.getType().equals(type)) {
				System.out.println(p.getName()+"\t\t"+p.getEmail()+"\t\t"+p.getTelephone()+"\t\t"+p.getAddress());
			}
			
		}
		
		//	If loop, architect specified for vowel in output string ('an' instead of 'a')
		if (type=="architect") {
			System.out.println("Please select an "+type+" by name:");			
		}
		else {
			System.out.println("Please select a "+type+" by name:");
		}
		
		String name = userInput.nextLine();
		
		//	Declare variables for person
		int pId = 0;
		String pName = "";
		String pEmail = "";
		String pTel = "";
		String pAddress = "";
		
		//	Reset person iterator for next while loop
		pIterator = allPeople.iterator();
		
		//	Loop through iterator to find person with same name
		while ( (pIterator.hasNext()) && (true)) {
			Person p = pIterator.next();
			if (p.getName().equals(name)) {
				
				//	Assign return person variables with that specific person's variables
				pId = p.getId();
				pName = p.getName();
				pEmail = p.getEmail();
				pTel = p.getTelephone();
				pAddress = p.getAddress();
				
				break;
			}
		}
		
		//	Create a person object to return
		Person returnedPerson = new Person(pId, pName, pEmail, pTel, pAddress, type);
		
		//	Return person object
		return returnedPerson;
	}
	
	
	/**
	 * method to create new person object
	 * 
	 * @param personType specify person type
	 * @return newPerson returned person to add inside project object
	 */
	private static Person createPerson(String personType) {
		
		userInput.nextLine();
		
		//The following lines ask user to input variables for new person
		System.out.println("Person ID:");
		
		// Keep asking user until correct value is received
		while(!userInput.hasNextInt()) {
			System.out.println("\nEntered incorrect value");
			System.out.println("Please enter an integer value:");
			userInput.next();
		}
		
		//	Assign variable if input is int value
		int personId = 0;
		if (userInput.hasNextInt()){
			personId = userInput.nextInt();
		}
		
		userInput.nextLine();  // Consume newline left-over -- solving the problem of using nextLine after nextInt(). Source: https://stackoverflow.com/questions/13102045/scanner-is-skipping-nextline-after-using-next-or-nextfoo 
		
		
		System.out.println("Enter name:");
		String name = userInput.nextLine();
		
		System.out.println("Enter telephone:");
		String telephone = userInput.nextLine();
		
		System.out.println("Enter email:");
		String email = userInput.nextLine();
		
		System.out.println("Enter address:");
		String address = userInput.nextLine();
		
		
		//create new person as newToProject
		Person newPerson = new Person(personId, name, telephone, email, address, personType);
		
		Iterator<Person> pIterator = allPeople.iterator();
		
		//	Declare boolean variable to determine whether person should be added or not
		boolean addNewPerson = true;
		
		//	Loop thorugh iterator to check if person exists
		while ( (pIterator.hasNext()) && (true)) {
			Person p = pIterator.next();
			if (p.getName().equals(newPerson.getName())) {
				
				//	If person exists, then change boolean to false to not add person again
				addNewPerson = false;
				break;
			}
		}
		
		//	Only add person to person list array and text file if boolean variable is true 
		if (addNewPerson) {
			allPeople.add(newPerson);
			
			// Run method that updates the person table on the sql database
			try {
				updatePersonRecords(statement);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//return person variable
		return newPerson;
		
	}
	
	
	/**
	 * Method that takes the person 
	 * list array and writes it to a text file
	 * 
	 * @param statement passed as parameter to simplify program
	 * @throws SQLException necessary if sql error occurs
	 */
	private static void updatePersonRecords(Statement statement) throws SQLException{
		//first delete everything
		//then insert everything again
		
		// Delete book if book id matches id of input
		rowsAffected = statement.executeUpdate("DELETE FROM people");
		System.out.println("Delete Query complete, " + rowsAffected + " rows removed.");
		
		//	Declare iterator and use it in while loop
		Iterator<Person> peopleIterator = allPeople.iterator();
		
		while (peopleIterator.hasNext()) {
			String query = peopleIterator.next().getPersonSqlQuery();

			// Insert into book database table and print success message
			rowsAffected = statement.executeUpdate(query);
			System.out.println("Insert query complete, " + rowsAffected + " rows added.");
		}
		
	}
	
	
	/**
	 * Method that reads person text file 
	 * and adds people objects to list array
	 * 
	 * @param statement passed as parameter to simplify sql code
	 * @throws SQLException necessary for possible sql error
	 */
	private static void loadExistingPeople(Statement statement) throws SQLException{
		
		// Query that gets all variables for every book entry
		results = statement.executeQuery("SELECT * FROM people");
		
		// Loop over the results, printing them all.
		while (results.next()) {
			//System.out.println(results.getInt("id") + "\t\t"+ results.getString("title") + "\t\t"+ results.getString("author") + "\t\t\t"+ results.getInt("qty"));
			
			//create new person as newToProject
			Person loadPerson = new Person(results.getInt("id"), results.getString("name"), results.getString("telephone"), results.getString("email"), results.getString("address"), results.getString("personType"));
			
			//add new person to person array
			allPeople.add(loadPerson);
			
		}

	}
	
	
	/**
	 * Method that takes the project 
	 * list array and writes it to a text file
	 * 
	 * @param statement passed to simplify code
	 * @throws SQLException needed for possible sql error
	 */
	private static void updateProjectRecords(Statement statement) throws SQLException{
		
		// Delete book if book id matches id of input
		rowsAffected = statement.executeUpdate("DELETE FROM projects");
		System.out.println("Delete Query complete, " + rowsAffected + " rows removed.");
				
		//	Declare iterator and use it in while loop
		Iterator<Project> projIterator = allProjects.iterator();
		
		while (projIterator.hasNext()) {
			
			//	Get query string from project class by running getProjectSqlQuery method 
			String query = projIterator.next().getProjectSqlQuery();
			
			// Insert into book database table and print success message
			rowsAffected = statement.executeUpdate(query);
			System.out.println("Insert query complete, " + rowsAffected + " rows added.");
		}
		
	}
	

	/**
	 * Method that reads project text file 
	 * and adds project objects to list array
	 * 
	 * @param statement passed as a parameter to simplify code
	 * @throws SQLException needed for possible sql error
	 */
	private static void loadExistingProjects(Statement statement) throws SQLException{
		
		// Query that gets all variables for every book entry
		results = statement.executeQuery("SELECT * FROM projects");
			
		// Loop over the results, printing them all.
		while (results.next()) {
			
			// Get integer values to match project entries to person table entries
			int custID = results.getInt("customerId");
			int conID = results.getInt("contractorId");
			int archID = results.getInt("architectId");
			
			// Create 3 people with placeholder values to assign later
			Person architect = new Person(0, "placeholder", "placeholder", "placeholder", "placeholder", "placeholder");
			Person customer = new Person(0, "placeholder", "placeholder", "placeholder", "placeholder", "placeholder");
			Person contractor = new Person(0, "placeholder", "placeholder", "placeholder", "placeholder", "placeholder");
			
			//	Also declare people iterator
			Iterator<Person> peopleIt = allPeople.iterator();
			
			//	Loop through people iterator to also update person's details
			while (peopleIt.hasNext()) {
				Person currentPerson = peopleIt.next();
				
				//	If person associated with project is found
				if (currentPerson.getId() == archID ) {
					architect = currentPerson;
				}
				
				if (currentPerson.getId() == custID ) {
					customer = currentPerson;
				}
				
				if (currentPerson.getId() == conID ) {
					contractor = currentPerson;
				}
				
			}
			
			// Create project object
			Project loadProject = new Project(results.getInt("number"), results.getString("name"), results.getString("buildingType"), results.getString("address"), results.getInt("erfNumber"), results.getDouble("totalFee"), results.getDouble("totalPaid"), results.getDate("deadline").toLocalDate(), architect, contractor, customer );
			
			//	Set completion date of project if not null
			if (results.getDate("deadline")!=null) {
				loadProject.setCompletionDate(results.getDate("deadline").toLocalDate());
			}
			
			//	Set finalised status of project if not false
			if (results.getBoolean("finalisedProject")) {
				loadProject.setFinalisedStatus(results.getBoolean("finalisedProject"));
			}
			
			//add new person to person array
			allProjects.add(loadProject);
			
		}
			
	}

}
