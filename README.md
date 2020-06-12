LBUS Application (Location Based User Service):-

	This service provides following operations
	
	1. FindUsersByCity
	2. GetUsers
	3. GetUserById

FindUsersByCity:

	Method to find the users located in the specific city given.
	
GetUsers:

	Fetch all users.
	
GetUserById:

	Fetch user details by given id.

Technical Features:

	1. API Specification provided in the resources folder in app.json (written using Open API 3)
	2. API developed using Vert.x libraries version 3.9.1
	3. Handlers are created based on the operationId specified in the API Specification
	4. Load configuration details from config.json
	5. Logging configuration details are given in logback.xml under resources folder
	6. Test cases are written using Junit 5
	7. Java 8 used for development 
	8. Gradle version 6.0 used for build
	
	
To Build this project follow the below steps:

	1. Import project as Gradle project in any IDE like Eclipse
	2. Open Gradle Tasks view
	3. Go to LBUSApp -> build
	4. Click on build
	5. This will build your project and generate the LBUSApp-1.0.0.jar under LBUSApp\build\libs folder
	
To Run the application follow the below steps:

	1. Open Command prompt 
	2. Go to LBUSApp\build\libs folder 
	3. Execute 'java -jar LBUSApp-1.0.0.jar' command
	4. This will start the application in localhost on port 8080
	
To Test the application follow the below steps:

	1. To test FindUsersByCity API 
	2. Go to any browser or any Rest Client like Post Man
	3. Hit 'http://localhost:8080/city/London/users'
	4. You should be able to see the response in page
	5. Similar way to getUsers, use 'http://localhost:8080/users'
	6. To getUserById, use 'http://localhost:8080/user/1'
	
To view the test reports follow the below steps:

	1. Go to LBUSApp\build\reports\tests\test
	2. Click on Index.html
	3. You should be able to see the test reports
	