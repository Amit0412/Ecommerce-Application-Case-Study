package entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class Products {
    private int product_id;
    private String name;
    private double price;
    private String description;
    private int stockQuantity;

    public Products(int product_id, String name, double price, String description, int stockQuantity) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public static boolean productFound(int productId) {
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM Products WHERE Product_id = ?")) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString(){
        return "\n"+"Product ID: "+this.getProduct_id() +"\n"+"Name: "+this.getName()+"\n" + "Price: " + this
        .getPrice()+"\n" + "Description: " + this.getDescription()+"\n" + "Stock Quantity: " + this.getStockQuantity()+"\n";
    }
}