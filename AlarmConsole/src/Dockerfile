FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY . .
RUN javac --release 17 *.java && \
    jar cfe app.jar Main *.class

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=builder /app/app.jar .

CMD ["java", "-jar", "app.jar"]
