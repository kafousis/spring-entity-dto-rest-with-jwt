# Spring Entity-Dto Rest with JWT
Spring Boot project that shows how to build and secure a RESTful API using Spring Data JPA and Spring Security. The example implements Role Based Access Control (RBAC) by mapping certain privileges to roles which are then assigned to the application users.

## Software Stack
- Gradle
- Docker
- H2 Database
- PostgreSQL
- Spring Boot
- Spring Data JPA
- Spring Security
- Json Web Tokens
- Springdoc-OpenAPI
- ModelMapper

## PostgreSQL / H2
You can either use PostgreSQL or H2 database with this project.

If you choose to use PostgreSQL you can either use a local instance running on your computer, or use the provided docker-compose.yaml file to create new instances of PostgreSQL and pgAdmin using Docker. When your PostgreSQL instance is up and running do not forget to open **application.properties** to provide the appropriate database url and valid credentials.

The other option is H2, which by default, stores all the data in memory. This means that the data will be lost when you close or restart the application. If you want to make the data persist you can change this behavior by using file-based storage.

## Run project
```shell
# run with PostgreSQL
./gradlew bootRun

# run with H2
./gradlew bootRun -Ph2
```
When the project runs Hibernate generates the database tables based on the Entity models. The database is then populated with some data using CommandLineRunner.

> If you are using PostgreSQL keep in mind that the database tables and data will be deleted when the application stops or restarts because of the **spring.jpa.hibernate.ddl-auto=create-drop** property in application.properties file.

The inserted data include 3 users:
- username: admin, pass: admin
- username: manager, pass: manager
- username: user, pass: user

## Swagger UI 
Swagger UI is a user interface based on the OpenAPI specification which allows anyone to see available API endpoints and interact with them, along with API documentation.

After you run the project, the Swagger UI page will be available at:

> http://localhost:8080/swagger-ui.html

## Example cURL requests

### Login
```shell
curl --request POST \
     --header "Content-Type: application/json" \
     --data '{"username":"admin","password":"admin"}' \
     --include \
     http://localhost:8080/login
```

You will receive access_token and refresh_token in response headers.

### Sample API request using access_token
```shell
curl --header "Authorization: Bearer <access_token>" \
     http://localhost:8080/api/users
```

### Refresh expired token
```shell
curl --header "Authorization: Bearer <refresh_token>" \
     --include \
     http://localhost:8080/token/refresh
```