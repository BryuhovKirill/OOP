package Tanks.Tanks;


import Tanks.Field.Graph;
import Tanks.Field.PartField;

public class Tank {
    private PartField currentPartField;
    private int hp;
    private final int damage;

    public Tank(PartField currentPartField, int hp, int damage, int rangeAttack) {
        this.currentPartField = currentPartField;
        this.hp = hp;
        this.damage = damage;
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
