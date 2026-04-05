#!/bin/bash
# Replace Aiven password in application.properties

if [ -f "backend/src/main/resources/application.properties" ]; then
  sed -i 's/${DB_PASSWORD}/${DB_PASSWORD}/g' backend/src/main/resources/application.properties
fi

if [ -f "backend/target/classes/application.properties" ]; then
  sed -i 's/${DB_PASSWORD}/${DB_PASSWORD}/g' backend/target/classes/application.properties  
fi
