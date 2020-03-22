## Bank Application Test Task

### Change the next properties:

#### If you use MySQL:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

#### IF you use not MySQL

- `change dependecy in pom.xml`
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.datasource.driver-class-name`
- `spring.jpa.properties.hibernate.dialect`

### run test
`mvn test`

### build jar
`mvn clean package`

### run jar
`java -jar bank-application-test-task-1.0-SNAPSHOT.jar`

