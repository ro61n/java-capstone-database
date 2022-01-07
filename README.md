# Capstone Project Lvl3T8 - OOP: 02/12/2021

The final version of a Java-coded project that allows a structural engineering firm to keep track of their projects and the people that it involves.

It does this by accepting user inputs and storing various information related to the projects and or people as relevant objects. There are multiple people objects involved in a project object, namely a contractor, architect and client.

When the program is run, a user is greeted with a menu that allows for various options such as to create a new project, change the due date or finalise a project. These options run methods which carry out the required instructions. The methods may call other classes (and methods in those classes) to achieve the required instructions.

The "Main" class is the class that contains the application loop and there are two other classes, namely the project class and the people class. The latter two classes are used for the objects that they relate to.

All the project and people objects are stored in SQL databases (poisepms) and are regularly saved (after a method is executed or any change is made). There are two tables inside the ‘poisepms’ database, namely the 'project' and 'people' table. When the program starts, all the rows from the two tables inside the SQL database are converted into relevant people or project objects. By storing them back onto the table, they are simply converted into a SQL format again.

Robin Titus is the maintainer and contributor of this project.

In order to run this application, you can open this project from Eclipse and click on the green run icon.
