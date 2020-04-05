FROM openjdk:8-jdk-alpine
COPY ./target/greeting-app-0.1.0.jar /usr/app/
WORKDIR /usr/app
RUN sh -c 'touch greeting-app-0.1.0-SNAPSHOT.jar'
ENTRYPOINT ["java","-jar","greeting-app-0.1.0.jar"]