DELETE FROM user_role;
DELETE FROM usr;

INSERT INTO usr(id, active, password, username) VALUES
(1, true, '$2a$08$h9IZhvtZZ3sCs4ohNXnHPeXZBfBX.AOsh9ZvwcOOdCNT6zzKH4moq', 'admin'),
(2, true, '$2a$08$h9IZhvtZZ3sCs4ohNXnHPeXZBfBX.AOsh9ZvwcOOdCNT6zzKH4moq', 'testor');

INSERT INTO user_role(user_id, roles) VALUES
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');