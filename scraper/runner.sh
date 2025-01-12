#!/bin/bash

# Subroutine to run the scraper
run() {
  echo "Running scraper —" $(date)
  python3 -m main
}

# Run the scraper
if [ "$1" == "once" ]; then  # Run once
  run
else  # Run every at an interval
  while true; do
    run
    sleep 20m
  done
fi
