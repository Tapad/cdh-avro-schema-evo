#!/bin/bash

set -o errexit

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

pushd "$DIR/.."

sbt ';clean ;compile ;assembly'

test -d build || mkdir build

SCHEMA_1_JAR='schema-1/target/scala-2.10/schema-1-datagen.jar'
SCHEMA_2_JAR='schema-2/target/scala-2.10/schema-2-datagen.jar'
SCHEMA_3_JAR='schema-3/target/scala-2.10/schema-3-datagen.jar'
SCHEMA_4_JAR='schema-4/target/scala-2.10/schema-4-datagen.jar'
SCHEMA_5_JAR='schema-5/target/scala-2.10/schema-5-datagen.jar'
CONV_JAR='conv/target/scala-2.10/conv.jar'

cp $SCHEMA_1_JAR build
cp $SCHEMA_2_JAR build
cp $SCHEMA_3_JAR build
cp $SCHEMA_4_JAR build
cp $SCHEMA_5_JAR build
cp $CONV_JAR build

cp bin/Makefile build
cp bin/paths.sh build
cp bin/datagen.sh build
cp bin/conv.sh build
cp bin/evo.sh build

popd
