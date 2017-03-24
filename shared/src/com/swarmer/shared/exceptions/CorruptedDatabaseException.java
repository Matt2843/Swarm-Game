package com.swarmer.shared.exceptions;

/**
 * Created by Matt on 03/24/2017.
 */
public class CorruptedDatabaseException extends Throwable {

    public CorruptedDatabaseException() {
    }

    public CorruptedDatabaseException(String message) {
        super(message);
    }

}
