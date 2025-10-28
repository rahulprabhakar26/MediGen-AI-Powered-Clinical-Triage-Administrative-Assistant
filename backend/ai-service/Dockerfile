FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build.gradle .
COPY src ./src

RUN apt-get update && apt-get install -y gradle && gradle build -x test

EXPOSE 8083

CMD ["java", "-jar", "build/libs/ai-service-0.0.1-SNAPSHOT.jar"]
