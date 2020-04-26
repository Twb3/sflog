#!/usr/bin/env bash
mkdir -p build/output/
mvn clean package shade:shade
cat stub.sh target/sflog-0.0.1.jar > build/output/sflog && chmod +x build/output/sflog
