# Aged-Care-Management-System-Advanced-Programming2
An aged care management system written in Java for Advanced Programming Assignment 2

## Installation
The project was created in Eclipse and saved as a general archive file. 
It can be reproduced in eclipse by creating a new project, then importing the “Assignment2.zip” archive file into that project. All files have been saved, including the database files, serialization files and text files that are used by the application. 

Java version used: Java SE 15 (jdk 15.0.2)

JavaFX used: javafx sdk 16

Eclipse used: Version 2020-12

## Design Decisions
The application was designed with the MVC model where the Model contains the business logic, view contains the GUI and the controller connects the two together. This allowed me to change and configure the menu without having to change any of the underlying business logic.
I decided to use the Singleton pattern for the main Model (which is CareHome) as I only wanted one instance of this class. On startup, CareHome loads its initial state from a serialized file. 
The main audit data is saved into the HSQL Database that is using the Data Access Object design patterns. Employee, Resident, administration of medicine and change of prescription is all saved into this database. In addition, there is a complete separate log file (actionlog.txt) that saves every single action made in the application (this is for internal use). I’ve separated the two as the audit database can be provided as a whole to an external auditor without providing trivial log information. Users and logins are controlled via serialization for the same reason as the above. 
All user input is restricted to the user that is currently logged into the system. For example, any functions that don’t relate to a nurse are greyed out. 
All input fields are checked and/or restricted in what input can be provided to them. For example, the dose text field that requires only numbers can only have numbers input into them. OK buttons to save information are also greyed out when the information in input fields is not valid. 
Compliance for shifts is valid only when there is at least one nurse assigned to all morning and afternoon shifts AND at least one doctor is assigned one shift per day. Until this compliance isn’t met, there will be a message displayed in the shifts menu of the application. This will automatically be removed when compliance is reached. 
There is no need to manually save any part of the program, the database, text log file and serialized data file are all automatically updated and saved. 
If any of the files are deleted (Eg. database files, log file, serialized data file), they will be automatically re-created on the startup of the program. The database will create its tables the first time the application is run. The login for the first time user is ‘admin’, ‘admin’. 



