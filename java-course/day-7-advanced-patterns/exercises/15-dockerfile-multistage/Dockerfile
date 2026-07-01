# Stage 1: Build — full JDK + Gradle
FROM gradle:8.5-jdk21 AS builder
WORKDIR /home/gradle/app
COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src
RUN gradle bootJar --no-daemon

# Stage 2: Run — slim JRE only (~200MB vs ~800MB)
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
USER app
WORKDIR /app
COPY --from=builder /home/gradle/app/build/libs/*.jar app.jar
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
