# VirtualLabs

Spring Boot Project

## Docker

Update `spring.datasource.url=jdbc:mysql://localhost:3306/virtuallabs` host to match MariaDB container
Run `bash ./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=springio/gs-spring-boot-docker` to build Docker image
Run `docker run -p 8080:8080 -t springio/gs-spring-boot-docker` to start container