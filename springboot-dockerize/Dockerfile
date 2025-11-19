FROM amazoncorretto:17.0.7-alpine

EXPOSE 8181

ADD target/dockerized-spring-boot.jar  dockerized-spring-boot.jar

ENTRYPOINT ["java","-jar","/dockerized-spring-boot.jar"]