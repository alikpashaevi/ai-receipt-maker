# Step 1: Use a lightweight JDK image
FROM openjdk:21-jdk

# Step 2: Set working directory inside the container
WORKDIR /app

# Step 3: Copy the JAR file from your local machine into the container
COPY build/libs/*SNAPSHOT.jar app.jar

# Step 4: Expose the port your Spring Boot app runs on
EXPOSE 8080

# Step 5: Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
