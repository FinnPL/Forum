# Forum

## Setup

- Install Java Spring (all spring dependencys in [prom.xml](../master/pom.xml))
  if you use IntelliJ every dependency should be automatically install when you clone the Project.
- Host your own mySQL and Apache server, for testing you can use [xampp](https://www.apachefriends.org/de/download.html "xampp download page")
- Acces the [mySQL admin panel](http://localhost/phpmyadmin/) and add an new Database named forum
- Enter your mySQL config in the [application.properties](../master/src/main/resources/application.properties) file.

Example config for using xampp:

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/forum
spring.datasource.username=root
spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

- [Run](../main/java/de/ghse/forum/ForumApplication.java) the project
- You can use [Postman](https://www.postman.com/downloads/ "Postman download page") to send test requests
