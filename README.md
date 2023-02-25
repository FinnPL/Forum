# Forum

## Setup
 
Update & upgrade
```
sudo apt-get update
sudo apt-get upgrade
```

Install Java 19
```
wget https://download.oracle.com/java/19/latest/jdk-19_linux-x64_bin.deb
sudo apt-get -qqy install ./jdk-19_linux-x64_bin.deb
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-19/bin/java 1919
```
Install Maven
```
sudo apt install maven
```
Clone projekt
```
git clone https://github.com/FinnPL/Forum
```
Install and start MySQL-server
```
sudo apt install mysql-server
sudo systemctl start mysql.service
```
Open my SQL
```
sudo mysql
```
Type the following into the mySQL console.
Replace password with your own password.
Create Forum Database
```
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'YOUR_OWN_PASSWORD_HERE';
FLUSH PRIVILEGES;
SELECT user,authentication_string,plugin,host FROM mysql.user;
CREATE DATABASE forum;   
exit
```
Continue in Linux Console and enter your chosen mySQL password into application.properties
```
nano Forum/src/main/resources/application.properties
```

```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/forum
spring.datasource.username=root
spring.datasource.password= YOUR_OWN_PASSWORD_HERE

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Build
```
cd Forum/
mvn package
```
Open Port 8080
```
sudo ufw allow 8080
```

Run Forum.jar
```
cd target/
sudo java -jar Forum-0.0.1-SNAPSHOT.jar
```

## Docker Setup

Build 
```
mvn package
```
Build Docker Image
```
sudo docker build -t forum .
```
Run Docker Image
```
 sudo docker run -p 8080:8080 --network="host" forum
```
