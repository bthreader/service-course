DROP TABLE IF EXISTS bikes;
DROP TABLE IF EXISTS models;
DROP TABLE IF EXISTS groupsets;

-- Models

CREATE TABLE models (
    id serial primary key,
    name varchar(80) NOT NULL,
    model_year int NOT NULL,
    brand_name varchar(80) NOT NULL
);

INSERT INTO models (id, name, model_year, brand_name)
VALUES (0, 'Allez Sprint', 2023, 'Specialized');

-- Groupsets

DROP TYPE IF EXISTS groupset_brand;
CREATE TYPE groupset_brand AS ENUM ('SRAM', 'SHIMANO', 'CAMPAGNOLO');

CREATE TABLE groupsets (
    name varchar(80) primary key,
    brand groupset_brand NOT NULL,
    is_electronic boolean NOT NULL DEFAULT false
);

INSERT INTO groupsets (name, brand, is_electronic)
VALUES ('105 11s', 'SHIMANO', false);

-- Bikes

CREATE TABLE bikes (
    id serial primary key,
    model_id int NOT NULL references models(id),
    groupset_name varchar(80) NOT NULL references groupsets(name),
    hero_image_url TEXT,
    size varchar(20) NOT NULL
);

INSERT INTO bikes (model_id, groupset_name, size)
VALUES (0, '105 11s', '54cm');
