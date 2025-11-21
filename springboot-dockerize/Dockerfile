FROM openjdk:17
EXPOSE:8181
ADD target/dockerized-spring-boot.jar  dockerized-spring-boot
ENTRYPOINT ["java","-jar","dockerized-spring-boot.jar"]