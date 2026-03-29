package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return createObject(resultSet);
                }
                return null;
            } catch (ReflectiveOperationException | SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), resultSet -> {
                    try {
                        List<T> objects = new ArrayList<>();
                        while (resultSet.next()) {
                            objects.add(createObject(resultSet));
                        }
                        return objects;
                    } catch (ReflectiveOperationException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public long insert(Connection connection, T object) {
        List<Object> params = new ArrayList<>();

        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                params.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }

        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), params);
    }

    @Override
    public void update(Connection connection, T object) {
        List<Object> params = new ArrayList<>();

        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                params.add(field.get(object));
            } catch (IllegalAccessException e) {
                throw new DataTemplateException(e);
            }
        }

        try {
            params.add(entityClassMetaData.getIdField().get(object));
        } catch (IllegalAccessException e) {
            throw new DataTemplateException(e);
        }

        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), params);
    }

    private T createObject(ResultSet resultSet) throws ReflectiveOperationException, SQLException {
        T object = entityClassMetaData.getConstructor().newInstance();

        for (Field field : entityClassMetaData.getAllFields()) {
            field.setAccessible(true);
            field.set(object, resultSet.getObject(field.getName(), field.getType()));
        }

        return object;
    }
}
