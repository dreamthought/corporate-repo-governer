FROM openjdk:8-jre-alpine
WORKDIR /opt/repo-name-governor
COPY ./build/libs/repo-name-governor-0.0.1-SNAPSHOT.jar repo-name-governor.jar
RUN mkdir -p /pem
COPY pem/github.pem /pem 
RUN adduser -D repo-governor
USER repo-governor
EXPOSE 8080
CMD ["java", "-jar", "repo-name-governor.jar"]

