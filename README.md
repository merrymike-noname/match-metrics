# MatchMetrics

Webapp for getting information about football teams and upcoming matches including the results probabilities.

## Technologies Used in the Project

- **Spring Boot:** for rapid creation and configuration of Spring applications, simplifying development and deployment.
- **Spring Web:** allows for creating RESTful APIs for data exchange with the system's clients.
- **Spring Data:** used for working with databases, including the use of Hibernate ORM for data access.
- **Spring Security:** ensures the security of the application, including user authentication and authorization.
- **Hibernate ORM:** used for database interaction and provides object-relational mapping (ORM) capabilities.
- **Flyway:** used for performing database migrations.
- **SQL:** used for creating and managing the database, including tables for hotels, rooms, reservations, and clients.
- **JUnit and Mockito:** used for writing and automating code testing to ensure its quality and reliability.
- **Swagger-UI:** tool for creating UI documentation.
- **Maven:** used for managing project dependencies and building.

## Key Features

- User authentication and authorization (based on JWT)
- Automatic updates for the matches list and team stats
- Representing data including match results predictions and team statistics
- Team comparison
- Match calendar
- Specialized panel for ADMIN users

## Build

To run the service:
```shell
mvn spring-boot:run
```
You should adjust your `properties` file according to the template defined in `application.properties.origin`
