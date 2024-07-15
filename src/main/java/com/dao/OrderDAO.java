package com.dao;

import com.entity.Order;

import java.sql.Connection;

public class OrderDAO extends AbstractDAO<Order> {
    public OrderDAO(Connection conn, String table) {
        super(conn, table);
    }
}
