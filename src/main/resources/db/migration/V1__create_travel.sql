create extension if not exists "uuid-ossp";

create table if not exists travel (
  id uuid primary key default uuid_generate_v4(),
  name varchar not null,
  destination varchar not null,
  start_at timestamptz not null,
  end_at timestamptz not null
)
