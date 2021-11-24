package Tanks.Field;

public class PartField {
    private final int index;
    private char type;

    public PartField(int index, char type) {
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }
    public char getType() {
        return type;
    }
    public void setType(char type) {
        this.type = type;
    }
}
