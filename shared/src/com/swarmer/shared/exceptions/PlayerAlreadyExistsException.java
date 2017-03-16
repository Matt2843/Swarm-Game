package com.swarmer.shared.exceptions;

/**
 * Created by Matt on 03/16/2017.
 */
public class PlayerAlreadyExistsException extends Throwable {

    public PlayerAlreadyExistsException() {
    }

    public PlayerAlreadyExistsException(String message) {
        super(message);
    }

}
