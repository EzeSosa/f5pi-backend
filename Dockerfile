FROM openjdk:17-jdk
LABEL authors="esosa"
VOLUME /tmp
COPY build/libs/f5pi-backend-0.0.1-SNAPSHOT.jar app.jar
CMD ["sh", "-c", "java -jar app.jar"]