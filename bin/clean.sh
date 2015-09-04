#!/bin/bash

set -o errexit
set -o nounset

DIR=$(cd $(dirname "${BASH_SOURCE[0]}") && pwd)

. $DIR/paths.sh

find "$DATA_DIR" -mindepth 1 -delete
