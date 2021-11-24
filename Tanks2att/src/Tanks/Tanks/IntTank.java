package Tanks.Tanks;

import Tanks.Field.Graph;
import Tanks.Field.PartField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(as=IntTank.class)
public interface IntTank {
    /**
     * Shows is the tank alive.
     * @return
     */
    boolean isAlive();
    /**
     * Increasing the level of the tank (not yet available).
     * @return
     */
    void skill(boolean lvlUp);
    /**
     * Getting damage
     * @param value
     */
    void damage(int value);
    /**
     * Shooting and dealing damage to enemy tank.
     * @param graph
     * @param attackedTank
     * @param hit
     */
    void shoot(Graph graph, Tank attackedTank, int hit);
    /**
     * Move from the first vertex to the second.
     * @param graph
     * @param v1
     * @param v2
     */
    void move(Graph graph, PartField v1, PartField v2);
    /**
     * Get vertex where the tank is right now.
     * @return
     */
    PartField currentPartField();
    /**
     * Set vertex where the tank is right now.
     * @param currentPartField
     */
    void setCurrentVertex(PartField currentPartField);
    /**
     * Get power of hit.
     * @return
     */
    int getHit();
}

