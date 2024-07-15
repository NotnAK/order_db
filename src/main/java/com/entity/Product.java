package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shared.Id;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private int id;
    private String name;
    private BigDecimal price;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
