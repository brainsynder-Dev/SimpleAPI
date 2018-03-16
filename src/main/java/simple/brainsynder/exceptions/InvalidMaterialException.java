package simple.brainsynder.exceptions;

public class InvalidMaterialException extends Exception{
    public InvalidMaterialException(String clazz, String message) {
        super("SimpleAPI Error With Class:[" + clazz + "] >> " + message);
    }
}
