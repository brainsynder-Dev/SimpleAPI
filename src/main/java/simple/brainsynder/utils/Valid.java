package simple.brainsynder.utils;

import simple.brainsynder.exceptions.SimpleAPIException;

public class Valid {
    public static void isTrue(boolean expression, String message, Object value) {
        if(!expression) {
            throw new SimpleAPIException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message, long value) {
        if(!expression) {
            throw new SimpleAPIException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message, double value) {
        if(!expression) {
            throw new SimpleAPIException(message + value);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if(!expression) {
            throw new SimpleAPIException(message);
        }
    }

    public static void isTrue(boolean expression) {
        if(!expression) {
            throw new SimpleAPIException("The expression is false");
        }
    }

    public static void notNull(Object object) {
        notNull(object, "The object is null");
    }

    public static void notNull(Object object, String message) {
        if(object == null) {
            throw new SimpleAPIException(message);
        }
    }
}
