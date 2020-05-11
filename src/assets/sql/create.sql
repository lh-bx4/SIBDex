CREATE DATABASE pokemon;
\c pokemon
CREATE SCHEMA chen;
SET search_path to chen;
CREATE TABLE type (
    id SERIAL,
    name VARCHAR(30),
    CONSTRAINT pk_type PRIMARY KEY(id) 
);
CREATE TABLE attack(
    id SERIAL,
    name VARCHAR(30),
    type INT,
    CONSTRAINT pk_attack PRIMARY KEY(id),
    CONSTRAINT fk_attack_type FOREIGN KEY(type) REFERENCES type(id)
);
CREATE TABLE trainer(
    id SERIAL,
    name VARCHAR(30),
    CONSTRAINT pk_trainer PRIMARY KEY(id)
);
CREATE TABLE pokemon(
    id INT,
    name VARCHAR(20),
    level INT,
    health INT,
    type1 INT,
    type2 INT,
    trainer INT,
    CONSTRAINT pk_pokemon PRIMARY KEY(id),
    CONSTRAINT fk_pokemon_type1 FOREIGN KEY(type1) REFERENCES type(id),
    CONSTRAINT fk_pokemon_type2 FOREIGN KEY(type2) REFERENCES type(id),
    CONSTRAINT fk_pokemon_trainer FOREIGN KEY(trainer) REFERENCES trainer(id)
);
CREATE TABLE evolve(
    down INT,
    up INT,
    CONSTRAINT pk_evolve PRIMARY KEY(down,up),
    CONSTRAINT fk_evolve_down FOREIGN KEY(down) REFERENCES pokemon(id),
    CONSTRAINT fk_evolve_up FOREIGN KEY(up) REFERENCES pokemon(id)
);
\dt

/*DROP TABLE player;
DROP SCHEMA player1;
DELETE FROM  Club WHERE 
*/
