![myplace.png](logo.png)

[EN](README.md)

### Приложение "MyPlace"
Серверная часть для маркетплейса.  
Вы можете организовать свою собственную торговую площадку.  
Приглашайте продавцов, привлекайте покупателей и зарабатывайте.

<u>_Проект в разработке_</u>

### Готовые сервисы:
- **Статистика (mp-stats)** `MongoDB reactive, WebFlux, Junit, Mockito`   
  - Сервис протестирован JUnit тестами, работая с базой 'mp-stats-test', которая после тестов автоматически очищается.  
  - Приложена коллекция Postman тестов. Запускать на пустой базе. Рекомендую запустить сервис с профилем 'test', работа будет вестись с тестовой базой 'mp-stats-test', которую вы можете перед/после тестов удалять для очистки данных. Иначе работа будет происходить с реальной базой 'mp-stats', тогда после тестов не забудьте очистить базу.
- **Безопасность (mp-security)** `PostgreSql, Redis, H2, Spring Security`  
  - Проверка регистрации пользователя и прав доступа по JWT.  
  - Приложена коллекция Postman тестов. Запускать на пустой базе.

### Планируемый стек:
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