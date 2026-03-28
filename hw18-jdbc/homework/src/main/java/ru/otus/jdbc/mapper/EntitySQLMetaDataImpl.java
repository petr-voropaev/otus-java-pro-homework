package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String tableName;
    private final String idColumnName;
    private final List<String> columnNames;
    private final List<String> nonIdColumnNames;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> metaData) {
        this.tableName = metaData.getName();
        this.idColumnName = metaData.getIdField().getName();
        this.columnNames = metaData.getAllFields().stream().map(Field::getName).collect(Collectors.toList());
        this.nonIdColumnNames =
                metaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.toList());
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT %s FROM %s;", String.join(", ", columnNames), tableName);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format(
                "SELECT %s FROM %s WHERE %s = ?;", String.join(", ", columnNames), tableName, idColumnName);
    }

    @Override
    public String getInsertSql() {
        String columns = String.join(", ", nonIdColumnNames);
        String values = nonIdColumnNames.stream().map(col -> "?").collect(Collectors.joining(", "));

        return String.format("INSERT INTO %s (%s) VALUES (%s);", tableName, columns, values);
    }

    @Override
    public String getUpdateSql() {
        String values = nonIdColumnNames.stream().map(col -> col + " = ?").collect(Collectors.joining(", "));

        return String.format("UPDATE %s SET %s WHERE %s = ?;", tableName, values, idColumnName);
    }
}
