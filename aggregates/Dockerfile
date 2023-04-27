# build
FROM maven:3-openjdk-8-slim AS BUILD
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean package -DskipTests

# runtime
FROM openjdk:8-alpine AS RUNTIME
RUN apk update && apk add --no-cache libc6-compat bash curl
    
RUN ln -s /lib64/ld-linux-x86-64.so.2 /lib/ld-linux-x86-64.so.2
WORKDIR /app/
COPY --from=BUILD /tmp/target/wiadrodanych-kafka-streams-*-jar-with-dependencies.jar .
COPY run.sh .