FROM bellsoft/liberica-openjdk-alpine-musl:16
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "/app.jar"]
