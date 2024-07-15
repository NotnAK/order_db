package com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shared.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private int id;
    private int customerId;
    private int productId;
    private int quantity;

    public Order(int customerId, int productId, int quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }
}
