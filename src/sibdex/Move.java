/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sibdex;

/**
 *
 * @author windows
 */
public class Move {
    protected int id,pp,power ;
    protected String name,type,category;
    protected double accuracy;

    public Move(int id, int pp, int power, String name, String type, String category, double accuracy) {
        this.id = id;
        this.pp = pp;
        this.power = power;
        this.name = name;
        this.type = type;
        this.category = category;
        this.accuracy = accuracy;
    }
        //effectiveness of a move on a defending pokemon
        public double effectiveness (Pokemon defendingPokemon) {
      String[] defenderTypes = {defendingPokemon.getType1(), defendingPokemon.getType2()};
      double effectiveness = 1;
      String attackType = this.type;

      for (String type : defenderTypes) {

        if (attackType.equals("normal")) {
          if (type.equals("rock") || type.equals("steel")) {
            effectiveness *= .5;
          } else if (type.equals("ghost")) {
            effectiveness = 0;
          }
        }
        if(attackType.equals("dark")){
            if(type.equals("fight") || type.equals("dark") ||  type.equals("fairy")){
               effectiveness *= 0.5;
            } else if ( type.equals("psychic") ||  type.equals("ghost")){
                effectiveness *= 2;
            }
        if(attackType.equals("steel")){
            if( type.equals("fire") ||  type.equals("water") ||  type.equals("electricity") ||  type.equals("steel")){
                effectiveness *= 0.5;
            } else if ( type.equals("ice") ||  type.equals("rock") ||  type.equals("fairy")){
                effectiveness *= 2;
            }
        }
        }
        if(attackType.equals("fairy")){
            if(type.equals("fire") || type.equals("poison") || type.equals("steel")){
                effectiveness *= 0.5;
            } else if (type.equals("fight") || type.equals("dragon") || type.equals("dark")){
                effectiveness *= 2 ;
            }
        }
        if (attackType.equals("fire")) {
          if (type.equals("fire") || type.equals("water") || type.equals("rock") || type.equals("dragon")) {
            effectiveness *= .5;
          } else if (type.equals("grass") || type.equals("bug") || type.equals("ice") ||  type.equals("steel")) {
            effectiveness *=2;
          }
        }
        if (attackType.equals("water")) {
          if (type.equals("fire") || type.equals("ground") || type.equals("rock")) {
            effectiveness *=2;
          } else if (type.equals("water") || type.equals("grass") || type.equals("dragon")) {
            effectiveness *=.5;
          }
        }
        if (attackType.equals("electric")) {
          if (type.equals("water") || type.equals("flying")) {
            effectiveness *=2;
          } else if (type.equals("electric") || type.equals("grass") || type.equals("dragon")) {
            effectiveness *= .5;
          } else if (type.equals("ground")) {
            effectiveness = 0;
          }
        }
        if (attackType.equals("grass")) {
          if (type.equals("fire") || type.equals("grass") || type.equals("poison") || type.equals("flying") || type.equals("Bug") || type.equals("dragon") ||  type.equals("steel")) {
            effectiveness *= .5;
          } else if (type.equals("water") || type.equals("ground") || type.equals("rock")) {
            effectiveness *= 2;
          }
        }
        if (attackType.equals("ice")) {
          if (type.equals("fire") || type.equals("water") || type.equals("ice") ||  type.equals("steel")) {
            effectiveness *= .5;
          } else if (type.equals("grass") || type.equals("ground") || type.equals("flying") || type.equals("dragon")) {
            effectiveness *=2;
          }
        }
        if (attackType.equals("fighting")) {
          if (type.equals("normal") || type.equals("ice") || type.equals("rock") ||  type.equals("dark") ||  type.equals("steel")) {
            effectiveness *=2;
          } else if (type.equals("poison") || type.equals("flying") || type.equals("psychic") || type.equals("bug") ||  type.equals("fairy")) {
            effectiveness *=.5;
          } else if (type.equals("ghost")) {
            effectiveness = 0;
          }
        }
        if (attackType.equals("poison")) {
          if (type.equals("grass") ||  type.equals("fairy")) {
            effectiveness *= 2;
          } else if (type.equals("poison") || type.equals("ground") || type.equals("rock") || type.equals("ghost")) {
            effectiveness *=.5;
          } else if ( type.equals("steel")){
              effectiveness *= 0.5;
          }
        }
        if (attackType.equals("ground")) {
          if (type.equals("fire") || type.equals("electric") || type.equals("poison") || type.equals("rock") ||  type.equals("steel")) {
            effectiveness *=2;
          } else if (type.equals("grass") || type.equals("bug")) {
            effectiveness *=.5;
          } else if (type.equals("flying")) {
            effectiveness = 0;
          }
        }
        if (attackType.equals("flying")) {
          if (type.equals("grass") || type.equals("fighting") || type.equals("bug")) {
            effectiveness *= 2;
          } else if (type.equals("electric") || type.equals("rock") || type.equals("steel")) {
            effectiveness *=.5;
          }
        }
        if (attackType.equals("psychic")) {
          if (type.equals("fighting") || type.equals("ground")) {
            effectiveness *= 2;
          } else if (type.equals("psychic") ||  type.equals("steel")) {
            effectiveness *=.5;
          } else if ( type.equals("dark")){
            effectiveness *= 0.5;
          }
        }
        if (attackType.equals("bug")) {
          if (type.equals("grass") || type.equals("psychic") || type.equals("dark")) {
            effectiveness *=2;
          } else if (type.equals("fire") || type.equals("fighting") || type.equals("poison") || type.equals("flying") || type.equals("ghost") ||  type.equals("steel") ||  type.equals("fairy")) {
            effectiveness *=.5;
          }
        }
        if (attackType.equals("rock")) {
          if (type.equals("fire") || type.equals("ice") || type.equals("flying") || type.equals("bug")) {
            effectiveness *=2;
          } else if (type.equals("fighting") || type.equals("ground") ||  type.equals("steel")) {
            effectiveness *=.5;
          }
        }
        if (attackType.equals("ghost")) {
          if (type.equals("psychic") || type.equals("ghost")) {
            effectiveness *=2;
          } else if (type.equals("normal")) {
            effectiveness *=0;
          }
        }
        if (attackType.equals("dragon")) {
          if (type.equals("dragon")) {
            effectiveness *=2;
          } else if ( type.equals("steel")){
              effectiveness *= 0.5;
          } else if ( type.equals("fairy")){
              effectiveness *= 0;
          }
        }

      }
      return effectiveness;
    }
    
}
