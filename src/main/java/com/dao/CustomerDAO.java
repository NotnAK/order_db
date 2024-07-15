package com.dao;

import com.entity.Customer;

import java.sql.Connection;

public class CustomerDAO extends AbstractDAO<Customer>{
    public CustomerDAO(Connection conn, String table) {
        super(conn, table);
    }
}
