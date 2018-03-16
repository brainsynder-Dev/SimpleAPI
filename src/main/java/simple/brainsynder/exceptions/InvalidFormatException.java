package simple.brainsynder.exceptions;

public class InvalidFormatException extends Exception{
    public InvalidFormatException(String clazz, String message) {
        super("SimpleAPI Error With Class:[" + clazz + "] >> " + message);
    }
}
