#!/bin/bash

CURR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source ${CURR_DIR}/common.sh

# vars
DATASOURCE=$1
PORT=$2
TYPE=$3

# validation
if [[ -z "${DATASOURCE}" || -z "${PORT}" || -z "${TYPE}" ]]; then
    echo "Error: DATASOURCE or PORT or TYPE not set"
    echo "Usage: $0 [DATASOURCE] [PORT] [TYPE]"
    exit 1
fi

# transform to lowercase
DATASOURCE=$(echo ${DATASOURCE} | tr "[:upper:]" "[:lower:]")
TYPE=$(echo ${TYPE} | tr "[:upper:]" "[:lower:]")

# run
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" http://localhost:${PORT}/connectors/ -d @config/${DATASOURCE}-${TYPE}.json