FROM eclipse-temurin:17-jdk

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} backend-service.jar

ENTRYPOINT ["java", "-jar", "backend-service.jar"]

EXPOSE 80