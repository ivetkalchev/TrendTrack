FROM gradle:8.10-jdk17

WORKDIR /opt/app

COPY ./build/libs/trendtrack-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar trendtrack-0.0.1-SNAPSHOT.jar"]