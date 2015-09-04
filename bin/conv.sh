#!/bin/bash

set -o nounset

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/paths.sh

MAIN=com.twitter.scalding.Tool
JOB=com.tapad.AvroParquetConverter

print_results() {
  local task=$1
  local path=$2
  test -e "$path" && echo "$1 succeeded" || "$1 failed - check $LOG_DIR for job output"
}

test -d $LOG_DIR || mkdir $LOG_DIR

echo "Converting Avro data files with schema-1 to Avro-Parquet..."
java -cp "$SCHEMA_1_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-1" --output "$PARQ_DIR/schema-1" &> "$LOG_DIR/conv-schema-1.log"
print_results "Conversion" "$PARQ_DIR/schema-1"

echo "Converting Avro data files with schema-1 to Avro-Parquet..."
java -cp "$SCHEMA_2_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-2" --output "$PARQ_DIR/schema-2" &> "$LOG_DIR/conv-schema-2.log"
print_results "Conversion" "$PARQ_DIR/schema-2"

echo "Converting Avro data files with schema-1 to Avro-Parquet..."
java -cp "$SCHEMA_3_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-3" --output "$PARQ_DIR/schema-3" &> "$LOG_DIR/conv-schema-3.log"
print_results "Conversion" "$PARQ_DIR/schema-3"

echo "Converting Avro data files with schema-1 to Avro-Parquet..."
java -cp "$SCHEMA_4_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-4" --output "$PARQ_DIR/schema-4" &> "$LOG_DIR/conv-schema-4.log"
print_results "Conversion" "$PARQ_DIR/schema-4"

echo "Converting Avro data files with schema-1 to Avro-Parquet..."
java -cp "$SCHEMA_5_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-5" --output "$PARQ_DIR/schema-5" &> "$LOG_DIR/conv-schema-5.log"
print_results "Conversion" "$PARQ_DIR/schema-5"
