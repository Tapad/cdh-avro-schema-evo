#!/bin/bash

set -o errexit

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/paths.sh

java -jar $SCHEMA_1_JAR "$AVRO_DIR/schema-1" 10
java -jar $SCHEMA_2_JAR "$AVRO_DIR/schema-2" 10
java -jar $SCHEMA_3_JAR "$AVRO_DIR/schema-3" 10
java -jar $SCHEMA_4_JAR "$AVRO_DIR/schema-4" 10
java -jar $SCHEMA_5_JAR "$AVRO_DIR/schema-5" 10
