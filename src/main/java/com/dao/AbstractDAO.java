package com.dao;

import com.shared.Id;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public abstract class AbstractDAO<T> {
    private final Connection conn;
    private final String table;

    public AbstractDAO(Connection conn, String table) {
        this.conn = conn;
        this.table = table;
    }

    public void createTable(Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        Field id = getPrimaryKeyField(fields);

        StringJoiner sql = new StringJoiner(",", "CREATE TABLE " + table + " (", ")");
        sql.add(id.getName() + " INT AUTO_INCREMENT PRIMARY KEY");

        Stream.of(fields)
                .filter(f -> !f.equals(id))
                .forEach(f -> {
                    f.setAccessible(true);
                    String fieldType;
                    if (f.getType() == int.class) {
                        fieldType = "INT";
                    } else if (f.getType() == String.class) {
                        fieldType = "VARCHAR(100)";
                    }
                    else if (f.getType() == BigDecimal.class) {
                        fieldType = "DECIMAL(10, 2)";
                    }
                    else {
                        throw new RuntimeException("Wrong type: " + f.getType());
                    }
                    sql.add(f.getName() + " " + fieldType);
                });

        try (Statement st = conn.createStatement()) {
            st.execute(sql.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    private Field getPrimaryKeyField(Field[] fields) {
        return Stream.of(fields).filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("No Id field found"));
    }
    public void add(T t){
        try{
            Field[] fields = t.getClass().getDeclaredFields();
            Field id = getPrimaryKeyField(fields);
            StringJoiner names = new StringJoiner(",");
            StringJoiner values = new StringJoiner(",");
            Stream.of(fields)
                    .filter(f -> !f.equals(id))
                    .forEach(f -> {
                        f.setAccessible(true);
                        try {
                            names.add(f.getName());
                            values.add("'" + f.get(t) + "'");
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            String sql = " INSERT INTO " + table + "(" + names.toString() +  ") VALUES(" + values.toString() + ")";
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void update(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            Field id = getPrimaryKeyField(fields);

            StringJoiner sj = new StringJoiner(",");
            Stream.of(fields)
                    .filter(f -> !f.equals(id))
                    .forEach(f -> {
                        f.setAccessible(true);
                        try {
                            sj.add(f.getName() + " = '" + f.get(t) + "'");
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
            String sql = "UPDATE " + table + " SET " + sj.toString() + " WHERE " +
                    id.getName() + " = '" + id.get(t) + "'";

            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void delete(T t) {
        try {
            Field[] fields = t.getClass().getDeclaredFields();
            Field id = getPrimaryKeyField(fields);
            id.setAccessible(true);
            String sql = "DELETE FROM " + table + " WHERE " + id.getName() + " = '" + id.get(t) + "'";
            try (Statement st = conn.createStatement()) {
                st.execute(sql);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS " + table;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
    public List<T> getAll(Class<T> cls) {
        List<T> res = new ArrayList<>();

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + table)) {
            ResultSetMetaData md = rs.getMetaData();

            while (rs.next()) {
                T t = cls.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    String columnName = md.getColumnName(i);
                    Field field = cls.getDeclaredField(columnName);
                    field.setAccessible(true);

                    field.set(t, rs.getObject(columnName));
                }

                res.add(t);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return res;
    }
}
