CREATE TABLE employees
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    first_name varchar(100),
    last_name  varchar(100),
    email      varchar(200)
);

