INSERT INTO users (email, password, authorities, enabled)
VALUES
    ('radek', '$2a$10$iweDsmkbunbIQZ8zgGoa..s7R7j2j3nGSereRswOpP9dF7SGygmP6', '{ROLE_ADMIN, ROLE_USER}', true),
    ('john', '12345', '{ROLE_USER}', true);