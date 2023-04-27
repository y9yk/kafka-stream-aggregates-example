#!/bin/bash

JAVA_MAIN_CLASS=com.example.aggregate.App

java -cp * ${JAVA_MAIN_CLASS} \
     --parent ${PARENT_TOPIC} \
     --children ${CHILDREN_TOPIC} \
     --bootstrap_servers ${BOOTSTRAP_SERVERS}