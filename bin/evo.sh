#!/bin/bash

set -o nounset

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/paths.sh

MAIN=com.twitter.scalding.Tool

print_results() {
  local task=$1
  local path=$2
  test -e "$path" && echo "$1 succeeded" || "$1 failed - check $LOG_DIR for job output"
}

test -d $LOG_DIR || mkdir $LOG_DIR

# Avro evolution

JOB=com.tapad.AvroEvolver
echo "Evolving Avro data files (schema 1 to 2)..."
java -cp "$SCHEMA_2_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-1" --output "$AVRO_DIR/schema-1-2" &> "$LOG_DIR/avro-evo-schema-1-2.log"
print_results "Evolution" "$AVRO_DIR/schema-1-2"

echo "Evolving Avro data files (schema 2 to 3)..."
java -cp "$SCHEMA_3_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-2" --output "$AVRO_DIR/schema-2-3" &> "$LOG_DIR/avro-evo-schema-2-3.log"
print_results "Evolution" "$AVRO_DIR/schema-2-3"

echo "Evolving Avro data files (schema 3 to 4)..."
java -cp "$SCHEMA_4_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-3" --output "$AVRO_DIR/schema-3-4" &> "$LOG_DIR/avro-evo-schema-3-4.log"
print_results "Evolution" "$AVRO_DIR/schema-3-4"

echo "Evolving Avro data files (schema 4 to 5)..."
java -cp "$SCHEMA_5_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$AVRO_DIR/schema-4" --output "$AVRO_DIR/schema-4-5" &> "$LOG_DIR/avro-evo-schema-4-5.log"
print_results "Evolution" "$AVRO_DIR/schema-4-5"

# Avro-Parquet evolution

JOB=com.tapad.AvroParquetEvolver
echo "Evolving Parquet-Avro data files (schema 1 to 2)..."
java -cp "$SCHEMA_2_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQ_DIR/schema-1" --output "$PARQ_DIR/schema-1-2" &> "$LOG_DIR/avro-parquet-evo-schema-1-2.log"
print_results "Evolution" "$PARQ_DIR/schema-1-2"

echo "Evolving Parquet-Avro data files (schema 2 to 3)..."
java -cp "$SCHEMA_3_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQ_DIR/schema-2" --output "$PARQ_DIR/schema-2-3" &> "$LOG_DIR/avro-parquet-evo-schema-2-3.log"
print_results "Evolution" "$PARQ_DIR/schema-2-3"

echo "Evolving Parquet-Avro data files (schema 3 to 4)..."
java -cp "$SCHEMA_4_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQ_DIR/schema-3" --output "$PARQ_DIR/schema-3-4" &> "$LOG_DIR/avro-parquet-evo-schema-3-4.log"
print_results "Evolution" "$PARQ_DIR/schema-3-4"

echo "Evolving Parquet-Avro data files (schema 4 to 5)..."
java -cp "$SCHEMA_5_JAR:$CONV_JAR" $MAIN $JOB --hdfs --input "$PARQ_DIR/schema-4" --output "$PARQ_DIR/schema-4-5" &> "$LOG_DIR/avro-parquet-evo-schema-4-5.log"
print_results "Evolution" "$PARQ_DIR/schema-4-5"
