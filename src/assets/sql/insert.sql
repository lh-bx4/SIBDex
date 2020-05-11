INSERT INTO type (name) VALUES 
    ('Acier'),
    ('Combat'),
    ('Dragon'),
    ('Eau'),
    ('Electik'),
    ('Fée'),
    ('Feu'),
    ('Glace'),
    ('Insecte'),
    ('Normal'),
    ('Plante'),
    ('Poison'),
    ('Psy'),
    ('Roche'),
    ('Sol'),
    ('Spectre'),
    ('Ténèbres'),
    ('Vol');
INSERT INTO attack (name, type) VALUES
    ('Vive-attaque',10),
    ('Bolle Êlek',5);
--INSERT INTO trainer VALUES ('Sasha');
INSERT INTO pokemon VALUES 
    (25,'Pikachu',0,35,5,null),
    (172,'Pichu',0,20,5,null),
    (26,'Raichu',0,60,5,null)
;
INSERT INTO evolve VALUES (172,25),(25,26);