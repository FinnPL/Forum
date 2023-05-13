FROM gradle:jdk17 as buildstage
WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle
COPY src src
RUN ./gradlew build --no-daemon

FROM openjdk:17
COPY --from=buildstage /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]