create table if not exists travel (
  id uuid,
  name varchar not null,
  destination varchar not null,
  start_at timestamp not null,
  end_at timestamp not null
)
