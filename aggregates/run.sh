#!/bin/bash

export PATH="/opt/aggregates/jdk/bin:${PATH}"
export JAVA_APP_DIR=/opt/aggregates/lib
export JAVA_MAIN_CLASS=com.example.aggregates.App

exec /opt/aggregates/run-java.sh \
     --parent ${PARENT_TOPIC} \
     --children ${CHILDREN_TOPIC} \
     --bootstrap_servers ${BOOTSTRAP_SERVERS}