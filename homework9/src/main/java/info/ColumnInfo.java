package info;

import java.lang.reflect.Field;


public class ColumnInfo {
    private String name;
    private Field field;

    public ColumnInfo(String name, Field field) {
        this.name = name;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
