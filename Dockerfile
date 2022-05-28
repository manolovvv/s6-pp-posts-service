FROM openjdk:11

EXPOSE 8082

ADD target/posts-service-0.0.1-SNAPSHOT.jar posts-service-0.0.1-SNAPSHOT.jar

ENTRYPOINT [ "java", "-jar" , "/posts-service-0.0.1-SNAPSHOT.jar" ]