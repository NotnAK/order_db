package com.main;

import com.dao.CustomerDAO;
import com.dao.OrderDAO;
import com.dao.ProductDAO;
import com.entity.Customer;
import com.entity.Order;
import com.entity.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class UserService {
    public static void addCustomer(Scanner sc, CustomerDAO customerDAO) {
        try {
            System.out.print("Enter customer name: ");
            String customerName = sc.nextLine();
            System.out.print("Enter customer email: ");
            String customerEmail = sc.nextLine();
            customerDAO.add(new Customer(customerName, customerEmail));
        } catch (Exception e) {
            System.out.println("Failed to add customer. Error: " + e.getMessage());
        }
    }

    public static void addProduct(Scanner sc, ProductDAO productDAO) {
        try {
            System.out.print("Enter product name: ");
            String productName = sc.nextLine();
            System.out.print("Enter product price: ");
            String productPrice = sc.nextLine();
            productDAO.add(new Product(productName, new BigDecimal(productPrice)));
        } catch (Exception e) {
            System.out.println("Failed to add product. Error: " + e.getMessage());
        }
    }

    public static void createOrder(Scanner sc, OrderDAO orderDAO) {
        try {
            System.out.print("Enter customer ID: ");
            int customerId = Integer.parseInt(sc.nextLine());
            System.out.print("Enter product ID: ");
            int productId = Integer.parseInt(sc.nextLine());
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());
            orderDAO.add(new Order(customerId, productId, quantity));
        }
        catch (Exception e) {
            System.out.println("Failed to create order. Error: " + e.getMessage());
        }
    }

    public static void viewCustomers(CustomerDAO customerDAO) {
        try {
            List<Customer> customers = customerDAO.getAll(Customer.class);
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve customers. Error: " + e.getMessage());
        }
    }

    public static void viewProducts(ProductDAO productDAO) {
        try {
            List<Product> products = productDAO.getAll(Product.class);
            for (Product product : products) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve products. Error: " + e.getMessage());
        }
    }

    public static void deleteCustomer(Scanner sc, CustomerDAO customerDAO) {
        try {
            System.out.print("Enter customer ID to delete: ");
            int deleteCustomerId = Integer.parseInt(sc.nextLine());
            customerDAO.delete(new Customer(deleteCustomerId, null, null));
        } catch (Exception e) {
            System.out.println("Failed to delete customer. Error: " + e.getMessage());
        }
    }

    public static void deleteProduct(Scanner sc, ProductDAO productDAO) {
        try {
            System.out.print("Enter product ID to delete: ");
            int deleteProductId = Integer.parseInt(sc.nextLine());
            productDAO.delete(new Product(deleteProductId, null, null));
        }catch (Exception e) {
            System.out.println("Failed to delete product. Error: " + e.getMessage());
        }
    }

    public static void deleteOrder(Scanner sc, OrderDAO orderDAO) {
        try {
            System.out.print("Enter order ID to delete: ");
            int deleteOrderId = Integer.parseInt(sc.nextLine());
            orderDAO.delete(new Order(deleteOrderId, 0, 0, 0));
        }catch (Exception e) {
            System.out.println("Failed to delete order. Error: " + e.getMessage());
        }
    }

    public static void viewOrders(Connection connection) {
        String query = "SELECT Orders.id, Customers.name AS customer_name, Products.name AS product_name, Orders.quantity " +
                "FROM Orders " +
                "JOIN Customers ON Orders.customerId = Customers.id " +
                "JOIN Products ON Orders.productId = Products.id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int orderId = resultSet.getInt("id");
                String customerName = resultSet.getString("customer_name");
                String productName = resultSet.getString("product_name");
                int quantity = resultSet.getInt("quantity");

                System.out.format("Order ID: %d, Customer: %s, Product: %s, Quantity: %d\n",
                        orderId, customerName, productName, quantity);
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve orders. Error: " + e.getMessage());
        }
    }
}
