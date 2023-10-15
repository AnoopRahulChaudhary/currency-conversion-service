FROM eclipse-temurin:17
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} currency-conversion-service.jar
ENTRYPOINT ["java","-jar","/currency-conversion-service.jar"]