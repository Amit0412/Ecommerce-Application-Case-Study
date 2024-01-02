package exception;

public class OrderNotFoundException extends Exception {
    String s1 = "";
    public OrderNotFoundException(String message) {
        super(message);
        this.s1 = message;
    }
    @Override
    public String toString(){
        return s1;
    }
}