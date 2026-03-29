package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.otus.annotation.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> entityClass;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.allFields = List.of(entityClass.getDeclaredFields());
        this.idField = findIdField();
        this.fieldsWithoutId = findFieldsWithoutId();
    }

    public String getName() {
        return entityClass.getSimpleName();
    }

    public Field getIdField() {
        return idField;
    }

    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    public List<Field> getAllFields() {
        return allFields;
    }

    public Constructor<T> getConstructor() {
        try {
            return entityClass.getDeclaredConstructor();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create instance of " + entityClass.getName(), e);
        }
    }

    private Field findIdField() {
        return allFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No field with @Id annotation found in class: " + entityClass.getName()));
    }

    private List<Field> findFieldsWithoutId() {
        List<Field> result = new ArrayList<>();

        for (Field field : allFields) {
            if (!field.equals(idField)) {
                result.add(field);
            }
        }

        return Collections.unmodifiableList(result);
    }
}
