FROM maven:latest AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM eclipse-temurin:latest
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 8189
ENTRYPOINT ["java", "-jar", "/app.jar"]
