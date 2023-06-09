# 1. Prepare JDK
FROM fedora:29 as jdk

RUN mkdir /tmp/jdk \
     && cd /tmp/jdk \
     && curl -O https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_linux-x64_bin.tar.gz \
     && tar -xvf openjdk-11.0.1_linux-x64_bin.tar.gz

# Run this to get list of needed modules;
# Not running it as part of the Dockerfile as it needs the app JAR as input, so it'd have to be executed each time
# jdeps --print-module-deps --class-path aggregates/target/dependencies/* aggregates/target/aggregates-0.1-SNAPSHOT.jar

RUN /tmp/jdk/jdk-11.0.1/bin/jlink \
    --add-modules java.desktop,java.management,java.naming,java.security.jgss,java.security.sasl,java.sql,jdk.unsupported \
    --verbose --strip-debug --compress 2 --no-header-files --no-man-pages \
    --output /opt/jre-minimal

# 2. Build
FROM maven:3.5-jdk-8-alpine as build

COPY pom.xml /tmp/aggregates/

# Download all Maven dependencies; unless the POM changes, this step will be cached for future builds
RUN mvn dependency:go-offline -f /tmp/aggregates/pom.xml

RUN mvn dependency:copy-dependencies -f /tmp/aggregates/pom.xml -DoutputDirectory=target/dependencies -DincludeScope=compile

COPY src /tmp/aggregates/src
RUN mvn package -DskipTests -o -f /tmp/aggregates/pom.xml

# 3. Create actual image (jlink-ed JDK, dependencies, JAR and launcher)
FROM registry.fedoraproject.org/fedora-minimal:30

COPY --from=jdk /opt/jre-minimal /opt/aggregates/jdk
RUN cd /opt/aggregates \
    && curl -sO https://raw.githubusercontent.com/fabric8io-images/run-java-sh/master/fish-pepper/run-java-sh/fp-files/run-java.sh \
    && chmod u+x run-java.sh
COPY --from=build /tmp/aggregates/target/dependencies/* /opt/aggregates/lib/
COPY run.sh /opt/aggregates
COPY --from=build /tmp/aggregates/target/aggregates.jar /opt/aggregates/lib/aggregates.jar