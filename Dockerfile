# Use an appropriate base image with Java 17
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/CheckMe-0.0.1-SNAPSHOT.jar app.jar

# Expose the desired port (adjust the port number if needed)
EXPOSE 8080

# Run the application when the container starts
CMD ["java", "-jar", "app.jar"]
