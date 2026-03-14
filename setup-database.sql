-- PostgreSQL Database Setup for 500K+ Concurrent Users
-- SmartFarm Aquaculture Management System

-- Create database
CREATE DATABASE smartfarm_aquaculture 
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Connect to the database
\c smartfarm_aquaculture;

-- Create optimized user for the application
CREATE USER aquaculture_user WITH 
    PASSWORD 'Aquaculture@2026!Secure'
    NOSUPERUSER
    NOCREATEDB
    NOCREATEROLE;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE smartfarm_aquaculture TO aquaculture_user;
GRANT ALL ON SCHEMA public TO aquaculture_user;

-- Set default privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO aquaculture_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO aquaculture_user;

-- Create extensions for performance
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pg_stat_statements";
CREATE EXTENSION IF NOT EXISTS "pg_trgm";

-- Performance optimizations for 500K users
ALTER SYSTEM SET max_connections = 1000;
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;
ALTER SYSTEM SET random_page_cost = 1.1;
ALTER SYSTEM SET effective_io_concurrency = 200;

-- Reload PostgreSQL configuration
SELECT pg_reload_conf();

-- Create optimized indexes for high performance
-- These indexes will be created when the application starts

-- Connection pool configuration for 500K users
-- Recommended: HikariCP with 20-200 connections per instance
-- Multiple application instances for horizontal scaling

COMMIT;
