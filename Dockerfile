FROM openjdk:17

LABEL mentainer="fotismanolas@outlook.com"

WORKDIR /app

COPY target/Universe-1.0-SNAPSHOT.jar /app/Universe.jar

ENTRYPOINT ["java", "-jar", "Universe.jar"]