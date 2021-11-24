package Tanks.Tanks;


import Tanks.Field.PartField;

public class Enemy extends Tank{
    public Enemy(PartField currentPartField, int hp, int damage, int range) {
        super(currentPartField,hp,damage,range);
    }
}
