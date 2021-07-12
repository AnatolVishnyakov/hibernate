-- Информация о подключениях
SELECT *
FROM pg_stat_activity
WHERE datname = current_database()
  and pid != pg_backend_pid();