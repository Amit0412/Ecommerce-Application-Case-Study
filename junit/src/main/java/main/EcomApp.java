package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dao.OrderProcessorRepository;
import dao.OrderProcessorRepositoryImpl;
import entity.Customers;
import entity.Products;


public class EcomApp {
    private static final OrderProcessorRepository orderProcessorRepository = new OrderProcessorRepositoryImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args){
        while (true) {
            System.out.println("E-commerce Application Menu:");
            System.out.println("1. Register Customers");
            System.out.println("2. Create Products");
            System.out.println("3. Delete Products");
            System.out.println("4. Add to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Place Order");
            System.out.println("7. View Customers Order");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerCustomers();
                    break;
                case 2:
                    createProducts();
                    break;
                case 3:
                    deleteProducts();
                    break;
                case 4:
                    addToCart();
                    break;
                case 5:
                    viewCart();
                    break;
                case 6:
                    placeOrder();
                    break;
                case 7:
                    viewCustomersOrder();
                    break;
                case 8:
                    System.out.println("Exiting the application. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void registerCustomers() {
        System.out.print("Enter Customers ID: ");
        int CustomersId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Customers name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Customers email: ");
        String email = scanner.nextLine();

        System.out.print("Enter Customers password: ");
        String password = scanner.nextLine();

        Customers Customers = new Customers(CustomersId, name, email, password);
        boolean result = orderProcessorRepository.createCustomer(Customers);

        if (result) {
            System.out.println("Customers registered successfully.");
        } else {
            System.out.println("Failed to register Customers.");
        }
    }

    private static void createProducts() {
        System.out.print("Enter Products ID: ");
        int ProductsId = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("Enter Products name: ");
        String name = scanner.nextLine();
    
        System.out.print("Enter Products price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Products description: ");
        String description = scanner.nextLine();
    
        System.out.print("Enter Products stock quantity: ");
        int stockQuantity = scanner.nextInt();
        scanner.nextLine(); 
    
        Products Products = new Products(ProductsId, name, price, description, stockQuantity);
        boolean result = orderProcessorRepository.createProduct(Products);
    
        if (result) {
            System.out.println("Products created successfully.");
        } else {
            System.out.println("Failed to create Products.");
        }
    }
    
    private static void deleteProducts() {
        System.out.print("Enter Products ID to delete: ");
        int ProductsId = scanner.nextInt();
        boolean result = orderProcessorRepository.deleteProduct(ProductsId);
    
        if (result) {
            System.out.println("Products deleted successfully.");
        } else {
            System.out.println("Failed to delete Products. Products ID might not exist.");
        }
    }
    
    private static void addToCart() {
        System.out.print("Enter Customers ID: ");
        int CustomersId = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("Enter Products ID to add to cart: ");
        int ProductsId = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("Enter quantity to add to cart: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
    
        Customers Customers = new Customers(CustomersId, "", "", "");
        Products Products = new Products(ProductsId, "", 0.0, "", 0);
    
        boolean result = orderProcessorRepository.addToCart(Customers, Products, quantity);
    
        if (result) {
            System.out.println("Products added to cart successfully.");
        } else {
            System.out.println("Failed to add Products to cart. Customers or Products ID might not exist.");
        }
    }
    
    private static void viewCart() {
        System.out.print("Enter Customers ID to view cart: ");
        int CustomersId = scanner.nextInt();
        scanner.nextLine(); 
    
        Customers Customers = new Customers(CustomersId, "", "", "");
        List<Products> cartItems = orderProcessorRepository.getAllFromCart(Customers);
    
        if (!cartItems.isEmpty()) {
            System.out.println("Cart Items:");
            for (Products item : cartItems) {
                System.out.println(item.toString());
            }
        } else {
            System.out.println("Cart is empty.");
        }
    }
    
    private static void placeOrder() {
        System.out.print("Enter Customers ID to place order: ");
        int CustomersId = scanner.nextInt();
        scanner.nextLine(); 
    
        System.out.print("Enter shipping address: ");
        String shippingAddress = scanner.nextLine();
    
        Customers Customers = new Customers(CustomersId, "", "", "");
    
        List<Map<Products, Integer>> Productss = new ArrayList<>();
        Map<Products, Integer> orderItem = new HashMap<>();
        orderItem.put(new Products(1, "Sample Products", 10.0, "Sample Description", 50), 2);
        Productss.add(orderItem);
    
        boolean result = orderProcessorRepository.placeOrder(Customers, Productss, shippingAddress);
    
        if (result) {
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Failed to place order. Customers or Products ID might not exist.");
        }
    }
    
    private static void viewCustomersOrder() {
        System.out.print("Enter Customers ID to view orders: ");
        int CustomersId = scanner.nextInt();
        scanner.nextLine();
    
        List<Map<Products, Integer>> orders = orderProcessorRepository.getOrdersByCustomer(CustomersId);
    
        if (!orders.isEmpty()) {
            System.out.println("Customers Orders:");
            for (Map<Products, Integer> order : orders) {
                for (Map.Entry<Products, Integer> item : order.entrySet()) {
                    System.out.println(item.getKey().toString() + "Quantity: " + item.getValue() + "\n");
                }
            }
        } else {
            System.out.println("No orders found for the Customers.");
        }
    }
}