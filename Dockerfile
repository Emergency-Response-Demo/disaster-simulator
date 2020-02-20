FROM quay.io/btison/openjdk18-openshift:1.6

ENV JAVA_APP_DIR=/deployments

EXPOSE 8080 8443

COPY target/disaster-simulator-1.0.0.jar /deployments/
