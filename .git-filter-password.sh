#!/bin/bash
# Replace datasource password placeholder in application.properties

if [ -f "backend/src/main/resources/application.properties" ]; then
  sed -i 's/${SPRING_DATASOURCE_PASSWORD}/${SPRING_DATASOURCE_PASSWORD}/g' backend/src/main/resources/application.properties
fi

if [ -f "backend/target/classes/application.properties" ]; then
  sed -i 's/${SPRING_DATASOURCE_PASSWORD}/${SPRING_DATASOURCE_PASSWORD}/g' backend/target/classes/application.properties
fi
