# Tomcat Application Server Docker Image
FROM eclipse-temurin:19-jdk-alpine as build

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Final Stage - Tomcat with deployed application
FROM eclipse-temurin:19-jdk-alpine

# Download and extract Tomcat
RUN apk add --no-cache curl && \
    curl -fsSL https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.50/bin/apache-tomcat-10.1.50.tar.gz | tar xz && \
    mv apache-tomcat-10.1.50 /usr/local/tomcat && \
    rm -rf /usr/local/tomcat/webapps/* && \
    apk del curl

# Copy the built WAR file to Tomcat webapps
COPY --from=build /app/target/hospital_management_system.war /usr/local/tomcat/webapps/

# Expose ports
EXPOSE 8080

# Set environment variables for databases
ENV DB_HOST=postgres \
    DB_PORT=5432 \
    DB_NAME=hospital_db \
    DB_USER=postgres \
    DB_PASSWORD=postgres \
    MONGO_URI=mongodb://mongodb:27017 \
    REDIS_HOST=redis \
    REDIS_PORT=6379

CMD ["/usr/local/tomcat/bin/catalina.sh", "run"]
