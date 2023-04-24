#!/bin/bash

# env
CURR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$(dirname "${CURR_DIR}")"

# trap [error, sigquit, sigterm, sigint]
trap 'echo "[ERROR] start.connector.sh";exit 1' ERR
trap 'echo "[INTERRUPTED] received signal to stop";exit 1' SIGQUIT SIGTERM SIGINT

# utils
line_break() {
  seq -s= 100 | tr -d '[:digit:]';echo
}

try() {
  [[ $- = *e* ]]; SAVED_OPT_E=$?
  set +e
}

throw() {
  exit $1
}

catch() {
  exception_code=$?
  (( $SAVED_OPT_E )) && set +e
  return $exception_code
}