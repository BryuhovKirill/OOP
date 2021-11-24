package ru.vsu.cs.tanks.Tanks;

import ru.vsu.cs.tanks.Field.Graph;
import ru.vsu.cs.tanks.Field.PartField;

public class Tank {
    private PartField currentPartField;
    private int hp;
    private final int damage;
    private final int rangeAttack;

    public Tank(PartField currentPartField, int hp, int damage, int rangeAttack) {
        this.currentPartField = currentPartField;
        this.hp = hp;
        this.damage = damage;
        this.rangeAttack = rangeAttack;
    }
    // возвращает количество хп
    public boolean isAlive() {
        return this.hp > 0;
    }
    // стреляет по выбраному танку
    public void shoot(Graph field, Tank attackedTank, int hit) {
        attackedTank.lossOfHP(hit);

    }
    // возвращает количество урона наносимого танком
    public int getDamage() {
        return damage;
    }

    // двигает танк из одной части в другую
    public void move(Graph field, PartField p1, PartField p2) {
        if (field.isAdj(p1.getIndex(), p2.getIndex())) {
            currentPartField = p2;
        }
    }
    // возвращает дальность атаки

    public int getRangeAttack() {
        return rangeAttack;
    }

    //добовляет хп
    public void setHp(int hp) {
        this.hp += hp;
    }

    // возвращает нынешнее положение танка
    public PartField getPartField() {
        return this.currentPartField;
    }

    // изменяет нынешнее положение танка
    public void setPartField(PartField currentPartField) {
        this.currentPartField = currentPartField;
    }
    // танк теряет количество хп
    public void lossOfHP(int damageHP) {
        this.hp -= damageHP;
    }
}
