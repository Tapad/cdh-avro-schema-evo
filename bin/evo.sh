#!/bin/bash

set -o errexit

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/paths.sh

MAIN=com.twitter.scalding.Tool
JOB=com.tapad.AvroParquetEvolver

java -cp "$SCHEMA_2_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQUET_DATA_DIR/schema-1" --output "$PARQUET_DATA_DIR/schema-1-2"
java -cp "$SCHEMA_3_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQUET_DATA_DIR/schema-2" --output "$PARQUET_DATA_DIR/schema-2-3"
java -cp "$SCHEMA_4_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQUET_DATA_DIR/schema-3" --output "$PARQUET_DATA_DIR/schema-3-4"
java -cp "$SCHEMA_5_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQUET_DATA_DIR/schema-4" --output "$PARQUET_DATA_DIR/schema-4-5"
