FROM openjdk:11

COPY target/userhistory-0.0.1-SNAPSHOT.jar userhistory.jar

EXPOSE 8080

CMD java $JAVA_OPTS -jar userhistory.jar