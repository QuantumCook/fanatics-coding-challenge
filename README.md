# Bet Fanatics Coding Challenge

### Overview
This basic code challenge interfaces with an existing REST-based API and performs
various operations against that API.  


The API and its documentation are located here:  https://gorest.co.in/

### Implementation Description

The application uses Spring-Boot MVC and Java 11.

A single REST endpoint is used to launch the app workflow.

### Workflow Operations

1. Retrieve page 3 of the list of all users.
2. Using a logger, log the total number of pages from the previous request.
3. Sort the retrieved user list by name.
4. After sorting, log the name of the last user.
5. Update that user's name to a new value and use the correct http method to save it.
6. Delete that user.
7. Attempt to retrieve a nonexistent user with ID 5555.  Log the resulting http response code.

### Running the application

The application can be launched through Intellij or the command line.

#### Intellij

- Navigate to the application [class](code-challenge/src/main/java/com/cook/codechallenge/CodeChallengeApplication.java)
- Click the run configuration option in the gutter.

#### Command Line

- execute `mvn spring-boot:run`

#### Launch
Once the application is running, access http://localhost:8080/challenge/launch to launch the workflow