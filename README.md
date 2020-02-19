# About
An end to end spring boot application which supports following feature
## Spring data jpa integration
A simple data model is defined to support many - many mapping between user and role table.
2 JPA entities are define to support this realtionship.
#### user
#### role

an intermediate table user_role is generated by manay to many mapping to keep mapping between user id and role id. 

# Spring security
A multi login spring security is defined to support following types of authentication 
  #### Basic authentication
  #### Form based
  #### JWT token based - jwt implementation is based on following blog post
  https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world
   
# Building
    mvn clean install
    
# Running
## Maven
    mvn spring-boot:run
## Maven wrapper
### Windows
    mvnw spring-boot:run
### Linux
    ./mvnw spring-boot:run
## Java
    java -jar target/multi-login-0.0.1-SNAPSHOT.jar
    
# Usage
* Regular home page: http://localhost:8094/
* Regular login page: http://localhost:8094/login
* Test basic authentication ping service: http://localhost:8094/api/v1/ping
* Test user creation service: http://localhost:8094/swagger-ui.html
