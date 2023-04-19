FROM openjdk:21-ea-17-slim-bullseye as buildstage
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN ./mvnw package -DskipTests

FROM openjdk:21-ea-17-slim-bullseye
COPY --from=buildstage app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]