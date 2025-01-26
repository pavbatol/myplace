![myplace.png](docs/images/logo.png)

[EN](README.md)

### Приложение "MyPlace"
Серверная часть для маркетплейса.  
Вы можете организовать свою собственную торговую площадку.  

<u>_Проект в разработке_</u>

### Готовые сервисы:
- **Статистика (mp-stats)** `MongoDB-reactive, WebFlux, Mapstruct, Junit, Mockito, Swagger`   
  - Сервис протестирован JUnit тестами, работая с базой 'mp-stats-test', которая после тестов автоматически очищается.  
  - Приложена коллекция Postman тестов. Запускать на пустой базе. Рекомендую запустить сервис с профилем 'test', работа будет вестись с тестовой базой 'mp-stats-test', которую вы можете перед/после тестов удалять для очистки данных. Иначе работа будет происходить с реальной базой 'mp-stats', тогда после тестов не забудьте очистить базу.
- **Безопасность (mp-security)** `JPA/Hibernate, Spring Security, PostgreSql, Redis, H2, Mapstruct, Swagger`  
  - Проверка регистрации пользователя и прав доступа по JWT.  
  - Приложена коллекция Postman тестов. Запускать на пустой базе с профилем 'test'. Можете добавить профили-маркеры: 
    test-confirmation-code-reading и test-mail-sender-bypassing для автоматического считывания кода подтверждения регистрации и для обхода ошибки, если еще не указан MAIL_SENDER, соответственно.
- **Профиль (mp-profile)** `JPA/Hibernate, Liquibase, PostgreSql, H2, Mapstruct, Swagger`
  - Сервис профиля пользователя предоставляет работу с данными: от даты регистрации до аватарки. 
  - Содержит в себе сервис `Geo` — работа с адресами.
  - Загружается предварительный набор гео-данных: все страны; регионы, районы, и города России.
  - Администрирование гео-данных: Возможность импортировать данные из CSV файла с получением отчета загрузки.
  
    <div style="margin-left: 20px;">

      Пример загружаемого файла и полученного отчета в виде так же CSV файла.  
      Для удобства добавлено отображение в табличном виде.   
      Как видно, исключены дубликаты и отсортировано последовательно по полям: `Country,Region,District,City,Street,House`.

    <details>
    <summary>Импорт:</summary>
    
      ![sample-file-to-import_geo-data.png](docs/images/sample-file-to-import_geo-data.png)

    </details>

    <details>
    <summary>Отчет:</summary>
      
      ![sample-geo-data-load-report.png](docs/images/sample-geo-data-load-report.png)

    </details>
    
    </div>

---

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

---

### Запуск

#### Для локального запуска убедитесь, что у вас установлены:
- JDK 11
- Apache Maven
- Docker (не ниже 19.03.0)
- Docker Compose (не ниже 1.27.0)

> **Примечание:** Все команды выполняйте из корня проекта.

#### 1. Соберите проект

<div style="margin-left: 20px;">

Собрать с запуском тестов перед сборкой. Должны быть установлены:
- Redis
- MongoDB
- PostgreSQL

```bash
mvn clean package
```

Собрать без запуска тестов

```bash
mvn clean package -DskipTests
```

Если вы хотите полностью пропустить компиляцию тестов и не включать их в сборку, вы можете использовать:

```bash
mvn clean package -Dmaven.test.skip=true
````

</div>

#### 2. Запустите проект (профили: `test`, `develop`, `production`)

<div style="margin-left: 20px;">

Для профиля `production` заполните соответствующие файлы с переменными окружения:
   - docker/stats/.env.stats.prod
   - docker/security/.env.security.prod
   - docker/profile/.env.profile.prod


 Для запуска каждого профиля, выполните следующие команды:  
 (включена проверка `healthcheck`, самый долгий интервал выставлен 60 сек, так что нужно подождать)

<div style="margin-left: 20px;">

<details>
<summary>Запустить с профилем `test`</summary>

Профиль `test`


  ```bash
  sudo docker-compose -f docker/docker-compose-test.yml build
  ```

  ```bash
  sudo docker-compose -f docker/docker-compose-test.yml up
  ```

[//]: # (</div>)

</details>

<details>
<summary>Запустить с профилем `develop`</summary>

Профиль `develop`

[//]: # (<div style="margin-left: 20px;">)

 ```bash  
sudo docker-compose -f docker/docker-compose-dev.yml build
 ```

 ```bash
sudo docker-compose -f docker/docker-compose-dev.yml up
 ```

[//]: # (</div>)

</details>

<details>
<summary>Запустить с профилем `production`</summary>

Профиль `production`

[//]: # (<div style="margin-left: 20px;">)

 ```bash  
sudo docker-compose -f docker/docker-compose-prod.yml build
 ```

 ```bash
sudo docker-compose -f docker/docker-compose-prod.yml up
 ```

</details>

</div>

</div>

<div style="margin-left: 20px;">

Запуск сервисов по отдельности (примеры с разными профилями)

<div style="margin-left: 20px;">

<details>
<summary>Запустить сервис `mp-stats`</summary>

Сервис `mp-stats`

<div style="margin-left: 20px;">

Профиль `test`

```bash  
sudo docker-compose -f docker/stats/docker-compose-stats-test.yml build
```

```bash 
sudo docker-compose -f docker/stats/docker-compose-stats-test.yml up
```

Профиль `develop`

```bash 
sudo docker-compose -f docker/stats/docker-compose-stats-dev.yml build
```

```bash 
sudo docker-compose -f docker/stats/docker-compose-stats-dev.yml up
```

Профиль `production`

```bash 
sudo docker-compose -f docker/stats/docker-compose-stats-prod.yml build
```

```bash 
sudo docker-compose -f docker/stats/docker-compose-stats-prod.yml up
```

</div>

</details>

<details>
<summary>Запустить сервис `mp-security`</summary>

Сервис `mp-security`

<div style="margin-left: 20px;">

Профиль `test`

```bash 
sudo docker-compose -f docker/security/docker-compose-security-test.yml build
```

```bash 
sudo docker-compose -f docker/security/docker-compose-security-test.yml up
```

Профиль `develop`

```bash 
sudo docker-compose -f docker/security/docker-compose-security-dev.yml build
```

```bash 
sudo docker-compose -f docker/security/docker-compose-security-dev.yml up
```

Профиль `production`

```bash 
sudo docker-compose -f docker/security/docker-compose-security-prod.yml build
```

```bash 
sudo docker-compose -f docker/security/docker-compose-security-prod.yml up
```

</div>

</details>

<details>
<summary>Запустить сервис `mp-profile`</summary>

Сервис `mp-profile`

<div style="margin-left: 20px;">

Профиль `test`

```bash 
sudo docker-compose -f docker/profile/docker-compose-profile-test.yml build
```

```bash 
sudo docker-compose -f docker/profile/docker-compose-profile-test.yml up
```

Профиль `develop`

```bash 
sudo docker-compose -f docker/profile/docker-compose-profile-dev.yml build
```

```bash 
sudo docker-compose -f docker/profile/docker-compose-profile-dev.yml up
```

Профиль `production`

```bash 
sudo docker-compose -f docker/profile/docker-compose-profile-prod.yml build
```

```bash 
sudo docker-compose -f docker/profile/docker-compose-profile-prod.yml up
```

</div>

</details>

</div>

</div>

