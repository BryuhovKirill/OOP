package ru.vsu.cs.tanks.Tanks;

import ru.vsu.cs.tanks.Field.PartField;

public class Enemy extends Tank{
    public Enemy(PartField currentPartField, int hp, int damage, int range) {
        super(currentPartField,hp,damage,range);
    }
}
