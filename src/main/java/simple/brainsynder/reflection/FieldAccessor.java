package simple.brainsynder.reflection;

import java.lang.reflect.Field;

public abstract class FieldAccessor<T> {
    public abstract T get(Object target);

    public abstract void set(Object target, T value);

    public abstract boolean hasField(Object target);

    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return getField(target, name, fieldType, 0);
    }

    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        while (true) {
            for (final Field field : target.getDeclaredFields()) {
                if (((name == null) || (field.getName().equals(name))) && (fieldType.isAssignableFrom(field.getType())) && (index-- <= 0)) {
                    field.setAccessible(true);

                    return new FieldAccessor<T>() {
                        public T get(Object target) {
                            try {
                                return (T) field.get(target);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot access reflection.", e);
                            }
                        }

                        public void set(Object target, T value) {
                            try {
                                field.set(target, value);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException("Cannot access reflection.", e);
                            }
                        }

                        public boolean hasField(Object target) {
                            return field.getDeclaringClass().isAssignableFrom(target.getClass());
                        }
                    };
                }

            }

            if (target.getSuperclass() != null) {
                target = target.getSuperclass();
                continue;
            }
            throw new IllegalArgumentException("Cannot find field with type " + fieldType);
        }
    }
}