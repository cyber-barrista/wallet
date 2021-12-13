#!/bin/bash

set -e

# TODO Refactor once proper CI is introduced
cd ..
./gradlew clean
./gradlew db:generateDbApiCode
./gradlew shadowJar
cd deploy

cp ../rest/build/libs/rest*.jar ./

docker build . --tag wallet
