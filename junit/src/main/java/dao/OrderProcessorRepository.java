package dao;

import entity.*;


import java.util.List;
import java.util.Map;

public interface OrderProcessorRepository {
    boolean createProduct(Products product);
    boolean createCustomer(Customers customer);
    boolean deleteProduct(int product_id);
    boolean deleteCustomer(int customer_id);
    boolean addToCart(Customers customer, Products product, int quantity);
    boolean removeFromCart(Customers customer, Products product);
    List<Products> getAllFromCart(Customers customer);
    boolean placeOrder(Customers customer, List<Map<Products, Integer>> products, String shippingAddress);
    List<Map<Products, Integer>> getOrdersByCustomer(int customerId);

}