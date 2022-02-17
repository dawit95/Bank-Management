FROM openjdk:11-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
#ARG JAR_FILE=build/libs/BankApplication-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} BankApplication.jar
#ENTRYPOINT ["java","-jar","/BankApplication.jar"]
#EXPOSE 8080
