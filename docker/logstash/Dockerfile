ARG ELK_VERSION

FROM docker.elastic.co/logstash/logstash:${ELK_VERSION}
LABEL maintainer="wissensalt@gmail.com"

RUN /usr/share/logstash/bin/logstash-plugin install logstash-output-jdbc

COPY logstash.yml /usr/share/logstash/config/logstash.yml
COPY logstash.conf /usr/share/logstash/pipeline/logstash.conf
COPY mysql-connector-java-8.0.27.jar /usr/share/logstash/logstash-core/lib/jars/mysql.jar