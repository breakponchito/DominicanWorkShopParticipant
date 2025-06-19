create table PUBLIC.BOOK
(
    ID              BIGINT not null
        primary key,
    AUTHOR          CHARACTER VARYING,
    DESCRIPTION     CHARACTER VARYING,
    IMAGENAME       CHARACTER VARYING,
    ISBN            CHARACTER VARYING,
    PRICE           DOUBLE PRECISION,
    PUBLICATIONDATE DATE,
    TITLE           CHARACTER VARYING
);


INSERT INTO PUBLIC.BOOK (ID, AUTHOR, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (1, 'Craig Walls', 'Generative AI tools like ChatGPT cause an immediate jaw drop for almost everyone who encounters them.', 'imageBook1.png', '978-0596152657', 35, '2024-08-01', 'Spring  AI in Action');
INSERT INTO PUBLIC.BOOK (ID, AUTHOR, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (2, 'Laurentiu Spilca', 'Searching for bugs, detangling messy legacy code, or evaluating your codebase for new features sucks up much of a developer''s time.', 'imageBook2.png', '9781633435575', 38.5, '2024-12-01', 'Troubleshooting Java, Second Edition');
INSERT INTO PUBLIC.BOOK (ID, AUTHOR, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (3, 'Benjamin Evans', 'Understanding Java from the JVM up gives you a solid foundation to grow your expertise.', 'imageBook3.png', '9781617298875', 39.19, '2022-10-01', 'The Well-Grounded Java Developer, Second Edition');
INSERT INTO PUBLIC.BOOK (ID, AUTHOR, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (4, 'Chris Kiehl', 'Simplify your Java code with data-oriented programming!.', 'imageBook4.png', '9781633436930', 33.59, '2024-09-01', 'Data-Oriented Programming in Java');
INSERT INTO PUBLIC.BOOK (ID, AUTHOR, DESCRIPTION, IMAGENAME, ISBN, PRICE, PUBLICATIONDATE, TITLE) VALUES (5, 'John Carnell', 'By dividing large applications into separate self-contained units, Microservices are a great step toward reducing complexity and increasing flexibility.', 'imageBook5.png', '9781617296956', 33.59, '2025-05-01', 'Spring Microservices in Action, Second Edition');