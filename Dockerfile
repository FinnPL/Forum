FROM gradle:jdk17 as buildstage
WORKDIR /app
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle gradle
COPY src src
RUN ./gradlew build --exclude-task test -i

FROM openjdk:17
COPY --from=buildstage /app/build/libs/forum-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]