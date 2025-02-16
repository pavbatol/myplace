![myplace.png](docs/images/logo.png)

[RU](README-RU.md)

### What is the "MyPlace" application
The server part for the marketplace.  
You can organize your own platform.

> **!** _Project in development_

### Ready-made services:
- **Statistics (mp-stats)** `MongoDB reactive, Webflix, Mapstruct, Junit, Mockito, Swagger`  
  - The service has been tested by JUnit tests, working with the 'mp-stats-test' database, which is automatically cleared after the tests.  
  - A collection of Postman tests is attached. Run on an empty database. I recommend starting the service with the 'test' profile, the work will be carried out with the 'mp-stats-test' test database, which you can delete before / after the tests to clear the data. Otherwise, the work will take place with the real base of 'mp-stats', then after the tests do not forget to clear the database.
- **Security (mp-security)** `JPA/Hibernate, Spring Security, PostgreSql, Redis, H2, Mapstruct, Swagger`  
  - Checking user registration and access rights by JWT.  
  - A collection of Postman tests is attached. Run on an empty database with the 'test' profile. You can add marker profiles:
    test-confirmation-code-reading and test-mail-sender-bypassing to automatically read the registration confirmation code and to bypass the error if MAIL_SENDER is not specified yet, respectively.
- **Profile (mp-profile)** `JPA/Hibernate, Liquibase, PostgreSql, H2, Mapstruct, Swagger`
  - The user profile service provides work with data: from the date of registration to the avatar. 
  - It contains the `Geo` service — working with addresses.
  - Loading a preliminary set of geo-data: all countries; regions, districts, and cities of Russia; streets of Moscow, St. Petersburg, Balakovo.
  - Geo-data administration: Ability to import data from a CSV file with a loading report.
  
    <div style="margin-left: 20px;">

    Example of the uploaded file and the generated report, also in CSV format (see [sample-file-to-import-geo.csv](docs/files/sample-file-to-import-geo.csv) and [sample-geo-data-load-report.csv](docs/files/sample-geo-data-load-report.csv)).  
    For convenience, a tabular representation is provided.  
    The process ensures that duplicate entries are excluded, and the data is sorted sequentially by the following fields: `Country,Region,District,City,Street,House`.

    <details>
    <summary>Import:</summary>

    ![sample-file-to-import_geo-data.png](docs/images/sample-file-to-import_geo-data.png)

    </details>

    <details>
    <summary>Report:</summary>

    ![sample-geo-data-load-report.png](docs/images/sample-geo-data-load-report.png)

    </details>

    </div>  

---

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

---

### Open API

To visualize the specification, paste it into any Swagger editor. For example this one: https://editor.swagger.io

| Service     | Swagger UI URL                                                             | OpenAPI Docs URL                           | Specification                                                                        |
|-------------|----------------------------------------------------------------------------|--------------------------------------------|--------------------------------------------------------------------------------------|
| mp-test     | [localhost:9090/docs/swagger-ui.html](localhost:9090/docs/swagger-ui.html) | [localhost:9090/docs](localhost:9090/docs) | [mp-stats-v-1-0-0.json](docs/specification/mp-stats-service-spec-v-1-0-0.json)       |
| mp-security | [localhost:8078/docs/swagger-ui.html](localhost:8078/docs/swagger-ui.html) | [localhost:8078/docs](localhost:8078/docs) | [mp-security-v-1-0-0.json](docs/specification/mp-security-service-spec-v-1-0-0.json) |
| mp-profile  | [localhost:8076/docs/swagger-ui.html](localhost:8076/docs/swagger-ui.html) | [localhost:8076/docs](localhost:8076/docs) | [mp-profile-v-1-0-0.json](docs/specification/mp-profile-service-spec-v-1-0-0.json)   |

# Running the Project

#### Make sure you have the following installed:
- JDK 11
- Apache Maven
- Docker (version 19.03.0 or higher)
- Docker Compose (version 1.28.0 or higher)

> **Note:** Execute all commands from the project root.  
> For ease of use, a standard and an alternative option are provided using the manage.sh script.
> This script is designed for use on Linux-based systems, including but not limited to Ubuntu, CentOS, Debian, and other distributions. 
> The script is not intended to run on Windows or MacOS without additional tools, such as WSL (Windows Subsystem for Linux) or proper environment configuration.

