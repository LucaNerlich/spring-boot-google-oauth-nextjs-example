# Stage 1: Build
FROM gradle:8.10.2-jdk21-alpine as build

######
### Add your ENV Vars via ARG's here
######

WORKDIR /app

# path starts in repo root
ADD ./spring .

# build .jar in /build/libs with name from settings.gradle and build.gradle
RUN gradle bootJar -Dorg.springframework.boot.devinfo=true

# Stage 2: Run
FROM eclipse-temurin:21-jdk-alpine as run

WORKDIR /app

# Create a new account and group for the application
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# copy jar to work dir root
COPY --from=build /app/build/libs/spring-boot-google-oauth-nextjs-example-0.0.1-SNAPSHOT.jar .

EXPOSE ${PORT}

CMD ["java", "-jar", "spring-boot-google-oauth-nextjs-example-0.0.1-SNAPSHOT.jar"]
