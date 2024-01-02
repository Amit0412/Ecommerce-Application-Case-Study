package exception;

public class CustomerNotFoundException extends Exception {
    String s1 = "";
    public CustomerNotFoundException(String message) {
        super(message);
        this.s1 = message;
    }
    @Override
    public String toString(){
        return s1;
    }
}
