package com.main;

import com.dao.CustomerDAO;
import com.dao.OrderDAO;
import com.dao.ProductDAO;
import com.entity.Customer;
import com.entity.Order;
import com.entity.Product;
import com.shared.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        try(Connection conn = ConnectionFactory.getConnection()){
            CustomerDAO customerDAO = new CustomerDAO(conn, "Customers");
            OrderDAO orderDAO = new OrderDAO(conn, "Orders");
            ProductDAO productDAO = new ProductDAO(conn, "Products");

            customerDAO.dropTable();
            orderDAO.dropTable();
            productDAO.dropTable();

            customerDAO.createTable(Customer.class);
            productDAO.createTable(Product.class);
            orderDAO.createTable(Order.class);


            Scanner sc = new Scanner(System.in);
            while (true) {
                System.out.println("1: Add customer");
                System.out.println("2: Add product");
                System.out.println("3: Create order");
                System.out.println("4: View customers");
                System.out.println("5: View products");
                System.out.println("6: Delete customer");
                System.out.println("7: Delete product");
                System.out.println("8: Delete order");
                System.out.println("9: View orders");
                System.out.print("-> ");
                String s = sc.nextLine();
                try {
                    switch (s) {
                        case "1":
                            UserService.addCustomer(sc, customerDAO);
                            break;
                        case "2":
                            UserService.addProduct(sc, productDAO);
                            break;
                        case "3":
                            UserService.createOrder(sc, orderDAO);
                            break;
                        case "4":
                            UserService.viewCustomers(customerDAO);
                            break;
                        case "5":
                            UserService.viewProducts(productDAO);
                            break;
                        case "6":
                            UserService.deleteCustomer(sc, customerDAO);
                            break;
                        case "7":
                            UserService.deleteProduct(sc, productDAO);
                            break;
                        case "8":
                            UserService.deleteOrder(sc, orderDAO);
                            break;
                        case "9":
                            UserService.viewOrders(conn);
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
