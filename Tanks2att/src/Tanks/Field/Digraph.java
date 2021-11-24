package Tanks.Field;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Интерфейс для описания ориентированного графа (орграфа)
 */
@JsonSerialize(as=Digraph.class)
public interface Digraph extends Graph {
    /**
     * Интерфейс полностью совпадает с Graph
     */
}
