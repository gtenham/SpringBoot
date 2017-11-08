FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/jersey-starter-app-1.0.0-SNAPSHOT.jar jersey-starter-app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/jersey-starter-app.jar"]