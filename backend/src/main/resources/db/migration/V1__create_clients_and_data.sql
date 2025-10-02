CREATE TABLE clients (
                         id BIGINT PRIMARY KEY,
                         full_name VARCHAR(255) NOT NULL,
                         display_name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         details VARCHAR(1024),
                         active BOOLEAN NOT NULL,
                         location VARCHAR(255)
);

CREATE SEQUENCE client_seq START WITH 1 INCREMENT BY 1;

INSERT INTO clients (id, full_name, display_name, email, details, active, location) VALUES
                                                                                        (nextval('client_seq'), 'John Wick', 'John W.', 'John@wick.com', 'Highly skilled assassin known for his expertise in hand-to-hand combat and strategic takedowns.', TRUE, 'New York, USA'),
                                                                                        (nextval('client_seq'), 'John Snow', 'John S.', 'John@snow.com', 'Military commander with leadership experience, skilled in battle tactics and diplomacy.', FALSE, 'The North, Westeros'),
                                                                                        (nextval('client_seq'), 'John Doe', 'John D.', 'John@doe.com', 'An unidentified individual often used as a placeholder name in legal and investigative cases.', TRUE, NULL),
                                                                                        (nextval('client_seq'), 'John Lennon', 'John L.', 'John@lennon.com', 'Renowned musician, songwriter, and peace activist, known for revolutionizing rock music.', TRUE, 'Liverpool, UK'),
                                                                                        (nextval('client_seq'), 'John Adams', 'John A.', 'John@adams.com', 'Political leader and former president with expertise in governance, law, and diplomacy.', FALSE, 'Massachusetts, USA'),
                                                                                        (nextval('client_seq'), 'Jane Austen', 'Jane A.', 'Jane@austen.com', 'Acclaimed writer specializing in novels that explore themes of love, society, and morality.', TRUE, 'Hampshire, UK'),
                                                                                        (nextval('client_seq'), 'Emma Watson', 'Emma W.', 'Emma@watson.com', 'Actress and activist known for her roles in film and her advocacy for gender equality.', TRUE, 'Paris, France'),
                                                                                        (nextval('client_seq'), 'Harry Potter', 'Harry P.', 'Harry@potter.com', 'Student with advanced knowledge in magic, specializing in defense against dark arts.', FALSE, 'Hogwarts, UK'),
                                                                                        (nextval('client_seq'), 'Bruce Wayne', 'Bruce W.', 'Bruce@wayne.com', 'Entrepreneur and CEO with expertise in technology, business management, and philanthropy.', TRUE, 'Gotham City, USA'),
                                                                                        (nextval('client_seq'), 'Clark Kent', 'Clark K.', 'Clark@kent.com', 'Investigative journalist with strong reporting skills and a dedication to uncovering the truth.', TRUE, 'Metropolis, USA'),
                                                                                        (nextval('client_seq'), 'Diana Prince', 'Diana P.', 'Diana@prince.com', 'Diplomat and ambassador with expertise in international relations and conflict resolution.', TRUE, 'Themyscira'),
                                                                                        (nextval('client_seq'), 'Peter Parker', 'Peter P.', 'Peter@parker.com', 'Talented photographer with a knack for capturing high-quality images of spiders for media and journalism.', FALSE, 'New York, USA'),
                                                                                        (nextval('client_seq'), 'Tony Stark', 'Tony S.', 'Tony@stark.com', 'Brilliant engineer and entrepreneur specializing in cutting-edge technology and robotics.', TRUE, 'Malibu, USA'),
                                                                                        (nextval('client_seq'), 'Natasha Romanoff', 'Natasha R.', 'Natasha@romanoff.com', 'Highly trained intelligence operative skilled in espionage, combat, and counter-terrorism.', TRUE, NULL),
                                                                                        (nextval('client_seq'), 'Steve Rogers', 'Steve R.', 'Steve@rogers.com', 'Elite soldier with extensive military training and experience in battlefield leadership.', FALSE, 'Brooklyn, USA');
