CREATE TABLE "client"
(
    "id"       bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "name"    text NOT NULL,
    "password" text NOT NULL
);

CREATE TABLE "message"
(
    "id"      bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    "client_id" bigint NOT NULL,
    "text"    text NOT NULL

);

alter TABLE "message"
    add constraint message_user_id_fkey foreign key (client_id) references "client" (id);

INSERT INTO "client" (name, password)
VALUES ('Masha Ivanova', '$2a$12$x4NSRXAeMaGvlZ2PLTUefuRz81xqzskLoaFqPqoSBjvZ1fEbh/oQO');

INSERT INTO "message" (client_id, text) VALUES (1, 'firstMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'secondMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'thirdMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'forthMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'fifthMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'sixthMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'seventhMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'eightMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'ninthMessage');
INSERT INTO "message" (client_id, text) VALUES (1, 'tenthMessage');