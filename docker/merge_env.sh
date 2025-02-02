#!/bin/sh

echo 'Starting to combine .env files...';

echo ' Existing check:';
[ -f security/.env ] && printf ' \033[32m ✓ \033[0m security/.env\n' || printf ' \033[31m ✗ \033[0m security/.env\n';
[ -f profile/.env ] && printf ' \033[32m ✓ \033[0m profile/.env\n' || printf ' \033[31m ✗ \033[0m profile/.env\n';
[ -f stats/.env ] && printf ' \033[32m ✓ \033[0m stats/.env\n' || printf ' \033[31m ✗ \033[0m stats/.env\n';

cat security/.env profile/.env stats/.env > .env;

echo 'Combining completed';
