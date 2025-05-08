INSERT INTO users (id, email, password, role)
VALUES
    (1, 'ivanov123@mail.ru', '$2a$12$OW2W44tJkHh7Aj0zm9p1ROQWZM/Sm5rzrTl5iwV5WnEAoYhp32iha', 'ROLE_ADMIN'),
    (2, 'pet89rov@mail.ru', '$2a$12$fEkGtMISLsorcTMc2TRRd.gMK3TGtfmwawf/eGyHODXRRo.CO0BHu', 'ROLE_USER'),
    (3, 'si78dorov@mail.ru', '$2a$12$llfj2sESFa4GT2BRo5oUe.WDnYflfkqHEnRl2R6l.36dmDWGrGYly', 'ROLE_USER');

INSERT INTO subscriptions (id, title, description, user_id)
VALUES
    (1, 'Subscription 1', 'Description 1', 1),
    (2, 'Subscription 2', 'Description 2', 2),
    (3, 'Subscription 3', 'Description 3', 3);
