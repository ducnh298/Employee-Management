package ducnh.springboot.CustomException;

public class CheckinException extends Exception{
    public CheckinException(String message) {
        super(message);
    }
}
