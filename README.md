![travis-ci](https://travis-ci.org/PanteliienkoDanylo/bank-application-test-task.svg?branch=master)

## Bank Application Test Task

### Simple banking application. Operations: 
 - authorization
    - POST http://localhost:8080/bank-app-test-task/api/login
    - will return token
 - deposit or withdraw money
    - POST http://localhost:8080/bank-app-test-task/api/deposit
    - POST http://localhost:8080/bank-app-test-task/api/withdraw
 - get balance
    - GET http://localhost:8080/bank-app-test-task/api/balance
 - get operation histories
    - GET http://localhost:8080/bank-app-test-task/api/user/operation-histories
 
#### Technologies: Spring Boot, Spring Data JPA, Spring Security, H2, Swagger UI, Travis-CI 

#### Swagger-UI: http://localhost:8080/swagger-ui.html

#### run test
`mvn test`

#### build jar
`mvn clean package`

#### run jar
`java -jar bank-application-test-task-1.0-SNAPSHOT.jar`

