INSERT INTO users (email, password, authorities, enabled)
VALUES
    ('radek', '$2a$10$3Igok6v2alSppZmfVwN/WuuaWtcvarQyHAnigj1NoEuG4REFA.jS.', '{ROLE_ADMIN, ROLE_USER}', true),
    ('john', '12345', '{ROLE_USER}', true),
    ('bartek', '$2a$10$.zuqWbRdJAoQqonsXd7VrOfXe//IVNfVmaM5dPt7uwpj0MBuOpSK.', '{ROLE_ADMIN, ROLE_USER}', true);