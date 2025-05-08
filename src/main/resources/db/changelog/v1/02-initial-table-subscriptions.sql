CREATE TABLE IF NOT EXISTS subscriptions (
    id              INT PRIMARY KEY NOT NULL,
    title           VARCHAR (255) NOT NULL,
    description     VARCHAR (1000) NOT NULL,
    user_id       INT
    CONSTRAINT fk_subscriptions_user_id
    REFERENCES users (id),
    timestamp       TIMESTAMP
    );

COMMENT ON TABLE subscriptions IS 'Таблица подписок';
COMMENT ON COLUMN subscriptions.id IS 'id подписки';
COMMENT ON COLUMN subscriptions.title IS 'Заголовок подписки';
COMMENT ON COLUMN subscriptions.description IS 'Описание подписки';
COMMENT ON COLUMN subscriptions.user_id IS 'id пользователя подписки';
COMMENT ON COLUMN subscriptions.timestamp IS 'Время создания подписки';

CREATE SEQUENCE SUBS_SEQUENCE_ID START WITH 4;
