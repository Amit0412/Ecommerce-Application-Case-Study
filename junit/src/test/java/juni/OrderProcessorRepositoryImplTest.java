package juni;

import entity.Customers;


import entity.Products;
import exception.CustomerNotFoundException;
import exception.ProductNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.OrderProcessorRepository;
import dao.OrderProcessorRepositoryImpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderProcessorRepositoryImplTest {
    private OrderProcessorRepository orderProcessorRepository;

    @BeforeEach
    void setUp() {
        // You might initialize a test database or mock the necessary dependencies here
        orderProcessorRepository = new OrderProcessorRepositoryImpl();
    }

    @Test
    void testCreateProduct() {
        Products product = new Products(1, "Test Product", 19.99, "Sample description", 50);

        assertTrue(orderProcessorRepository.createProduct(product));
    }

    @Test
    void testAddToCart() {
        Customers customer = new Customers(1, "John Doe", "john@example.com", "password");
        Products product = new Products(1, "Test Product", 19.99, "Sample description", 50);

        assertTrue(orderProcessorRepository.addToCart(customer, product, 2));
    }

    @Test
    void testPlaceOrder() {
        Customers customer = new Customers(1, "John Doe", "john@example.com", "password");
        Products product = new Products(1, "Test Product", 19.99, "Sample description", 50);

        // Assuming addToCart method is working correctly
        orderProcessorRepository.addToCart(customer, product, 2);

        // Retrieve cart items
        List<Products> cartItems = orderProcessorRepository.getAllFromCart(customer);

        // Create a map of products and quantities
        Map<Products, Integer> productsQuantityMap = new HashMap<>();
        for (Products cartProduct : cartItems) {
            productsQuantityMap.put(cartProduct, 2); // Assuming the quantity is 2 for all items
        }

        assertTrue(orderProcessorRepository.placeOrder(customer, (List<Map<Products, Integer>>) productsQuantityMap, "Shipping Address"));
    }

    @Test
    void testExceptionThrownWhenCustomerIdNotFound() {
        Customers nonExistingCustomer = new Customers(999, "Nonexistent Customer", "nonexistent@example.com", "password");
        Products product = new Products(1, "Test Product", 19.99, "Sample description", 50);

        // Ensure an exception is thrown when trying to add to the cart for a non-existing customer
        assertThrows(CustomerNotFoundException.class, () -> orderProcessorRepository.addToCart(nonExistingCustomer, product, 1));
    }

    @Test
    void testExceptionThrownWhenProductIdNotFound() {
        Customers customer = new Customers(1, "John Doe", "john@example.com", "password");
        Products nonExistingProduct = new Products(999, "Nonexistent Product", 9.99, "Nonexistent description", 10);

        // Ensure an exception is thrown when trying to add a non-existing product to the cart
        assertThrows(ProductNotFoundException.class, () -> orderProcessorRepository.addToCart(customer, nonExistingProduct, 1));
    }
}