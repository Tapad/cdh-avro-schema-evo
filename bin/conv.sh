#!/bin/bash

set -o errexit

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/paths.sh

MAIN=com.twitter.scalding.Tool
JOB=com.tapad.AvroParquetConverter

java -cp "$SCHEMA_1_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-1" --output "$PARQ_DIR/schema-1"
java -cp "$SCHEMA_2_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-2" --output "$PARQ_DIR/schema-2"
java -cp "$SCHEMA_3_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-3" --output "$PARQ_DIR/schema-3"
java -cp "$SCHEMA_4_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-4" --output "$PARQ_DIR/schema-4"
java -cp "$SCHEMA_5_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-5" --output "$PARQ_DIR/schema-5"
