FROM gradle:jdk17 as buildstage
WORKDIR /app
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
COPY gradle gradle
COPY src src
RUN ./gradlew build --exclude-task test -i

FROM openjdk:17
COPY --from=buildstage /app/build/libs/*.jar /app/
RUN find /app -type f ! -name '*-plain.jar' -exec mv {} /app/app.jar ;
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]