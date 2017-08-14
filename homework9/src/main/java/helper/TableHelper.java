package helper;

import com.google.common.base.CaseFormat;
import info.ColumnInfo;
import info.TableInfo;
import model.DataSet;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class TableHelper {

    private TableHelper() {
    }

    public static TableInfo makeTableInfo(Class<? extends DataSet> clazz) {
        Entity entityAno = clazz.getAnnotation(Entity.class);
        if (entityAno != null) {
            String name;
            ColumnInfo primaryKey = null;
            List<ColumnInfo> columns = new ArrayList<>();
            Table tableAno = clazz.getAnnotation(Table.class);

            name = getTableName(clazz, entityAno, tableAno);

            List<Field> fields = getEntityFields(clazz);

            for (Field field : fields) {

                field.setAccessible(true);
                Column columnAno = field.getAnnotation(Column.class);
                ColumnInfo column = null;

                if (columnAno != null) {
                    if (!columnAno.name().equals("")) {
                        column = new ColumnInfo(columnAno.name(), field);
                    } else {
                        String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                        column = new ColumnInfo(columnName, field);
                    }
                }

                if (primaryKey == null && field.getAnnotation(Id.class) != null) {
                    if (column == null) {
                        String columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                        primaryKey = new ColumnInfo(columnName, field);
                    } else {
                        primaryKey = column;
                    }
                } else {
                    if (column != null) {
                        columns.add(column);
                    }
                }
            }

            if (primaryKey != null) {
                return new TableInfo(name, primaryKey, columns);
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    private static List<Field> getEntityFields(Class<? extends DataSet> clazz) {
        TreeSet<Field> fieldSet = new TreeSet<>((f1, f2) -> {
            if (f1.getName().equals(f2.getName())) {
                return 0;
            } else {
                return 1;
            }
        });

        fieldSet.addAll(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superclass = clazz.getSuperclass();
        while (!superclass.equals(Object.class)) {
            MappedSuperclass superclassAno = superclass.getAnnotation(MappedSuperclass.class);
            if (superclassAno != null) {
                fieldSet.addAll(Arrays.asList(superclass.getDeclaredFields()));
            }
            superclass = superclass.getSuperclass();
        }

        return new ArrayList<>(fieldSet);
    }

    private static String getTableName(Class<? extends DataSet> clazz, Entity entityAno, Table tableAno) {
        String name;
        if (tableAno != null && !tableAno.name().equals("")) {
            name = tableAno.name();
        } else if (!entityAno.name().equals("")) {
            name = entityAno.name();
        } else {
            name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, clazz.getSimpleName());
        }
        return name;
    }
}
