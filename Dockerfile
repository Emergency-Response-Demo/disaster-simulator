FROM registry.access.redhat.com/rhscl/postgresql-96-rhel7:1-32

USER root
COPY simulate-disaster.sh /opt
RUN chmod +x /opt/simulate-disaster.sh && chown 10001 /opt/simulate-disaster.sh

USER 10001
CMD /opt/simulate-disaster.sh
