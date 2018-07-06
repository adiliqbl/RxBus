package com.adiliqbal.rxbus;


public class Optional<T> {

    private T value;

    private Optional() {
        this.value = null;
    }

    private Optional(T value) {
        this.value = value;
    }

    public static <T> Optional<T> empty() {
        return new Optional<>();
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<>(value);
    }

    public T get() {
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }
}
