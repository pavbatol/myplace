![myplace.png](logo.png)

[RU](README-RU.md)

### What is the "MyPlace" application
The server part for the marketplace.  
You can organize your own platform, invite sellers, attract buyers and earn money.

<u>_Project in development_</u>

### Ready-made services:
- **Statistics (mp-stats)** `MongoDB reactive, Webflix, Junit, Mockito`  
  - The service has been tested by JUnit tests, working with the 'mp-stats-test' database, which is automatically cleared after the tests.  
  - A collection of Postman tests is attached. Run on an empty database. I recommend starting the service with the 'test' profile, the work will be carried out with the 'mp-stats-test' test database, which you can delete before / after the tests to clear the data. Otherwise, the work will take place with the real base of 'mp-stats', then after the tests do not forget to clear the database.
- **Security (mp-security)** `PostgreSql, Redis, H2, Spring Security`  
  - Checking user registration and access rights by JWT.  
  - A collection of Postman tests is attached. Run on an empty database with the 'test' profile. You can add marker profiles:
    test-confirmation-code-reading and test-mail-sender-bypassing to automatically read the registration confirmation code and to bypass the error if MAIL_SENDER is not specified yet, respectively.

### Intended technologies:
- Microservice architecture
- Maven
- Spring Boot
- Spring Data
- Spring Cloud
- Spring Security
- Tomcat, Netty
- Kafka
- JDBC, Hibernate
- Liquibase
- Postgresql, H2, MongoDB (reactive), Redis, Elasticsearch
- Junit, Mockito
- Docker-compose
- Kubernetes