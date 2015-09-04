#!/bin/bash

set -o errexit

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/common.sh

NUM_RECORDS=10

java -jar $SCHEMA_1_JAR "$AVRO_DIR/schema-1" $NUM_RECORDS
java -jar $SCHEMA_2_JAR "$AVRO_DIR/schema-2" $NUM_RECORDS
java -jar $SCHEMA_3_JAR "$AVRO_DIR/schema-3" $NUM_RECORDS
java -jar $SCHEMA_4_JAR "$AVRO_DIR/schema-4" $NUM_RECORDS
java -jar $SCHEMA_5_JAR "$AVRO_DIR/schema-5" $NUM_RECORDS
