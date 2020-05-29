/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sibdex;

/**
 *
 * @author Windows
 */
public class Pokemon {
protected int id,against_bug,against_dark,against_dragon,against_electric,against_fairy,against_fight,against_fire,against_flying,against_ghost,against_grass,against_ground,against_ice,against_normal,against_poison,against_psychic,against_rock,against_steel,against_water,attack,defense,speattack,spedefense,experience,height,speed,weight,health,gen;
protected String type1,type2,name;

public void setId(int id){
this.id = id;
}
public void setAtk(int atk){
this.attack = atk;
}

public void setAgainst_bug(int against_bug){
this.against_bug = against_bug;
}
public void setAgainst_dark(int against_dark){
this.against_dark = against_dark;
}
public void setAgainst_dragon(int against_dragon){
this.against_dragon = against_dragon;
}
public void setAgainst_electric(int against_electric){
this.against_electric = against_electric;
}
public void setAgainst_fairy(int against_fairy){
this.against_fairy = against_fairy;
}
public void setAgainst_fight(int against_fight){
this.against_fight = against_fight;
}
public void setAgainst_fire(int against_fire){
this.against_fire = against_fire;
}
public void setAgainst_flight(int against_flying){
this.against_flying = against_flying;
}
public void setAgainst_ghost(int against_ghost){
this.against_ghost = against_ghost;
}
public void setAgainst_grass(int against_grass){
this.against_grass = against_grass;
}
public void setAgainst_ground(int against_ground){
this.against_ground = against_ground;
}
public void setAgainst_ice(int against_ice){
this.against_ice = against_ice;
}
public void setAgainst_normal(int against_normal){
this.against_normal = against_normal;
}
public void setAgainst_poison(int against_poison){
this.against_poison = against_poison;
}
public void setAgainst_psychic(int against_psychic){
this.against_psychic = against_psychic;
}
public void setAgainst_rock(int against_rock){
this.against_rock = against_rock;
}
public void setAgainst_steel(int against_steel){
this.against_steel = against_steel;
}
public void setAgainst_water(int against_water){
this.against_water = against_water;
}

protected String resistances(){
String resistances = "";
if(this.against_bug < 1){
    resistances += " BUG : "+this.against_bug; 
} else if(this.against_dark < 1){
    resistances += " DARK : "+this.against_dark;
} else if(this.against_dragon < 1){
    resistances += " DRAGON : "+this.against_dragon;
} else if(this.against_electric < 1){
    resistances += " ELECTRIC : "+this.against_electric; 
} else if(this.against_fairy < 1){
    resistances += " FAIRY : "+this.against_fairy;
} else if(this.against_fight < 1){
    resistances += " FIGHT : "+this.against_fight;
} else if(this.against_flying < 1){
    resistances += " FLYING : "+this.against_flying;
} else if(this.against_fire < 1){
    resistances += " FIRE : "+this.against_fire;
} else if(this.against_ghost < 1){
    resistances += " GHOST : "+this.against_ghost;
} else if(this.against_grass < 1){
    resistances += " GRASS : "+this.against_grass;
} else if(this.against_ground < 1){
    resistances += " GROUND : "+this.against_ground;
} else if(this.against_ice < 1){
    resistances += " ICE : "+this.against_ice;
} else if(this.against_normal < 1){
    resistances += " NORMAL : "+this.against_normal;
} else if(this.against_poison < 1){
    resistances += " POISON : "+this.against_poison;
} else if(this.against_psychic < 1){
    resistances += " PSYCHIC : "+this.against_psychic;
} else if(this.against_rock < 1){
    resistances += " ROCK : "+this.against_rock;
} else if(this.against_steel < 1){
    resistances += " STEEL : "+this.against_steel;
} else if(this.against_water < 1){
    resistances += " WATER : "+this.against_water;
}
return(resistances);
}

protected String weaknesses(){
String weaknesses = "";
if(this.against_bug > 1){
    weaknesses += " BUG : "+this.against_bug; 
} else if(this.against_dark > 1){
    weaknesses += " DARK : "+this.against_dark;
} else if(this.against_dragon > 1){
    weaknesses += " DRAGON : "+this.against_dragon;
} else if(this.against_electric > 1){
    weaknesses += " ELECTRIC : "+this.against_electric; 
} else if(this.against_fairy > 1){
    weaknesses += " FAIRY : "+this.against_fairy;
} else if(this.against_fight > 1){
    weaknesses += " FIGHT : "+this.against_fight;
} else if(this.against_flying > 1){
    weaknesses += " FLYING : "+this.against_flying;
} else if(this.against_fire > 1){
    weaknesses += " FIRE : "+this.against_fire;
} else if(this.against_ghost > 1){
    weaknesses += " GHOST : "+this.against_ghost;
} else if(this.against_grass > 1){
    weaknesses += " GRASS : "+this.against_grass;
} else if(this.against_ground > 1){
    weaknesses += " GROUND : "+this.against_ground;
} else if(this.against_ice > 1){
    weaknesses += " ICE : "+this.against_ice;
} else if(this.against_normal > 1){
    weaknesses += " NORMAL : "+this.against_normal;
} else if(this.against_poison > 1){
    weaknesses += " POISON : "+this.against_poison;
} else if(this.against_psychic > 1){
    weaknesses += " PSYCHIC : "+this.against_psychic;
} else if(this.against_rock > 1){
    weaknesses += " ROCK : "+this.against_rock;
} else if(this.against_steel > 1){
    weaknesses += " STEEL : "+this.against_steel;
} else if(this.against_water > 1){
    weaknesses += " WATER : "+this.against_water;
}
return(weaknesses);
}

public String toString(){
    String pokemon = name + " | ID : "+id+" | GEN :"+gen+"\n"+
           "TYPE1 : "+type1+" | TYPE2 : "+type2+"\n"+
           "STATS : ATK = "+attack+" | DEF = "+defense+" | SPEATK = "+speattack+" | SPEDEF = "+spedefense+" | HEALTH = "+health+" | SPEED = "+speed+" | HEIGHT = "+height+"\n"+
           "WEAK AGAINST : "+this.weaknesses()+"\n"+
           "STRONG AGAINST :"+this.resistances();

    return(pokemon);
}

}
