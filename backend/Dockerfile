FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY pom.xml .

COPY src ./src

RUN apt-get update && apt-get install -y maven && \
    mvn clean package -DskipTests

ENTRYPOINT ["java", "-jar", "target/voting-system-0.0.1-SNAPSHOT.jar"]