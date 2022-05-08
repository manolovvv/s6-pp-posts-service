FROM openjdk:8-jdk-alpine

EXPOSE 8081

ADD target/posts-service-0.0.1-SNAPSHOT.jar posts-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar" , "/posts-service-0.0.1-SNAPSHOT.jar" ]