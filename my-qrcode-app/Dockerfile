FROM openjdk:20
VOLUME /temp
EXPOSE 8090
ARG JAR_FILE=target/my-qrcode-app-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} /myqrcodev1.jar
ENTRYPOINT ["java","-jar","/myqrcodev1.jar"]