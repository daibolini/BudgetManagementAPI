-- TABLE USERS : CREATE 
INSERT INTO users (username, password, role) VALUES ('Max', '12345678', 'admin');
INSERT INTO users (username, password, role) VALUES ('Jane', '12345678', 'admin');
INSERT INTO users (username, password, role) VALUES ('Jackson', '12345678', 'admin');
INSERT INTO users (username, password, role) VALUES ('Ken', '87654321', 'user');
INSERT INTO users (username, password, role) VALUES ('Jenny', '87654321', 'user');
INSERT INTO users (username, password, role) VALUES ('Alice', '87654321', 'user');
INSERT INTO users (username, password, role) VALUES ('Darlene', '87654321', 'user');

/*
-- Updating users' data (for testing update)

UPDATE users 
SET username = 'Johnny', password = 'Doe-Smith' , role = 'admin'
WHERE id = 1;

UPDATE users 
SET username = 'Johnny', password = 'Doe-Smith' , role = 'role'
WHERE id = 1;

*/

-- TABLE Categories : CREATE 
INSERT INTO category (description,user_id) VALUES ('income', 1);
INSERT INTO category (description,user_id) VALUES ('food', 1);
INSERT INTO category (description,user_id) VALUES ('rent', 1);
INSERT INTO category (description,user_id) VALUES ('entertaiment', 1);
INSERT INTO category (description,user_id) VALUES ('transaport', 1);
INSERT INTO category (description,user_id) VALUES ('others', 1);

INSERT INTO category (description,user_id) VALUES ('income', 2);
INSERT INTO category (description,user_id) VALUES ('food', 2);
INSERT INTO category (description,user_id) VALUES ('rent', 2);
INSERT INTO category (description,user_id) VALUES ('entertaiment', 2);
INSERT INTO category (description,user_id) VALUES ('transaport', 2);
INSERT INTO category (description,user_id) VALUES ('others', 2);
