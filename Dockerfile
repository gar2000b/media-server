FROM openjdk:8
ADD /target/media-server.jar media-server.jar
EXPOSE 80
VOLUME /tmp
ENTRYPOINT ["java","","-jar","media-server.jar"]