#### 1. Build the Project

<div style="margin-left: 20px;">

Build with tests before packaging. The following services must be installed and running:
- Redis
- MongoDB
- PostgreSQL

```bash
mvn clean package
```

Build without running tests

```bash
mvn clean package -DskipTests
```

If you want to completely skip the test compilation and not include them in the build, you can use:

```bash
mvn clean package -Dmaven.test.skip=true
````

</div>

#### 2. Run the Project (profiles: `test`, `develop`, `production`)

<div style="margin-left: 20px;">

For the `production` profile, fill in the corresponding environment variable files:
- docker/stats/.env
- docker/security/.env, docker/security/env.security.prod
- docker/profile/.env


To run each profile, execute the following commands:  
(including the `healthcheck`, so please wait)

<div style="margin-left: 20px;">

<details>
<summary>Run with the `test` profile</summary>

`test` profile


  ```bash
  docker-compose -f docker/docker-compose-test.yml --profile full up
  ```
or
  ```bash
  docker/manage.sh test up
  ```

[//]: # (</div>)

</details>

<details>
<summary>Run with the `develop` profile</summary>

`develop` profile

[//]: # (<div style="margin-left: 20px;">)

 ```bash  
 docker-compose -f docker/docker-compose-dev.yml --profile full up
 ```
or
 ```bash
 docker/manage.sh dev up
 ```

[//]: # (</div>)

</details>

<details>
<summary>Run with the `production` profile</summary>

`production` profile

[//]: # (<div style="margin-left: 20px;">)

 ```bash  
 docker-compose -f docker/docker-compose-prod.yml --profile full up
 ```
or
 ```bash
docker/manage.sh prod up
 ```

> NOTE: In the standard setup (without using a script),
> you can specify an override file for docker-compose to set different values for services.
> This is useful for testing in a 'production' environment.  
Example:
> ```bash
>  docker-compose -f docker/docker-compose-prod.yml -f docker/docker-compose-prod.override.yml --profile full up
>  ```

</details>

</div>

</div>

<div style="margin-left: 20px;">

Running services individually (examples with different profiles)

<div style="margin-left: 20px;">

<details>
<summary>Run the `mp-stats` service</summary>

`mp-stats` service

<div style="margin-left: 20px;">

`test` profile

```bash  
docker-compose -f docker/docker-compose-test.yml --profile stats up
```
or
```bash 
docker/manage.sh test up stats
```

`develop` profile

 ```bash  
docker-compose -f docker/docker-compose-dev.yml --profile stats up
 ```
or
 ```bash
docker/manage.sh dev up stats
 ```

`production` profile

 ```bash  
docker-compose -f docker/docker-compose-prod.yml --profile stats up
 ```
or
 ```bash
docker/manage.sh prod up stats
 ```

</div>

</details>

<details>
<summary>Run the `mp-security` service`</summary>

`mp-security` service

<div style="margin-left: 20px;">

`test` profile

```bash  
docker-compose -f docker/docker-compose-test.yml --profile security up
```
or
```bash 
docker/manage.sh test up security
```

`develop` profile

 ```bash  
docker-compose -f docker/docker-compose-dev.yml --profile security up
 ```
or
 ```bash
docker/manage.sh dev up security
 ```

`production` profile

 ```bash  
docker-compose -f docker/docker-compose-prod.yml --profile security up
 ```
or
 ```bash
docker/manage.sh prod up security
 ```

</div>

</details>

<details>
<summary>Run the `mp-profile` service`</summary>

`mp-profile` service

<div style="margin-left: 20px;">

`test` profile

```bash  
docker-compose -f docker/docker-compose-test.yml --profile profile up
```
or
```bash 
docker/manage.sh test up profile
```

`develop` profile

 ```bash  
docker-compose -f docker/docker-compose-dev.yml --profile profile up
 ```
or
 ```bash
docker/manage.sh dev up profile
 ```

`production` profile

 ```bash  
docker-compose -f docker/docker-compose-prod.yml --profile profile up
 ```
or
 ```bash
docker/manage.sh prod up profile
 ```

</div>

</details>

</div>

</div>
