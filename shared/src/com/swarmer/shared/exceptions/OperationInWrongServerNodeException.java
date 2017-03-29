package com.swarmer.shared.exceptions;

/**
 * Created by Matt on 03/30/2017.
 */
public class OperationInWrongServerNodeException extends Throwable {
    public OperationInWrongServerNodeException() {
    }

    public OperationInWrongServerNodeException(String message) {
        super(message);
    }
}
