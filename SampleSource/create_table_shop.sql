CREATE TABLE shop (
	id      SERIAL PRIMARY KEY,
	name    VARCHAR(64)  NOT NULL,
	address VARCHAR(128) NOT NULL,
	tel     VARCHAR(16)  NOT NULL,
	lastup TIMESTAMP default CURRENT_TIMESTAMP
);


INSERT INTO shop (name, address,tel)
VALUES 
	('Kawasaki muza', '1-2-3 Saiwai-ku Kawasaki-city Kanagawa', '044-111-2222'),
	('Yokohama Joynus', '4-5-6 Yokohama-city Kanagawa', '045-333-4444'),
	('Shibuya 109', '3-2-1 Sibuya tokyo', '03-1234-5678'),
	('Shinjuku Mitsui', '8-7-6 Nishi-Sinjuku Tokyo', '03-7890-4321');

SELECT * FROM shop;
