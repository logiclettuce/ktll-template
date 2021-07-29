FROM openjdk:16-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "/app.jar"]