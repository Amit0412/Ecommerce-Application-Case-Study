package dao;

import entity.Customers;
import entity.Products;
import exception.CustomerNotFoundException;
import exception.OrderNotFoundException;
import exception.ProductNotFoundException;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessorRepositoryImpl implements OrderProcessorRepository {
    Connection connection;
    public OrderProcessorRepositoryImpl() {
        connection = DBConnection.getConnection();
    }

    @Override
    public boolean addToCart(Customers customer, Products products, int quantity) {
        try{
            if(!Products.productFound(products.getProduct_id())){
                throw new ProductNotFoundException("Product not found");
            }
            if(!Customers.customerFound(customer.getCustomer_id())){
                throw new CustomerNotFoundException("Customer not found");
            }
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO cart (customer_id, Product_id, quantity) " +
                        "VALUES (?, ?, ?)")) {
            statement.setInt(1, customer.getCustomer_id());
            statement.setInt(2, products.getProduct_id());
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        } catch (ProductNotFoundException | CustomerNotFoundException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeFromCart(Customers customer, Products Products) {
        try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM cart WHERE customer_id = ? AND Product_id = ?")) {
            statement.setInt(1, customer.getCustomer_id());
            statement.setInt(2, Products.getProduct_id());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }

    @Override
    public List<Products> getAllFromCart(Customers customer) {
        List<Products> cartItems = new ArrayList<>();
        try{
            if(!Customers.customerFound(customer.getCustomer_id())){
                throw new CustomerNotFoundException("Customer Not Found");
            }
            try (PreparedStatement statement = connection.prepareStatement("SELECT p.* FROM cart c JOIN Products p ON c.Product_id = p.Product_id WHERE c.customer_id = ?")) {
            statement.setInt(1, customer.getCustomer_id());

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Products Products = new Products(
                            resultSet.getInt("Product_id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getString("description"),
                            resultSet.getInt("stockQuantity"));

                    cartItems.add(Products);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }catch (CustomerNotFoundException cnf){
            cnf.printStackTrace();
        }
        return cartItems;
    }

    @Override
    public boolean placeOrder(Customers customer, List<Map<Products, Integer>> Products, String shippingAddress) {
        try {
            double totalPrice = calculateTotalPrice(Products);

            try (PreparedStatement orderStatement = connection.prepareStatement(
                    "INSERT INTO orders (customer_id, order_date, total_price, shipping_address) VALUES (?, CURRENT_DATE, ?, ?)",Statement.RETURN_GENERATED_KEYS)){
                orderStatement.setInt(1, customer.getCustomer_id());
                orderStatement.setDouble(2, totalPrice);
                orderStatement.setString(3, shippingAddress);

                orderStatement.executeUpdate();
                try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        try (PreparedStatement orderItemStatement = connection.prepareStatement(
                                "INSERT INTO order_items (order_id, Products_id, quantity) " +
                                        "VALUES (?, ?, ?)")) {

                            for (Map<Products, Integer> entry : Products) {
                                for (Map.Entry<Products, Integer> item : entry.entrySet()) {
                                    orderItemStatement.setInt(1, orderId);
                                    orderItemStatement.setInt(2, item.getKey().getProduct_id());
                                    orderItemStatement.setInt(3, item.getValue());

                                    orderItemStatement.addBatch();
                                }
                            }
                        }
                    }
                }
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }

    @Override
    public boolean createProduct(Products Products) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO Products (Product_id, name, price, description, stockQuantity) " +
                        "VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, Products.getProduct_id());
            statement.setString(2, Products.getName());
            statement.setDouble(3, Products.getPrice());
            statement.setString(4, Products.getDescription());
            statement.setInt(5, Products.getStockQuantity());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }

    @Override
    public boolean createCustomer(Customers customer) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO customers (customer_id, name, email, password) " +
                        "VALUES (?, ?, ?, ?)")) {
            statement.setInt(1, customer.getCustomer_id());
            statement.setString(2, customer.getName());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getPassword());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteProduct(int ProductsId) {
        try{
            try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM Products WHERE Product_id = ?")) {
            statement.setInt(1, ProductsId);
            if(statement.executeUpdate() > 0){
                return true;
            }
            else{
                throw new ProductNotFoundException("Product doesn't exist");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        } catch (ProductNotFoundException pnf){
            pnf.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(int customerId) {
        try{
            try (PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM customers WHERE customer_id = ?")) {
            statement.setInt(1, customerId);
            if(statement.executeUpdate() > 0){
                return true;
            }
            else{
                throw new CustomerNotFoundException("Customer is not found");
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        } catch (CustomerNotFoundException cnf){
            cnf.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<Products, Integer>> getOrdersByCustomer(int customerId) {
        List<Map<Products, Integer>> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT p.*, oi.quantity FROM orders o JOIN order_items oi ON o.order_id = oi.order_id " +
                        "JOIN Products p ON oi.Product_id = p.Product_id WHERE o.customer_id = ?")) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Products Products = new Products(
                            resultSet.getInt("Product_id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getString("description"),
                            resultSet.getInt("stockQuantity"));
                    int quantity = resultSet.getInt("quantity");
                    Map<Products, Integer> orderItem = new HashMap<>();
                    orderItem.put(Products, quantity);

                    orders.add(orderItem);
                }
            }
            try{
                if(orders.isEmpty()){
                    throw new OrderNotFoundException("Order Not Found");
                }
            } catch (OrderNotFoundException onf){
                onf.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    //Helper Method
    private double calculateTotalPrice(List<Map<Products, Integer>> Products) {
        double totalPrice = 0.0;
        for (Map<Products, Integer> entry : Products) {
            for (Map.Entry<Products, Integer> item : entry.entrySet()) {
                totalPrice += item.getKey().getPrice() * item.getValue();
            }
        }
        return totalPrice;
    }
}