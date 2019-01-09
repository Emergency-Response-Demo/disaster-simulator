FROM registry.access.redhat.com/rhscl/postgresql-96-rhel7:1-32

ENV PGPASSFILE=/opt/pgpass.conf

USER root
COPY opt/ /opt
RUN chown -R 10001 /opt/*
RUN chmod +x /opt/simulate-disaster.sh

USER 10001
CMD /opt/simulate-disaster.sh
