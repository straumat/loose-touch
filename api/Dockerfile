# Loose touch API.                                                                                                                                                                                     # Blockchain2graph container.
FROM openjdk:8-jre-alpine

# Install and configure application.
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar

# Set fake AWS credentials.
ENV AWS_ACCESS_KEY_ID=useless
ENV AWS_SECRET_ACCESS_KEY=useless

# Container port configuration.
EXPOSE 8080

# Container entry point configuration.
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local-dynamodb", "/app.jar", "--create-dynamodb-tables"]
