FROM adoptopenjdk/maven-openjdk8

WORKDIR app/

COPY . .

RUN mvn clean package

ENV clientId ""
ENV clientSecret ""

ENTRYPOINT ["java","-jar", "/app/target/ifoodbackend-0.0.1-SNAPSHOT.jar"]