package entity;

class Order_items {
    private int order_item_id;
    private int order_id;
    private int product_id;
    private int quantity;

    public Order_items(){}
    
    public Order_items(int order_item_id, int order_id, int product_id, int quantity) {
        this.order_item_id = order_item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
    }


    public int getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(int order_item_id) {
        this.order_item_id = order_item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString(){
        return "Order Item ID: "+this.getOrder_item_id()+", Order ID: "+this.getOrder_id()
        + ", Product ID: "+this.getProduct_id()+" , Quantity: "+this.getQuantity();
    }
}