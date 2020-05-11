SELECT * FROM attack a JOIN type t ON t.id=a.type;
SELECT * FROM trainer;
SELECT * FROM type;
SELECT * FROM pokemon;
SELECT pd.name,pu.name FROM evolve e JOIN pokemon pd ON e.down=pd.id JOIN pokemon pu ON e.up=pu.id;