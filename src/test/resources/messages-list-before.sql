DELETE FROM message;

INSERT INTO message(id, text, tag, user_id) VALUES
(1, 'first', 'tag-1', 1),
(2, 'second', 'tag-2', 1),
(3, 'third', 'tag-1', 1),
(4, 'fourth', 'tag-4', 2);

alter sequence message_seq restart with 10;
alter sequence usr_seq restart with 10;
