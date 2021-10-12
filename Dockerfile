FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/demo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/app.jar
COPY path /app/path
ENV APPLICATIONINSIGHTS_CONNECTION_STRING InstrumentationKey=3401ef54-fb0a-45b4-975d-55fc54ac900f
ENTRYPOINT ["java","-jar","-javaagent:/app/path/to/applicationinsights-agent-3.1.1.jar","/app/app.jar"]