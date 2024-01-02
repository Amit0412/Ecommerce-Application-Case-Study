package entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class Customers {
    private int customer_id;
    private String name;
    private String email;
    private String password;

    public Customers(int customer_id, String name, String email, String password) {
        this.customer_id = customer_id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static boolean customerFound(int customerId) {
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) FROM customers WHERE customer_id = ?")) {
            statement.setInt(1, customerId);
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
}