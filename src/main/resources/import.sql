INSERT INTO auditorium (id, name, seatsNumber, vipSeats) VALUES (1, 'Blue hall', 500, '25,26,27,28,29,30,31,32,33,34,35');
INSERT INTO auditorium (id, name, seatsNumber, vipSeats) VALUES (2, 'Red hall', 800, '25,26,27,28,29,30,31,32,33,34,35,75,76,77,78,79,80,81,82,83,84,85');
INSERT INTO auditorium (id, name, seatsNumber, vipSeats) VALUES (3, 'Yellow hall', 1000, '25,26,27,28,29,30,31,32,33,34,35,75,76,77,78,79,80,81,82,83,84,85,105,106,107,108,109,110,111,112,113,114,115');

INSERT INTO user (id, email, name, birthday, password, roles) VALUES (1, 'john@gmail.com', 'John', '1980-06-20','$2a$10$VFF2tbgBsyfM/LnaB9G2X.EDzNrvBDpaKo4.cjnTjR3LWFyqHQ7k.', 'BOOKING_MANAGER');
INSERT INTO user (id, email, name, birthday, password, roles) VALUES (2, 'mary@gmail.com', 'Mary', '1990-11-30', '$2a$10$ayxQuNxfnuNEDCikH1LimORrJ7BXl4GmPOYHKli6OWoiz1kza.ELq', 'REGISTERED_USER');

INSERT INTO account (ID, user_id, amount) VALUES (1, 1, 15000);
INSERT INTO account (ID, user_id, amount) VALUES (2, 2, 7000);

INSERT INTO event (id, name, rate, basePrice, dateTime, auditorium_id) VALUES (1, 'New Year Party', 'HIGH', 1500.5, '2019-12-31 23:30:00', 1);
INSERT INTO event (id, name, rate, basePrice, dateTime, auditorium_id) VALUES (2, 'Summer Party', 'MID', 500, '2018-06-01 10:30:00', 2);

INSERT INTO ticket (id, event_id, seats, price) VALUES (1, 1, '1,2,3', 3000.5);
INSERT INTO ticket (id, event_id, seats, price) VALUES (2, 1, '13,14', 2000);

INSERT INTO booking (id, user_id, ticket_id) VALUES (1, 1, 1);
INSERT INTO booking (id, user_id, ticket_id) VALUES (2, 1, 2);