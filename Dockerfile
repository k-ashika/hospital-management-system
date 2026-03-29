FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY hms-backend/.mvn/ .mvn
COPY hms-backend/mvnw hms-backend/pom.xml ./
RUN ./mvnw dependency:go-offline
COPY hms-backend/src ./src
RUN ./mvnw clean package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/HospitalMS-0.0.1-SNAPSHOT.jar"]