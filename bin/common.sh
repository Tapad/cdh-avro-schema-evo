#!/bin/bash

set -o nounset

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)
DATA_DIR=/tmp/tapad-datagen
AVRO_DIR="$DATA_DIR/avro"
PARQ_DIR="$DATA_DIR/parquet"
LOG_DIR="$DATA_DIR/log"
BUILD_DIR=$DIR/../build
SCHEMA_1_JAR="$BUILD_DIR/schema-1-datagen.jar"
SCHEMA_2_JAR="$BUILD_DIR/schema-2-datagen.jar"
SCHEMA_3_JAR="$BUILD_DIR/schema-3-datagen.jar"
SCHEMA_4_JAR="$BUILD_DIR/schema-4-datagen.jar"
SCHEMA_5_JAR="$BUILD_DIR/schema-5-datagen.jar"
CONV_JAR="$BUILD_DIR/conv.jar"

print_results() {
  local task=$1
  local path=$2
  test -e "$path/_SUCCESS" && echo "$1 succeeded" || "$1 failed - check $LOG_DIR for job output"
}
