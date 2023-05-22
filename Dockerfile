FROM gradle:7.6.1-jdk11-corretto

COPY . .

EXPOSE 8080

RUN gradle build

ENTRYPOINT ["java", "-jar", "build/libs/homebanking-0.0.1-SNAPSHOT-plain.jar"]
