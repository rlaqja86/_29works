package org._29cm.homework.product.exception;

public class SoldOutException extends RuntimeException {
    public SoldOutException(String msg) {
        super(msg);
    }
}
