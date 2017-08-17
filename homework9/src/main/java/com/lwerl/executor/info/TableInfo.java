package com.lwerl.executor.info;

import java.util.List;

public class TableInfo {

    private String name;
    private ColumnInfo primaryKey;
    private List<ColumnInfo> columns;

    public TableInfo(String name, ColumnInfo primaryKey, List<ColumnInfo> columns) {
        this.name = name;
        this.primaryKey = primaryKey;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ColumnInfo getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(ColumnInfo primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }
}
