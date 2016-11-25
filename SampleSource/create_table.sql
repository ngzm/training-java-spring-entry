CREATE TABLE book (
	id     SERIAL PRIMARY KEY,
	isbn   VARCHAR(20) NOT NULL,
	name   VARCHAR(64) NOT NULL,
	price  INTEGER NOT NULL,
	lastup TIMESTAMP default CURRENT_TIMESTAMP
);


INSERT INTO book (ISBN, NAME, PRICE)
VALUES 
	('123456789abcdefgh', 'JavaScript', 1200),
	('223456789abcdefgh', 'Perfect Java', 4000),
	('323456789abcdefgh', 'C lang. Programing Lesson', 2300),
	('423456789abcdefgh', 'Spring 3 entry', 3400);


SELECT * FROM book;
