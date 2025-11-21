# ------------------------------------------------------
# 🏗️ STAGE 1 — Build da aplicação (Maven)
# ------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn -q dependency:go-offline

COPY src ./src

RUN mvn -q clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=builder /app/target/*.jar kanban.jar

EXPOSE 8080

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar kanban.jar"]
