#!/bin/sh
#
# Author: pavbatol
# Created: February 07, 2025
# Version: 1.0.0
# License: MIT
#
# Description: This script simplifies docker-compose management for the MyPlace project.
#              It supports the following environments: prod, dev, test.
#              Available actions: up, down, build, logs.
#              Optionally, you can specify a Docker Compose profile and the detached mode flag

#-- Script settings
ENV_MERGER_SERVICE="env-merger"  # Service that merges all .env files (the container will be removed after execution)

#-- Functions
show_help() {
  echo "Usage: $0 <ENVIRONMENT> <ACTION> [COMPOSE_PROFILE] [-d]"
  echo
  echo "Help options:"
  echo "  -h, --help    Show this help message and exit"
  echo
  echo "Arguments:"
  echo "  <ENVIRONMENT>       The environment to use. Must be one of: prod, dev, test."
  echo "  <ACTION>            The action to perform. Must be one of: up, down, build, logs."
  echo "  [COMPOSE_PROFILE]   Optional: The Docker Compose profile to use (e.g., security, stats, profile, env-merger)."
  echo "  [-d]                Optional: Run in detached mode (only for 'up' action)."
  echo
  echo "Examples:"
  echo "  $0 prod up           # Start production environment."
  echo "  $0 dev up security   # Start development environment with 'security' profile."
  echo "  $0 prod up -d        # Start production environment in detached mode."
  echo "  $0 prod down         # Stop and remove production environment."
  echo "  $0 prod logs         # View logs for production environment."
  echo "  $0 test build        # Build services for the test environment."
  echo
  echo "Note:"
  echo "  - The '-d' flag is only valid with the 'up' action."
  echo "  - [COMPOSE_PROFILE] is ignored for 'down' and 'logs' actions."
  echo "  - If [COMPOSE_PROFILE] is not provided, the default profile 'full' will be used."
}
usage() {
  show_help
  exit 1
}
check_for_help() {
  if [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
      show_help
      exit 0
  fi
}
check_args_count() {
  # Expected: check_args_count ENV ACTION
  [ $# -lt 2 ] && printf "\033[31m !\033[0m Error: There are few incoming arguments.\n\n" && usage
}
check_args() {
  ENV=$1
  ACTION=$2
  COMPOSE_PROFILE=$3
  DETACH_MODE=$4

  if [ "$ENV" != "test" ] && [ "$ENV" != "dev" ] && [ "$ENV" != "prod" ]; then
      printf "\033[31m !\033[0m Error: <ENVIRONMENT> value is not correct: $ENV.\n\n"
      usage
  fi
  if [ "$ACTION" = "build" ] && [ -n "$DETACH_MODE" ]; then
      printf "\033[31m !\033[0m Error: Don't use [-d] with 'build' action.\n\n"
      usage
  fi
  if [ "$ACTION" = "down" ] && { [ -n "$DETACH_MODE" ] || [ -n "$COMPOSE_PROFILE" ]; }; then
      printf "\033[31m !\033[0m Error: Don't use [-d] or [COMPOSE_PROFILE] with 'down' action.\n\n"
      usage
  fi
}
up_then_remove_env_helper_container() {
  COMPOSE_FILE=$1
  WITH_COMPOSE_PROFILE=$2
  DETACH_MODE=$3

  printf "\033[1;33m💡  NOTE: Unset environment variables before the 'env-merger' service starts are expected and should not cause concern.\033[0m\n\n"

  sudo docker-compose -f $COMPOSE_FILE up --no-start --no-recreate "$ENV_MERGER_SERVICE"
  sudo docker-compose -f $COMPOSE_FILE start "$ENV_MERGER_SERVICE"
  sudo docker-compose -f $COMPOSE_FILE logs --follow "$ENV_MERGER_SERVICE" &
  LOG_PID=$!
  wait $LOG_PID

  sudo docker-compose -f $COMPOSE_FILE rm -f "$ENV_MERGER_SERVICE"

  sudo docker-compose -f $COMPOSE_FILE $WITH_COMPOSE_PROFILE up $DETACH_MODE --scale "$ENV_MERGER_SERVICE=0"
}

#-- Execute
check_for_help "$1"
check_args_count "$@"

ENV=$1              # Environment (prod, dev, test)
ACTION=$2           # ACTION (build, up, down, logs)
COMPOSE_PROFILE=""  # Optional: docker-compose profile (example: security)
DETACH_MODE=""      # Optional: -d for detached mode

shift 2

while [ $# -gt 0 ]; do
  case $1 in
    -d)
      DETACH_MODE="-d"
      ;;
    *)
      if [ -z "$COMPOSE_PROFILE" ]; then
        COMPOSE_PROFILE=$1
      else
        echo "Error: Too many arguments."
        usage
      fi
      ;;
  esac
  shift
done

check_args "$ENV" "$ACTION" "$COMPOSE_PROFILE" "$DETACH_MODE"

echo "Environment: $ENV, action: $ACTION, compose profile: ${COMPOSE_PROFILE:-not specified}, detach flag: ${DETACH_MODE:-not specified}"
echo

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
COMPOSE_FILE="$SCRIPT_DIR/docker-compose-$ENV.yml"
if [ ! -f "$COMPOSE_FILE" ]; then
  echo "File not found: $COMPOSE_FILE"
  exit 1
fi

WITH_COMPOSE_PROFILE="--profile full"
if [ -n "$COMPOSE_PROFILE" ]; then
  WITH_COMPOSE_PROFILE="--profile $COMPOSE_PROFILE"
fi

case $ACTION in
  build)
    echo "COMMAND: sudo docker-compose -f $COMPOSE_FILE $WITH_COMPOSE_PROFILE build"
    echo
    sudo docker-compose -f $COMPOSE_FILE $WITH_COMPOSE_PROFILE build
    ;;
  up)
    DOCKER_COMPOSE_CMD="sudo docker-compose -f $COMPOSE_FILE $WITH_COMPOSE_PROFILE up $DETACH_MODE"
    echo "EXPLAIN"
    if [ "$ENV" = "prod" ]; then
      echo "COMMAND: sudo docker-compose -f $COMPOSE_FILE up $ENV_MERGER_SERVICE"
      echo "COMMAND: sudo docker-compose -f $COMPOSE_FILE rm -f $ENV_MERGER_SERVICE"
      echo "COMMAND: $DOCKER_COMPOSE_CMD"
      echo
      up_then_remove_env_helper_container "$COMPOSE_FILE" "$WITH_COMPOSE_PROFILE" "$DETACH_MODE"
    else
      echo "COMMAND: $DOCKER_COMPOSE_CMD"
      echo
      sudo docker-compose -f $COMPOSE_FILE $WITH_COMPOSE_PROFILE up $DETACH_MODE
    fi
    ;;
  down)
    echo "COMMAND: sudo docker-compose -f $COMPOSE_FILE down"
    echo
    sudo docker-compose -f $COMPOSE_FILE down
    ;;
  logs)
    echo "COMMAND: sudo docker-compose -f $COMPOSE_FILE logs"
    echo
    sudo docker-compose -f $COMPOSE_FILE logs
    ;;
  *)
    printf "\033[31m !\033[0m Error: Unknown action: $ACTION.\n\n"
    usage
    ;;
esac
