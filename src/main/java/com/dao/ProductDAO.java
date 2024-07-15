package com.dao;

import com.entity.Product;

import java.sql.Connection;

public class ProductDAO extends AbstractDAO <Product>{
    public ProductDAO(Connection conn,String table) {
        super(conn, table);
    }
}
