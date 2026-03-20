# spring-boot-example

A Spring Boot example application.

### Prerequisites

- Java SE 21

### Background

This Spring Boot application provides CRUD (Create, Read, Update, Delete) operations via a REST API for an employee domain model. 

A JSON PATCH operation (partial update) is also supported whose JSON is defined by [RFC (Request For Comments) 6902](https://datatracker.ietf.org/doc/html/rfc6902). The [json-patch](https://github.com/java-json-tools/json-patch) API implements the RFC and is used in the project to initiate partial updates when consuming and producing JSON. Refer to the article, [Using JSON Patch in Spring](https://www.baeldung.com/spring-rest-json-patch), or consult the [RFC](https://datatracker.ietf.org/doc/html/rfc6902) to learn more about creating the proper JSON for partial updates.

An XML PATCH operation is also supported whose XML is defined by [RFC 5261](https://datatracker.ietf.org/doc/html/rfc5261). The [xml-patch](https://github.com/dnault/xml-patch) API implements the RFC and is used in the project to initiate partial updates when consuming and producing XML. The API is also compatible with the more recent [RFC 7351](https://www.rfc-editor.org/rfc/rfc7351.html) standard. The primary difference between those RFCs is the root element name requirement. Refer to the API's [README](https://github.com/dnault/xml-patch?tab=readme-ov-file#xml-patch) and both the [RFC 5261](https://datatracker.ietf.org/doc/html/rfc5261) and [RFC 7351](https://www.rfc-editor.org/rfc/rfc7351.html) documentation to learn more about creating the proper XML for partial updates.

Upon application start-up an in-memory database (H2 database) is seeded with sample data. Verify the Spring Boot application is running by navigating to the below server address in your preferred browser.

`localhost:8080/employees`

You can also use `curl` in a terminal window: `curl localhost:8080/employees`

The application supports consuming/producing (request/response) both XML and JSON. Navigating to `localhost:8080/employees` from the browser will produce XML output. You can use tools such as [Postman](https://www.postman.com/) to execute the CRUD REST verbs (POST, GET, PUT, DELETE) while specifying different options (e.g., parameters, authorization, headers, request body, etc.).

### Building

The application can be built via [Maven](https://maven.apache.org/) in a couple of ways. Execute the below from the application's root folder via the terminal window.

1. Create a JAR file without executing tests.
	- `./mvnw package -DskipTests`
2. Execute tests and create a JAR file.
	- `./mvnw package`

The above will create a JAR file in the project's root folder's **target** directory.

### Running

Type the below command from a terminal window in the project's root folder.

**MacOS/Linux**

`./mvnw spring-boot:run`

**Note: If `mvnw` is not executable run the below from the terminal window.**

`chmod +x mvnw`

**Windows**

`mvnw spring-boot:run`

You can also run the application, after it's built, by executing the below command from the terminal window.

`java -jar spring-boot-example-0.0.1-SNAPSHOT.jar`

