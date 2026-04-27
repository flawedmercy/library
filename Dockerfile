# ─────────────────────────────────────────────────────────────────────────────
# Stage 1: Build with Maven
# ─────────────────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /build

# Copy pom first for layer caching — dependencies won't re-download unless pom changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build (skip tests here; they run in CI)
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─────────────────────────────────────────────────────────────────────────────
# Stage 2: Minimal JRE runtime image
# ─────────────────────────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy only the built jar from stage 1
COPY --from=builder /build/target/*.jar app.jar

USER spring

EXPOSE 8080

# Health check via actuator
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]