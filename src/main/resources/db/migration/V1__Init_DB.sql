CREATE SEQUENCE IF NOT EXISTS message_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS usr_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE message
(
    id       BIGINT NOT NULL,
    text     VARCHAR(4096),
    tag      VARCHAR(255),
    user_id  BIGINT,
    filename VARCHAR(255),
    CONSTRAINT pk_message PRIMARY KEY (id)
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    roles   VARCHAR(255)
);

CREATE TABLE usr
(
    id              BIGINT  NOT NULL,
    username        VARCHAR(255),
    password        VARCHAR(255),
    active          BOOLEAN NOT NULL,
    email           VARCHAR(255),
    activation_code VARCHAR(255),
    CONSTRAINT pk_usr PRIMARY KEY (id)
);

ALTER TABLE message
    ADD CONSTRAINT FK_MESSAGE_ON_USER FOREIGN KEY (user_id) REFERENCES usr (id);

ALTER TABLE user_role
    ADD CONSTRAINT fk_user_role_on_user FOREIGN KEY (user_id) REFERENCES usr (id);