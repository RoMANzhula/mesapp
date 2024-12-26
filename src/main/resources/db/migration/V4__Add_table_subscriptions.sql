CREATE TABLE user_subscriptions (
    chanel_id BIGINT  NOT NULL REFERENCES usr,
    subscriber_id BIGINT  NOT NULL REFERENCES usr,
    PRIMARY KEY (chanel_id, subscriber_id)
)