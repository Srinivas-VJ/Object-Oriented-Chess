# temp build image

FROM openjdk
ADD build/libs/chess-0.0.1-SNAPSHOT.jar chess-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/chess-0.0.1-SNAPSHOT.jar"]