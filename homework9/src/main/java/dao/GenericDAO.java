package dao;

import executor.Executor;
import helper.QueryHelper;
import info.ColumnInfo;
import info.TableInfo;
import model.DataSet;

import java.lang.reflect.Field;

import static helper.TableHelper.makeTableInfo;

public class GenericDAO {

    private Executor executor;

    public GenericDAO(Executor executor) {
        this.executor = executor;
    }

    public <T extends DataSet> void save(T object) {
        TableInfo tableInfo = makeTableInfo(object.getClass());
        if (tableInfo != null) {
            String query = QueryHelper.makeSaveQuery(object, tableInfo);
            executor.execUpdate(query);
        }
    }

    public <T extends DataSet> T load(Object primaryKey, final Class<T> clazz) {
        T result = null;
        final TableInfo tableInfo = makeTableInfo(clazz);
        if (tableInfo != null) {
            String query = QueryHelper.makeLoadQuery(primaryKey, tableInfo);
            if (query != null) {
                result = executor.execQuery(query, resultSet -> {
                    T instance = null;
                    if (resultSet.next()) {
                        instance = clazz.newInstance();

                        ColumnInfo pkColumn = tableInfo.getPrimaryKey();
                        Field pkColumnField = pkColumn.getField();
                        Class<?> pkColumnClass = pkColumnField.getType();
                        pkColumnField.set(instance, resultSet.getObject(pkColumn.getName(), pkColumnClass));

                        for (ColumnInfo column : tableInfo.getColumns()) {
                            Field columnField = column.getField();
                            Class<?> columnClass = columnField.getType();
                            columnField.set(instance, resultSet.getObject(column.getName(), columnClass));
                        }
                    }
                    return instance;
                });
            }
        }
        return result;
    }

}
