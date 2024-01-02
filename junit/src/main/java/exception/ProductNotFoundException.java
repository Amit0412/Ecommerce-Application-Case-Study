package exception;

public class ProductNotFoundException extends Exception {
    String s1 ="";
    public ProductNotFoundException(String message) {
        super(message);
        this.s1 = message;
    }
    @Override
    public String toString(){
        return s1;
    }
}