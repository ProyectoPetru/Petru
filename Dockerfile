FROM maven:3.1.6 AS build
COPY /petru .
RUN mvn clean package -DskipTests

FROM openjdk:17-slim
COPY --from=build /petru/target/petru-0.0.1-SNAPSHOT.jar petru.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "petru.jar"]