package helper;

import info.ColumnInfo;
import info.TableInfo;
import model.DataSet;

import java.util.List;
import java.util.stream.Collectors;

public class QueryHelper {

    private QueryHelper() {
    }

    public static <T extends DataSet> String makeSaveQuery(final T object, TableInfo table) {
        String query = "INSERT INTO \"" + table.getName() + "\" ";

        List<ColumnInfo> columns = table.getColumns().stream().filter(c -> {
            Object obj;
            try {
                obj = c.getField().get(object);
            } catch (IllegalAccessException e) {
                obj = null;
            }
            return obj != null;
        }).collect(Collectors.toList());

        String columnsName = columns.stream()
                .map(ColumnInfo::getName)
                .collect(Collectors.joining(",", "(", ")"));

        String columnsValue = columns.stream()
                .map(c -> {
                    try {
                        Object obj = c.getField().get(object);
                        return getValueStringForSQL(obj);
                    } catch (IllegalAccessException e) {
                        // Is never happens
                        return null;
                    }
                }).collect(Collectors.joining(",", "(", ")"));

        query += columnsName + " VALUES " + columnsValue;
        return query;
    }

    public static <T extends DataSet> String makeLoadQuery(final Object primaryKey, TableInfo table) {
        if (primaryKey != null) {
            return "SELECT * FROM \""
                    + table.getName()
                    + "\" WHERE "
                    + table.getPrimaryKey().getName() + " = " + getValueStringForSQL(primaryKey);
        } else {
            return null;
        }
    }

    // Method for simple extensibility support type like ENUM and Date
    public static String getValueStringForSQL(Object value) {
        if (value instanceof String) {
            return "'" + value.toString() + "'";
        } else {
            return value.toString();
        }
    }
}
