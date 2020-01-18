FROM openjdk:8
ADD build/libs/rocket-0.0.1-SNAPSHOT.war rocket.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","rocket.war"]
