# Use the official OpenJDK image as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file to the container
COPY target/partner-universities-management-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java","-jar","app.jar"]
