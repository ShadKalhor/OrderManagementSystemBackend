# ---- Build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Copy Gradle wrapper + build scripts first (better layer caching)
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle* settings.gradle* gradle.properties* ./

# Make wrapper executable
RUN chmod +x ./gradlew

# Pre-download dependencies (speeds up rebuilds)
RUN ./gradlew --no-daemon dependencies || true

# Now copy source
COPY src ./src

# Build bootJar (Spring Boot runnable jar)
RUN ./gradlew --no-daemon clean bootJar -x test

# ---- Run stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built jar
COPY --from=build /app/build/libs/*.jar app.jar

# Render provides PORT (default 10000)
ENV PORT=10000

# Bind to 0.0.0.0 and PORT
ENTRYPOINT ["sh","-c","java -jar app.jar --server.address=0.0.0.0 --server.port=${PORT}"]
