FROM amazoncorretto:11-alpine-jdk

COPY target/extractor-0.0.1-SNAPSHOT.jar /tmp

EXPOSE 8099
CMD ["/bin/bash"]

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /tmp/extractor-0.0.1-SNAPSHOT.jar" ]
