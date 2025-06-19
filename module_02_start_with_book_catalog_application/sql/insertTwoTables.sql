create table PUBLIC.BOOK
(
    BOOK_ID         BIGINT not null
        primary key,
    DESCRIPTION     CHARACTER VARYING,
    IMAGENAME       CHARACTER VARYING,
    ISBN            CHARACTER VARYING,
    PRICE           DOUBLE PRECISION,
    PUBLICATIONDATE DATE,
    TITLE           CHARACTER VARYING
);


INSERT INTO PUBLIC.BOOK (BOOK_ID, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (1, 'Generative AI tools like ChatGPT cause an immediate jaw drop for almost everyone who encounters them.', 'imageBook1.png', '978-0596152657', 35, '2024-08-01', 'Spring  AI in Action');
INSERT INTO PUBLIC.BOOK (BOOK_ID, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (2, 'Searching for bugs, detangling messy legacy code, or evaluating your codebase for new features sucks up much of a developer''s time.', 'imageBook2.png', '9781633435575', 38.5, '2024-12-01', 'Troubleshooting Java, Second Edition');
INSERT INTO PUBLIC.BOOK (BOOK_ID, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (3, 'Understanding Java from the JVM up gives you a solid foundation to grow your expertise.', 'imageBook3.png', '9781617298875', 39.19, '2022-10-01', 'The Well-Grounded Java Developer, Second Edition');
INSERT INTO PUBLIC.BOOK (BOOK_ID, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (4, 'Simplify your Java code with data-oriented programming!.', 'imageBook4.png', '9781633436930', 33.59, '2024-09-01', 'Data-Oriented Programming in Java');
INSERT INTO PUBLIC.BOOK (BOOK_ID, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (5, 'By dividing large applications into separate self-contained units, Microservices are a great step toward reducing complexity and increasing flexibility.', 'imageBook5.png', '9781617296956', 33.59, '2025-05-01', 'Spring Microservices in Action, Second Edition');

create table PUBLIC.AUTHOR
(
    ID      BIGINT not null
        primary key,
    AGE     INTEGER,
    NAME    CHARACTER VARYING,
    CITY    CHARACTER VARYING,
    STATE   CHARACTER VARYING,
    STREET  CHARACTER VARYING,
    ZIP     CHARACTER VARYING,
    BOOK_ID BIGINT,
    constraint FK_AUTHOR_BOOK_ID
        foreign key (BOOK_ID) references PUBLIC.BOOK
);


INSERT INTO PUBLIC.AUTHOR (ID, AGE, NAME, CITY, STATE, STREET, ZIP, BOOK_ID) VALUES (1, 30, 'Craig Walls', 'Sherman Oaks', 'CA', '4521 Koontz Lane', '91403', 1);
INSERT INTO PUBLIC.AUTHOR (ID, AGE, NAME, CITY, STATE, STREET, ZIP, BOOK_ID) VALUES (2, 30, 'Laurentiu Spilca', 'Green Bay', 'WI', '4071 Sycamore Lake Road', '54303', 2);
INSERT INTO PUBLIC.AUTHOR (ID, AGE, NAME, CITY, STATE, STREET, ZIP, BOOK_ID) VALUES (3, 30, 'Benjamin Evans', 'Woonsocket', 'RI', '1675 Bond Street', '02895', 3);
INSERT INTO PUBLIC.AUTHOR (ID, AGE, NAME, CITY, STATE, STREET, ZIP, BOOK_ID) VALUES (4, 30, 'Chris Kiehl', 'Novato', 'CA', '4336 Larry Street', '94947', 4);
INSERT INTO PUBLIC.AUTHOR (ID, AGE, NAME, CITY, STATE, STREET, ZIP, BOOK_ID) VALUES (5, 30, 'John Carnell', 'Donaldson', 'MN', '1288 Ferrell Street', '56720', 5);