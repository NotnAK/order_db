package com.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.shared.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    private int id;
    private String name;
    private String email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
