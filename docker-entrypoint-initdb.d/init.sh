#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
    CREATE USER test_traveller;
    CREATE DATABASE test_travel_db;
    GRANT ALL PRIVILEGES ON DATABASE test_travel_db TO test_traveller;
    ALTER USER test_traveller PASSWORD 'test';
EOSQL
